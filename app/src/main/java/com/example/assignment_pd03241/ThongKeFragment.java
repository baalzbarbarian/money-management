package com.example.assignment_pd03241;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment_pd03241.Database.DatabaseDAO;
import com.example.assignment_pd03241.MyArrayAdapter.ListViewSimpleArrayAdapter;
import com.example.assignment_pd03241.MyArrayAdapter.ThongKeAdapterListView;
import com.example.assignment_pd03241.Validate.CheckDate;
import com.example.assignment_pd03241.mDatabase.mMoney;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static android.hardware.camera2.params.RggbChannelVector.RED;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ThongKeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ThongKeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThongKeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    DatabaseDAO db;
    TextView txtTongThu, txtTongChi, txtTongThuChi;
    EditText edtDateLeft, edtDateRight;
    Button btnThongKe;
    Button btnDateLeft;
    Button btnDateRight;
    Spinner spnThongKe;
    ListView lvThongKe;
    List<mMoney> moneyList2;
    ThongKeAdapterListView listViewAdapter;
    View.OnClickListener ClickEvent = null;

    private OnFragmentInteractionListener mListener;

    public ThongKeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThongKeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThongKeFragment newInstance(String param1, String param2) {
        ThongKeFragment fragment = new ThongKeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_thong_ke, container, false);
        mapping();
        setSpinnerThongKe();
        clickEvent();
        return view;
    }

    //=========================================
    public void clickEvent(){
        ClickEvent = new View.OnClickListener() {
            @Override
            public void onClick(View viewControl) {
                switch (viewControl.getId()){
                    case R.id.btnPickerDateLeft:
                        DatePickerDialog(R.id.btnPickerDateLeft);
                        break;
                    case R.id.btnPickerDateRight:
                        DatePickerDialog(R.id.btnPickerDateRight);
                        break;
                    case R.id.btnThongKe:
                        checkDate(edtDateLeft.getText().toString(), edtDateRight.getText().toString());
                        setListView();
                        break;
                }
            }
        };
        btnDateLeft.setOnClickListener(ClickEvent);
        btnDateRight.setOnClickListener(ClickEvent);
        btnThongKe.setOnClickListener(ClickEvent);
    }

    //===============================================
    private void setListView(){
        String date1 = edtDateLeft.getText().toString();
        String date2 = edtDateRight.getText().toString();
        int positionSpinner = spnThongKe.getSelectedItemPosition();
        if (positionSpinner == 1){
            txtTongThu.setText("");
            txtTongChi.setText("");
            txtTongThuChi.setText("");
            db = new DatabaseDAO(getActivity());
            moneyList2 = db.getDataFromKhoanThuTable(date1, date2);
            listViewAdapter = new ThongKeAdapterListView(getActivity(), moneyList2);
            lvThongKe.setAdapter(listViewAdapter);
            sumWithKhoanThu(date1, date2);
            if (moneyList2.size() < 1){
                Toast.makeText(getActivity()," Dữ liệu không tồn tại", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(getActivity(),moneyList2.size()+" danh sách đã được tải", Toast.LENGTH_SHORT).show();
            }
        }else if (positionSpinner == 2){
            txtTongThu.setText("");
            txtTongThu.setText("");
            txtTongThuChi.setText("");
            db = new DatabaseDAO(getActivity());
            moneyList2 = db.getDataFromKhoanChiTable(date1, date2);
            listViewAdapter = new ThongKeAdapterListView(getActivity(), moneyList2);
            lvThongKe.setAdapter(listViewAdapter);
            sumWithKhoanChi(date1,date2);
            if (moneyList2.size() < 1){
                Toast.makeText(getActivity()," Dữ liệu không tồn tại", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(getActivity(),moneyList2.size()+" danh sách đã được tải", Toast.LENGTH_SHORT).show();
            }
        }else if (positionSpinner == 0){
            db = new DatabaseDAO(getActivity());
            txtTongThu.setText("");
            txtTongChi.setText("");
            txtTongThuChi.setText("");

            moneyList2 = db.getAllDataToThongKe3(date1, date2);
            listViewAdapter = new ThongKeAdapterListView(getActivity(), moneyList2);
            lvThongKe.setAdapter(listViewAdapter);
            sumWithAllData(date1, date2);
            if (moneyList2.size() < 1){
                Toast.makeText(getActivity()," Dữ liệu không tồn tại", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(getActivity(),moneyList2.size()+" danh sách đã được tải", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(),"Tải danh sách thất bại", Toast.LENGTH_SHORT).show();
        }

    }
    //==================================================//

    private void setSpinnerThongKe(){
        String[] spnList = {
                "Hiển thị tất cả",
                "Khoản Thu",
                "Khoản Chi"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_spinner_item, spnList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnThongKe.setAdapter(adapter);
    }



    private void DatePickerDialog(final int viewControl){

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

                        if (viewControl == R.id.btnPickerDateLeft){
                            edtDateLeft.setText(simpleDateFormat.format(calendar.getTime()));
                        }else {
                            edtDateRight.setText(simpleDateFormat.format(calendar.getTime()));
                        }
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    /*
    *  Ánh Xạ
    * *******/
    private void mapping(){
        edtDateLeft = view.findViewById(R.id.edtPickerDateLeft);
        edtDateRight = view.findViewById(R.id.edtPickerDateRight);
        btnThongKe = view.findViewById(R.id.btnThongKe);
        btnDateRight = view.findViewById(R.id.btnPickerDateRight);
        btnDateLeft = view.findViewById(R.id.btnPickerDateLeft);
        spnThongKe = view.findViewById(R.id.spnThongKe);
        lvThongKe = view.findViewById(R.id.lvThongKe);
        txtTongThu = view.findViewById(R.id.txtTongThu);
        txtTongChi = view.findViewById(R.id.txtTongChi);
        txtTongThuChi = view.findViewById(R.id.txtTongThuChi);
    }

    private void checkDate(String dateLeft, String dateRight){
        Boolean checkdateL = CheckDate.isValidDate(dateLeft);
        Boolean checkdateR = CheckDate.isValidDate(dateRight);
        if (!checkdateL){
            Toast.makeText(getActivity(), "Ngày tháng không hợp lệ", Toast.LENGTH_SHORT).show();
            edtDateLeft.getText().clear();
        }
        if (!checkdateR){
            Toast.makeText(getActivity(), "Ngày tháng không hợp lệ", Toast.LENGTH_SHORT).show();
            edtDateRight.getText().clear();
        }
    }

    private void sumWithAllData(String date1, String date2){
        db = new DatabaseDAO(getActivity());
        try{
            List<String> list = db.CalCulateWithAllData(date1, date2);
            txtTongThu.setText(list.get(0));
            txtTongChi.setText(list.get(1));
            txtTongThuChi.setText(list.get(2));
        }catch (Exception e){
            Log.e(null, e+"");
        }
    }

    private void sumWithKhoanThu(String date1, String date2){
        db = new DatabaseDAO(getActivity());
        try{
            String thu = db.CalCulateWithKhoanThu(date1, date2);
            txtTongThu.setText(thu);
        }catch (Exception e){
            Log.e(null, e+"");
        }
    }

    private void sumWithKhoanChi(String date1, String date2){
        db = new DatabaseDAO(getActivity());
        try {
            String chi = db.CalCulateWithKhoanChi(date1, date2);
            txtTongChi.setText(chi);
        }catch (Exception e){
            Log.e(null, e+"");
        }
    }

    // TODO: =======================================================
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
