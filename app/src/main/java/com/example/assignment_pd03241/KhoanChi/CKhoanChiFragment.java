package com.example.assignment_pd03241.KhoanChi;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.assignment_pd03241.Database.DatabaseDAO;
import com.example.assignment_pd03241.MyArrayAdapter.ListViewSimpleArrayAdapter;
import com.example.assignment_pd03241.R;
import com.example.assignment_pd03241.Validate.CheckDate;
import com.example.assignment_pd03241.mDatabase.mMoney;
import com.example.assignment_pd03241.mDatabase.mSumLoaiThu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CKhoanChiFragment extends Fragment {

    private View view, view1;
    Spinner spinnerDialog;
    Spinner spinnerSortForLv;
    List<String> listSpinner;
    List<String> listSpinnerForSortLv;
    private List<mSumLoaiThu> mSumList;
    private mSumLoaiThu mSum;
    DatabaseDAO db;
    ListView lvKhoanThu;
    ListViewSimpleArrayAdapter listViewAdapter;
    List<mMoney> mMoneyList;
    int pos;

    public CKhoanChiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ckhoan_chi, container, false);
        spinnerSortForLv = view.findViewById(R.id.spnKT);
        lvKhoanThu = view.findViewById(R.id.lvKhoanThu);
        FloatingActionButton fab = view.findViewById(R.id.fabKhoanChi);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                setFloatingButton();
                }
            });
        setSpinnerSortForListView();

        lvKhoanThu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                refreshSpinnerSort();
                pos = i;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Cảnh Báo !!!")
                        .setMessage("Bạn có muốn xóa khoản chi này ?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteItem(pos);

                            }
                        }).show();
            }
        });

        return view;
    }

    private void deleteItem(int i){
        db = new DatabaseDAO(getContext());
        mMoneyList = db.getAllKhoanChiForLV();
        mMoney m = mMoneyList.get(pos);
        int resutl = db.DeleteDataKC(m.getId());
        if (resutl > 0){
            setAllDataListViewOnKhoanThu();
            Toast.makeText(getActivity(),"DELETE SUCCESSFULLY",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(),"DELETE FAIL",Toast.LENGTH_SHORT).show();
        }
    }

    private void setAllDataListViewOnKhoanThu(){
        db = new DatabaseDAO(getActivity());
        mMoneyList = db.getAllKhoanChiForLV();
        listViewAdapter = new ListViewSimpleArrayAdapter(getActivity(),mMoneyList);
        lvKhoanThu.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
    }

    private void setDataListViewByName(String nameKT){
        db = new DatabaseDAO(getActivity());
        mMoneyList = db.getKThuByNameKC(nameKT);
        listViewAdapter = new ListViewSimpleArrayAdapter(getActivity(), mMoneyList);
        lvKhoanThu.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
    }

    private void refreshSpinnerSort(){
        db = new DatabaseDAO(getActivity());
        listSpinnerForSortLv = new ArrayList<>();
        listSpinnerForSortLv.add("Show All");

        SQLiteDatabase sqldb = db.getReadableDatabase();
        if (sqldb != null){
            String sql = "SELECT tenchitieu FROM tongchitable";
            Cursor cursor = sqldb.rawQuery(sql,null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false){
                String a = cursor.getString(0);
                listSpinnerForSortLv.add(a);
                cursor.moveToNext();
            }
            cursor.close();
            sqldb.close();
        }

        ArrayAdapter spnAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                listSpinnerForSortLv
        );
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortForLv.setAdapter(spnAdapter);
    }

    private void setSpinnerSortForListView(){
        db = new DatabaseDAO(getActivity());
        listSpinnerForSortLv = new ArrayList<>();
        listSpinnerForSortLv.add("Show All");

        SQLiteDatabase sqldb = db.getReadableDatabase();
        if (sqldb != null){
            String sql = "SELECT tenchitieu FROM tongchitable";
            Cursor cursor = sqldb.rawQuery(sql,null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false){
                String a = cursor.getString(0);
                listSpinnerForSortLv.add(a);
                cursor.moveToNext();
            }
            cursor.close();
            sqldb.close();
        }

        ArrayAdapter spnAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                listSpinnerForSortLv
        );
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortForLv.setAdapter(spnAdapter);

        spinnerSortForLv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){

                    setAllDataListViewOnKhoanThu();
                    spinnerSortForLv.setBackgroundColor(RED);
                }else {
                    String nameKT = spinnerSortForLv.getSelectedItem().toString();
                    setDataListViewByName(nameKT);
                    spinnerSortForLv.setBackgroundColor(WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinnerInDialog(){
        spinnerDialog = view1.findViewById(R.id.spinnerAddKT);
        db = new DatabaseDAO(getActivity());
        listSpinner = db.getNameChiTieuForSpinInDialog();
        ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, listSpinner);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDialog.setAdapter(spnAdapter);

    }

    private void setFloatingButton(){
        refreshSpinnerSort();
        //Set dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater1 = CKhoanChiFragment.this.getLayoutInflater();
        view1 = inflater1.inflate(R.layout.fragment_dialog_kc, null);
        builder.setView(view1);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //set spinner in dialog
        setSpinnerInDialog();

        TextInputLayout textAmount = view1.findViewById(R.id.textLayoutAmout);
        TextInputLayout textDate = view1.findViewById(R.id.textLayoutDate);
        final EditText edtAmount = view1.findViewById(R.id.edtDlgThuAmount);
        final EditText edtDate = view1.findViewById(R.id.edtDlgThuDate);
        Button btnPickerDate = view1.findViewById(R.id.btnDateDialogKhoanThu);
        Button btnAdd = view1.findViewById(R.id.btnDlgThuAdd);
        Button btnCancel = view1.findViewById(R.id.btnDlgThuCancel);

        //======================================================
        //Date Picker Button
        btnPickerDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(calendar.DATE);
                int month = calendar.get(calendar.MONTH);
                int year = calendar.get(calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                calendar.set(i, i1, i2);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                edtDate.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        //==============================================//



        //========== EVENT BUTTON ====================//
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new DatabaseDAO(getContext());
                String nameKT = null;
                double amountKT = 0;
                String dateKT = null;
                try {
                    nameKT = spinnerDialog.getSelectedItem().toString();
                    amountKT = Double.valueOf(edtAmount.getText().toString());
                    dateKT = edtDate.getText().toString();
                }catch (NullPointerException e){
                    Toast.makeText(getActivity(), "Các trường không được trống.", Toast.LENGTH_SHORT).show();
                }

                if (nameKT == null || dateKT == null){
                    Toast.makeText(getActivity(),"Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                }else {
                    Boolean checkDate = CheckDate.isValidDate(edtDate.getText().toString());
                    if (!checkDate){
                        Toast.makeText(getActivity(),"Ngày tháng không hợp lệ.", Toast.LENGTH_SHORT).show();
                        edtDate.getText().clear();
                    }else {
                        if (db.addKhoanChi(nameKT, amountKT, dateKT)) {
                            try {
                                int pos = spinnerDialog.getSelectedItemPosition();
                                mSumList = db.getAllLoaiChi();
                                mSum = mSumList.get(pos);
                                db.updateRecyclerViewKC(mSum);
                                setDataListViewByName(nameKT);
                                spinnerSortForLv.setSelection(pos + 1);
                                edtAmount.getText().clear();
                                edtDate.getText().clear();
                                Toast.makeText(getActivity(), "Thêm thành công.", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "Lỗi dữ liệu.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Làm mới ứng dụng", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

}
