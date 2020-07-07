package com.example.assignment_pd03241.RecyclerView;


import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_pd03241.Database.DatabaseDAO;
import com.example.assignment_pd03241.R;
import com.example.assignment_pd03241.mDatabase.mSumLoaiThu;
import com.google.android.material.textfield.TextInputLayout;
import java.util.List;

public class ThuRecylerViewAdapter
        extends RecyclerView.Adapter<ThuRecylerViewAdapter.ViewHolder>
        implements View.OnClickListener{

    List<mSumLoaiThu> mSumLoaiThuList;
    Context context;
    View view;
    EditText edtTypeNewName;
    DatabaseDAO db;
    mSumLoaiThu m;
    private View mView;



    public ThuRecylerViewAdapter(List<mSumLoaiThu> mSumLoaiThuList, Context context) {
        this.mSumLoaiThuList = mSumLoaiThuList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.recyclerview_items, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.nameLT.setText(mSumLoaiThuList.get(position).getNameLThu());
        holder.imgEdit.setImageResource(R.drawable.ic_mode_edit_black_24dp);
        holder.imgDel.setImageResource(R.drawable.ic_delete_black_24dp);
        holder.sumLT.setText(mSumLoaiThuList.get(position).getSumAmountLThu()+"");

        //Xóa dữ liệu Loại Thu !!
        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Bạn muốn xóa "+holder.nameLT.getText()+" ?")
                        .setMessage("Xóa "+holder.nameLT.getText().toString()+" sẽ xóa tất cả Khoản Thu có cùng tên. Vẫn xóa?")
                        .setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                DatabaseDAO db = new DatabaseDAO(context);
                                mSumLoaiThuList = db.getAllLoaiThu();

                                try {
                                    mSumLoaiThu mSumLoaiThu = mSumLoaiThuList.get(position);
                                    int result = db.delLoaiThu(mSumLoaiThu);
                                    if (result > 0){
                                        notifyItemRemoved(position);
                                        notifyItemChanged(position);
                                        updateData(db.getAllLoaiThu());
                                        Toast.makeText(context,
                                                "Đã xóa. ",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(context,
                                                "Xóa thất bại",Toast.LENGTH_SHORT).show();
                                        Log.e(null,position+"");

                                    }
                                }catch (IndexOutOfBoundsException e){
                                    Toast.makeText(context,
                                            "Dữ liệu không tồn tại. \nVui lòng làm mới ứng dụng!!",Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).setNegativeButton("Bỏ qua", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                }).show();
            }
        });

        //Chỉnh sửa tên Loại Thu
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.dialog_custom_edit_khoanthu, null);
                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                dialog.show();

                TextInputLayout txtLayOut = dialogView.findViewById(R.id.textInputLayOut);
                TextView tvSetOldName = dialogView.findViewById(R.id.tvSetName);
                edtTypeNewName = dialogView.findViewById(R.id.edtTypeNewName);

                db = new DatabaseDAO(context);
                mSumLoaiThuList = db.getAllLoaiThu();

                try{
                    m = mSumLoaiThuList.get(position);
                    tvSetOldName.setText(m.getNameLThu());

                }catch (IndexOutOfBoundsException e){
                    Toast.makeText(context,
                            "Dữ liệu đã bị xóa hoặc không tồn tại. " +
                                    "\nVui lòng làm mới ứng dụng",Toast.LENGTH_LONG).show();
                }

                final Button btnEdit = dialogView.findViewById(R.id.dialog_edtLT_btnEdit);
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String edtNewName = edtTypeNewName.getText().toString();
                        if (db.checkLoaiThu(edtNewName)){
                            Toast.makeText(context, "Tên đã tồn tại. Chọn tên khác ngay còn kịp!", Toast.LENGTH_SHORT).show();
                            edtTypeNewName.getText().clear();
                        }else {
                            db.editLoaiThu(m, edtNewName);
                            mSumLoaiThuList = db.getAllLoaiThu();
                            holder.nameLT.setText(mSumLoaiThuList.get(position).getNameLThu());
                            notifyItemChanged(position);
                            Toast.makeText(context,
                                    "Dữ liệu đã thay lòng đổi dạ",Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }

                    }
                });
                Button btnCancle = dialogView.findViewById(R.id.dialog_edtLT_btnCancel);
                btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


            }
        });

    }

    void updateData(List<mSumLoaiThu> loaiThu) {
        if (loaiThu == null) {
            return;
        }
        mSumLoaiThuList.clear();
        mSumLoaiThuList.addAll(loaiThu);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mSumLoaiThuList.size();
    }


    @Override
    public void onClick(View view) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

         TextView nameLT;
         TextView sumLT;
         ImageView imgEdit;
         ImageView imgDel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            nameLT = mView.findViewById(R.id.tvLTShow);
            sumLT = mView.findViewById(R.id.tvLTSum);
            imgEdit = mView.findViewById(R.id.imgEdit);
            imgDel = mView.findViewById(R.id.imgDel);

        }




    }

}
