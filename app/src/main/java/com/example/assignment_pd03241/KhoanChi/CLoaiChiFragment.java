package com.example.assignment_pd03241.KhoanChi;


import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.assignment_pd03241.Database.DatabaseDAO;
import com.example.assignment_pd03241.KhoanThu.TLoaiThuFragment;
import com.example.assignment_pd03241.R;
import com.example.assignment_pd03241.RecyclerView.ChiRecylerViewAdapter;
import com.example.assignment_pd03241.RecyclerView.ThuRecylerViewAdapter;
import com.example.assignment_pd03241.mDatabase.mSumLoaiThu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.IllegalFormatCodePointException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CLoaiChiFragment extends Fragment {
    RecyclerView recyclerView;
    DatabaseDAO db;
    View view;
    List<String> mLTnameList;
    List<mSumLoaiThu> nameListRyCyclerView;
    EditText edtLoaiThu;
    Button btnAdd, btnCancel;
    ImageView imgSynData;

    public CLoaiChiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cloai_chi, container, false);

        setRecyclerView();

        FloatingActionButton fab = view.findViewById(R.id.fabKhoanThu);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater1 = CLoaiChiFragment.this.getLayoutInflater();
                View view1 = inflater1.inflate(R.layout.frament_dialog_lc, null);
                builder.setView(view1);
                final AlertDialog dialog = builder.create();
                dialog.show();

                TextInputLayout textName = view1.findViewById(R.id.textLayoutDialogEdtLT);

                edtLoaiThu = view1.findViewById(R.id.edtDialogNameLoaiThu);

                btnAdd = view1.findViewById(R.id.btnDialogAddLT);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nameLT = edtLoaiThu.getText().toString();

                        if (!nameLT.isEmpty()){
                            DatabaseDAO db = new DatabaseDAO(getActivity());
                            if (db.checkLoaiChi(nameLT)){
                                Toast.makeText(getActivity(),"Loại chi đã tồn tại. Vui lòng chọn tên khác", Toast.LENGTH_SHORT).show();
                            }else {
                                db.addLoaiChi(nameLT);
                                refreshList();
                            }
                            edtLoaiThu.getText().clear();
                        }

                    }
                });
                btnCancel = view1.findViewById(R.id.btnDialogCancelLT);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        //Set animation for img SynC
        imgSynData = view.findViewById(R.id.btnSynData);
        Animation animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.rotate);
        imgSynData.startAnimation(animation);
        view.findViewById(R.id.btnSynData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshList();
                Toast.makeText(getActivity(),
                        "Làm mới thành công",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void refreshList(){
        RecyclerView recyclerView = view.findViewById(R.id.recylerviewLT);
        DatabaseDAO db = new DatabaseDAO(getActivity());
        List<mSumLoaiThu> mLTnameList = db.getNameLoaiChi();

        ChiRecylerViewAdapter thuRecylerViewAdapter =
                new ChiRecylerViewAdapter(mLTnameList, getActivity());

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getActivity());

        recyclerView.setAdapter(thuRecylerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setRecyclerView(){
        recyclerView = view.findViewById(R.id.recylerviewLT);
        db = new DatabaseDAO(getActivity());
        nameListRyCyclerView = db.getNameLoaiChi();
        ChiRecylerViewAdapter adapter = new ChiRecylerViewAdapter(nameListRyCyclerView, getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(
                        getActivity(),
                        ((LinearLayoutManager)
                                layoutManager).getOrientation()
                );
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

    }
}
