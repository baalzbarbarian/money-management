package com.example.assignment_pd03241.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.assignment_pd03241.mDatabase.mMoney;
import com.example.assignment_pd03241.mDatabase.mSumLoaiThu;

import java.util.ArrayList;
import java.util.List;

public class DatabaseDAO extends SQLiteOpenHelper {

    SQLiteDatabase sqLiteDatabase;
    Context context;
    private static final String DATABASE_NAME = "quanlythuchi.db";
    private static final String SUMKT_TABLE = "nametoctable";
    private static final String KHOANTHU_TABLE = "khoanthutable";
    private static final String ID = "id";
    private static final String NAME_T = "nameT";
    private static final String AMOUNT_T = "amountT";
    private static final String DATE_T = "dateT";
    private static final String SUM_AMOUNT = "sumAmountThu";

    private static final String BANGTONGCHITIEU = "tongchitable";
    private static final String TENLOAICHITIEU = "tenchitieu";
    private static final String TONGCHITIEU = "tongchitieu";
    private static final String BANGCHITIETCHITIEU = "chitietchitieutable";
    private static final String TIENCHITIEU = "tienchitieu";
    private static final String NGAYCHITIEU = "ngaychitieu";

    private static final String TAG = "Data";

    private static final String sumThuTable = "CREATE TABLE " +SUMKT_TABLE+ "("
            +ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +NAME_T+ " TEXT, "
            +SUM_AMOUNT+ " double);";
    private static final String khoanThuTable = "CREATE TABLE " +KHOANTHU_TABLE+ "("
            +ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +NAME_T+ " TEXT, "
            +AMOUNT_T+ " DOUBLE, "
            +DATE_T+ " TEXT, "
            +"FOREIGN KEY ("+NAME_T+") REFERENCES " +SUMKT_TABLE+ " ("+NAME_T+"));";

    private static final String bangTongChiTieu = "CREATE TABLE tongchitable " +
            "(id integer primary key autoincrement, tenchitieu text, tongchitieu double);";

    private static final String bangChiTietChiTieu = "CREATE TABLE chitietchitieutable " +
            "(id integer primary key autoincrement," +
            " tenchitieu text," +
            " tienchitieu double," +
            " ngaychitieu text," +
            " foreign key (tenchitieu) references tongchitable (tenchitieu));";

    public DatabaseDAO(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sumThuTable);
        sqLiteDatabase.execSQL(khoanThuTable);
        sqLiteDatabase.execSQL(bangTongChiTieu);
        sqLiteDatabase.execSQL(bangChiTietChiTieu);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + KHOANTHU_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SUMKT_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "tongchitable");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "chitietchitieutable");

        onCreate(sqLiteDatabase);
    }

    /**
     *
     * TỔNG HỢP CÁC METHOD CỦA KHOẢN CHI
     *
     * */

    public boolean addLoaiChi(String name){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenchitieu", name);
        values.put("tongchitieu", 0);
        long result = sqLiteDatabase.insert("tongchitable",
                null, values);
        sqLiteDatabase.close();
        if (result < 1){
            return false;
        }else {
            return true;
        }
    }

    //Get Loai Chi to RecyclerView in Fragment Loai Chi
    public List<mSumLoaiThu> getNameLoaiChi(){
        List<mSumLoaiThu> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String sql = "SELECT * FROM tongchitable";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            do {
                mSumLoaiThu mSumLoaiThu = new mSumLoaiThu();
                mSumLoaiThu.setNameLThu(cursor.getString(1));
                mSumLoaiThu.setSumAmountLThu(cursor.getDouble(2));
                list.add(mSumLoaiThu);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    public List<String> getNameChiTieuForSpinInDialog(){
        List<String> list = new ArrayList<>();
        String sql = "SELECT tenchitieu FROM tongchitable";
        sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()){
            do {
                String nameLT = cursor.getString(0);
                list.add(nameLT);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    public boolean addKhoanChi(String name, Double amount, String date){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenchitieu", name);
        values.put("tienchitieu", amount);
        values.put("ngaychitieu", date);

        long result = sqLiteDatabase.insert("chitietchitieutable", null, values);
        sqLiteDatabase.close();
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    public List<mSumLoaiThu> getAllLoaiChi(){
        List<mSumLoaiThu> list = new ArrayList<>();
        String sql = "SELECT * FROM tongchitable";
        sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()){
            do {
                mSumLoaiThu mSumLoaiThu = new mSumLoaiThu();
                mSumLoaiThu.setId(Integer.parseInt(cursor.getString(0)));
                mSumLoaiThu.setNameLThu(cursor.getString(1));
                mSumLoaiThu.setSumAmountLThu(Double.parseDouble(cursor.getString(2)));
                list.add(mSumLoaiThu);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    public int updateRecyclerViewKC(mSumLoaiThu mSumLoaiThu){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String sql = "SELECT tenchitieu, sum(tienchitieu) FROM chitietchitieutable WHERE tenchitieu = " +"'"+mSumLoaiThu.getNameLThu()+"'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        mSumLoaiThu.setNameLThu(cursor.getString(0));
        mSumLoaiThu.setSumAmountLThu(cursor.getDouble(1));

        Log.e(TAG,mSumLoaiThu.getNameLThu()+"=="+mSumLoaiThu.getSumAmountLThu());

        ContentValues values = new ContentValues();
        values.put("tenchitieu", mSumLoaiThu.getNameLThu());
        values.put("tongchitieu", mSumLoaiThu.getSumAmountLThu());
        int result = sqLiteDatabase.update("tongchitable", values,
                ID+ " =?",
                new String[]{String.valueOf(mSumLoaiThu.getId())});
        sqLiteDatabase.close();
        return result;
    }

    public List<mMoney> getKThuByNameKC(String nameKT){
        SQLiteDatabase db = this.getReadableDatabase();
        List<mMoney> list = new ArrayList<>();
        String sql = "SELECT * FROM chitietchitieutable WHERE tenchitieu LIKE " +"'"+nameKT+"'";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            mMoney m = new mMoney();
            m.setName(cursor.getString(1));
            m.setMoney(Double.parseDouble(cursor.getString(2)));
            m.setDate(cursor.getString(3));
            list.add(m);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<mMoney> getAllKhoanChiForLV(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<mMoney> list = new ArrayList<>();
        String sql = "SELECT * FROM chitietchitieutable";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            mMoney m = new mMoney();
            m.setName(cursor.getString(1));
            m.setMoney(Double.parseDouble(cursor.getString(2)));
            m.setDate(cursor.getString(3));
            list.add(m);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }


    public int delLoaiChi(mSumLoaiThu c){
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.example.assignment_pd03241/databases/quanlythuchi.db",null);
        }catch (SQLiteException e){
            Log.e(TAG,e+"============");
        }

        int sql = db.delete("tongchitable",
                ID+ "=?",
                new String[]{String.valueOf(c.getId())});

        int sql1 = db.delete("chitietchitieutable",
                "tenchitieu "+"=?",
                new String[]{c.getNameLThu()});
        db.close();
        if (sql == 0 && sql1 == 0){
            return -1;
        }else {
            return 1;
        }
    }



    public int editLoaiChi(mSumLoaiThu mSumLoaiThu, String newName){
        int a, b = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenchitieu", newName);

        a = db.update("tongchitable", values,
                "id =?",
                new String[]{String.valueOf(mSumLoaiThu.getId())});
        b = db.update("chitietchitieutable", values,
                "tenchitieu =?", new String[]{mSumLoaiThu.getNameLThu()});

        return a+b;
    }

    public int DeleteDataKC(int i){
        int a = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            int sql = db.delete("chitietchitieutable",
                    "id =?",
                    new String[]{String.valueOf(i)});
            if (sql == 0){
                a = -1;
                return a;
            }else {
                a = 1;
            }
        }catch (NullPointerException e){
            Log.e(TAG,"==="+e);
        }
        return a;
    }

    //=====================================//

    /**
     *
     * TỔNG HỢP CÁC METHOD CỦA KHOẢN THU
     *
     * */

    //Add name of collection in Loai Thu Dialog
    public boolean addLoaiThu(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_T, name);
        values.put(SUM_AMOUNT, 0);

        long result = db.insert(SUMKT_TABLE, null, values);
        db.close();
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    public List<String> getNameTOC(){
        List<String> list = new ArrayList<>();
        String sql = "SELECT " +NAME_T+ " FROM " +SUMKT_TABLE;
        sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()){
            do {
                String nameLT = cursor.getString(0);
                list.add(nameLT);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }


    public List<mSumLoaiThu> getNameTOC2(){
        List<mSumLoaiThu> list = new ArrayList<>();
        String sql = "SELECT nameT, sumAmountThu FROM " +SUMKT_TABLE;
        sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()){
            do {
                mSumLoaiThu mSumLoaiThu = new mSumLoaiThu();
                mSumLoaiThu.setNameLThu(cursor.getString(0));
                mSumLoaiThu.setSumAmountLThu(cursor.getDouble(1));
                list.add(mSumLoaiThu);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    public List<mSumLoaiThu> getAllLoaiThu(){
        List<mSumLoaiThu> list = new ArrayList<>();
        String sql = "SELECT * FROM " +SUMKT_TABLE;
        sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()){
            do {
                mSumLoaiThu mSumLoaiThu = new mSumLoaiThu();
                mSumLoaiThu.setId(Integer.parseInt(cursor.getString(0)));
                mSumLoaiThu.setNameLThu(cursor.getString(1));
                mSumLoaiThu.setSumAmountLThu(Double.parseDouble(cursor.getString(2)));
                list.add(mSumLoaiThu);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }


    //Add incomes
    public boolean addKhoanThu(String name, Double amount, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_T, name);
        values.put(AMOUNT_T, amount);
        values.put(DATE_T, date);

        long result = db.insert(KHOANTHU_TABLE, null, values);
        db.close();
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    public int updateRecyclerView(mSumLoaiThu mSumLoaiThu){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String sql = "SELECT nameT, sum(amountT) FROM khoanthutable WHERE nameT = " +"'"+mSumLoaiThu.getNameLThu()+"'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        mSumLoaiThu.setNameLThu(cursor.getString(0));
        mSumLoaiThu.setSumAmountLThu(cursor.getDouble(1));

        Log.e(TAG,mSumLoaiThu.getNameLThu()+"=="+mSumLoaiThu.getSumAmountLThu());

        ContentValues values = new ContentValues();
        values.put(NAME_T, mSumLoaiThu.getNameLThu());
        values.put(SUM_AMOUNT, mSumLoaiThu.getSumAmountLThu());
        int result = sqLiteDatabase.update(SUMKT_TABLE, values,
                ID+ " =?",
                new String[]{String.valueOf(mSumLoaiThu.getId())});
        sqLiteDatabase.close();
        return result;
    }

    public List<mMoney> getAllKhoanThu(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<mMoney> list = new ArrayList<>();
        String sql = "SELECT * FROM khoanthutable";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            mMoney m = new mMoney();
            m.setName(cursor.getString(1));
            m.setMoney(Double.parseDouble(cursor.getString(2)));
            m.setDate(cursor.getString(3));
            list.add(m);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<mMoney> getKThuByNameKT(String nameKT){
        SQLiteDatabase db = this.getReadableDatabase();
        List<mMoney> list = new ArrayList<>();
        String sql = "SELECT nameT, amountT, dateT FROM khoanthutable WHERE nameT LIKE " +"'"+nameKT+"'";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            mMoney m = new mMoney();
            m.setName(cursor.getString(0));
            m.setMoney(Double.parseDouble(cursor.getString(1)));
            m.setDate(cursor.getString(2));
            list.add(m);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public int DeleteData(int i){
        int a = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            int sql = db.delete(KHOANTHU_TABLE,
                    ID+"=?",
                    new String[]{String.valueOf(i)});
            if (sql == 0){
                a = -1;
                return a;
            }else {
                a = 1;
            }
        }catch (NullPointerException e){
            Log.e(TAG,"==="+e);
        }
        return a;
    }

    @SuppressWarnings("deprecation")
    public int delLoaiThu(mSumLoaiThu c){
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.example.assignment_pd03241/databases/quanlythuchi.db",null);
        }catch (SQLiteException e){
            Log.e(TAG,e+"============");
        }

        int sql = db.delete(SUMKT_TABLE,
                ID+ "=?",
                new String[]{String.valueOf(c.getId())});

        int sql1 = db.delete(KHOANTHU_TABLE,
                NAME_T+"=?",
                new String[]{c.getNameLThu()});
        db.close();
        if (sql == 0 && sql1 == 0){
            return -1;
        }else {
            return 1;
        }
    }


    public int editLoaiThu(mSumLoaiThu mSumLoaiThu, String newName){
        int a, b = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_T, newName);

        a = db.update(SUMKT_TABLE, values,
                ID+ " =?",
                new String[]{String.valueOf(mSumLoaiThu.getId())});
        b = db.update(KHOANTHU_TABLE, values,
                NAME_T+ "=?", new String[]{mSumLoaiThu.getNameLThu()});

        return a+b;
    }


    //======================= Các method của Fragment Thống kê

    public List<mMoney> getDataFromKhoanThuTable(String date1, String date2){
        sqLiteDatabase = this.getWritableDatabase();
        List<mMoney> list = new ArrayList<>();

            String sql1 = "SELECT * FROM "+KHOANTHU_TABLE+" WHERE " +DATE_T+ " BETWEEN "+"'"+date1+"'"+" AND "+"'"+date2+"'";
            Cursor cursor1 = sqLiteDatabase.rawQuery(sql1, null);
            cursor1.moveToFirst();
            while (!cursor1.isAfterLast()){
                mMoney m = new mMoney();
                m.setName(cursor1.getString(1));
                m.setMoney(Double.parseDouble(cursor1.getString(2)));
                m.setDate(cursor1.getString(3));
                list.add(m);
                cursor1.moveToNext();
            }
            cursor1.close();
            return list;
    }

    public List<mMoney> getDataFromKhoanChiTable(String date1, String date2){
        sqLiteDatabase = this.getWritableDatabase();
        List<mMoney> list = new ArrayList<>();
        String sql = "SELECT tenchitieu, tienchitieu, ngaychitieu FROM chitietchitieutable WHERE ngaychitieu BETWEEN "+"'"+date1+"'"+" AND "+"'"+date2+"'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            mMoney m = new mMoney();
            m.setName(cursor.getString(0));
            m.setMoney(Double.parseDouble(cursor.getString(1)));
            m.setDate(cursor.getString(2));
            list.add(m);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<mMoney> getAllDataToThongKe3(String date1, String date2){
        sqLiteDatabase = this.getWritableDatabase();
        List<mMoney> list = new ArrayList<>();

        String sql1 = "SELECT * FROM "+KHOANTHU_TABLE+" WHERE " +DATE_T+ " BETWEEN "+"'"+date1+"'"+" AND "+"'"+date2+"'";
        String sql2 = "SELECT tenchitieu, tienchitieu, ngaychitieu FROM chitietchitieutable WHERE ngaychitieu BETWEEN "+"'"+date1+"'"+" AND "+"'"+date2+"'";

        Cursor cursor1 = sqLiteDatabase.rawQuery(sql1, null);

        cursor1.moveToFirst();
        while (!cursor1.isAfterLast()){
            mMoney m = new mMoney();
            m.setName(cursor1.getString(1));
            m.setMoney(Double.parseDouble(cursor1.getString(2)));
            m.setDate(cursor1.getString(3));
            list.add(m);
            cursor1.moveToNext();
        }

        Cursor cursor2 = sqLiteDatabase.rawQuery(sql2, null);

        cursor2.moveToFirst();
        while (!cursor2.isAfterLast()){
            mMoney n = new mMoney();
            n.setName(cursor2.getString(0));
            n.setMoney(Double.parseDouble(cursor2.getString(1)));
            n.setDate(cursor2.getString(2));
            list.add(n);
            cursor2.moveToNext();
        }
        return list;
    }

    public String CalCulateWithKhoanThu(String date1, String date2){
        sqLiteDatabase = this.getWritableDatabase();
        String thu;
        try{
            String sql1 = "SELECT sum(amountT) FROM "+KHOANTHU_TABLE+" WHERE " +DATE_T+ " BETWEEN "+"'"+date1+"'"+" AND "+"'"+date2+"'";
            Cursor cursor1 = sqLiteDatabase.rawQuery(sql1, null);
            cursor1.moveToFirst();
            thu = cursor1.getString(0);
        }catch (Exception e){
            thu = "0.00";
        }
        return thu;
    }

    public String CalCulateWithKhoanChi(String date1, String date2){
        sqLiteDatabase = this.getWritableDatabase();
        String chi;
        try{
            String sql2 = "SELECT sum(tienchitieu) FROM chitietchitieutable WHERE ngaychitieu BETWEEN "+"'"+date1+"'"+" AND "+"'"+date2+"'";
            Cursor cursor2 = sqLiteDatabase.rawQuery(sql2, null);
            cursor2.moveToFirst();
            chi = cursor2.getString(0);
        }catch (Exception e){
            chi = "0.00";
        }
        return chi;
    }

    public List<String> CalCulateWithAllData(String date1, String date2){
        sqLiteDatabase = this.getWritableDatabase();
        List<String> list = new ArrayList<>();
        double thu = 0, chi = 0;
        try {
            String sql1 = "SELECT sum(amountT) FROM "+KHOANTHU_TABLE+" WHERE " +DATE_T+ " BETWEEN "+"'"+date1+"'"+" AND "+"'"+date2+"'";
            Cursor cursor1 = sqLiteDatabase.rawQuery(sql1, null);
            cursor1.moveToFirst();
            list.add(cursor1.getString(0));
            thu = Double.parseDouble(cursor1.getString(0));
        }catch (NullPointerException e){
            list.add(0,"0.00");
        }

        try {
            String sql2 = "SELECT sum(tienchitieu) FROM chitietchitieutable WHERE ngaychitieu BETWEEN "+"'"+date1+"'"+" AND "+"'"+date2+"'";
            Cursor cursor2 = sqLiteDatabase.rawQuery(sql2, null);
            cursor2.moveToFirst();
            list.add(1, cursor2.getString(0));
            chi = Double.parseDouble(cursor2.getString(0));

        }catch (NullPointerException e){
            list.add(1,"0.00");
        }

        try{
            double calculate = thu - chi;
            list.add(2, String.valueOf(calculate));
        }catch (Exception e){
            list.add(2,"0.00");
        }

        Log.e(null, list.get(0)+"=="+list.get(1)+"--"+list.get(2));
        return list;
    }

    //================== Check Exist ===============//
    public boolean checkLoaiThu(String nameLT){
        sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT "+NAME_T+" FROM "+SUMKT_TABLE+" WHERE "+NAME_T+" LIKE "+"'"+nameLT+"'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        String check;
        try {
            check = cursor.getString(0);
            if (!check.isEmpty()){
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    public boolean checkLoaiChi(String nameLC){
        sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT tenchitieu FROM tongchitable WHERE tenchitieu LIKE "+"'"+nameLC+"'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        String check;
        try {
            check = cursor.getString(0);
            if (!check.isEmpty()){
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
}
