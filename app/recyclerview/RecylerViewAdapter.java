package com.example.assignment_pd03241.RecyclerView;


import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_pd03241.Database.DatabaseDAO;
import com.example.assignment_pd03241.R;
import com.example.assignment_pd03241.mDatabase.mSumLoaiThu;

import java.util.List;

public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.ViewHolder> implements View.OnClickListener{

    DatabaseDAO db;
    List<mSumLoaiThu> mSumLoaiThuList;
    Context context;
    View view;

    private OnItemActionListener mOnItemActionListener;
    private View mView;

    public RecylerViewAdapter(List<mSumLoaiThu> mSumLoaiThuList, Context context) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        db = new DatabaseDAO(null);

        holder.setHandler(mOnItemActionListener);

        holder.nameLT.setText(mSumLoaiThuList.get(position).getNameLThu());
        holder.imgEdit.setImageResource(R.drawable.ic_mode_edit_black_24dp);
        holder.imgDel.setImageResource(R.drawable.ic_delete_black_24dp);
        holder.sumLT.setText(mSumLoaiThuList.get(position).getSumAmountLThu()+"");

    }

    @Override
    public int getItemCount() {
        return mSumLoaiThuList.size();
    }

    @Override
    public void onClick(View view) {

    }

    public void setHandler(OnItemActionListener onItemActionListener){
        mOnItemActionListener = onItemActionListener;
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

            imgDel.setOnTouchListener(new View.OnTouchListener() {

                private GestureDetector gestureDetector =
                        new GestureDetector(mView.getContext(),
                                new GestureDetector.SimpleOnGestureListener(){
                                    @Override
                                    public boolean onSingleTapConfirmed(MotionEvent e) {
                                        if (mOnItemActionListener != null){
                                            mOnItemActionListener.onClick(mView, getAdapterPosition());
                                        }
                                        return true;
                                    }
                                });

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    gestureDetector.onTouchEvent(motionEvent);
                    return true;
                }
            });

        }

        void setHandler(OnItemActionListener onItemActionListener){
            mOnItemActionListener = onItemActionListener;
        }

//        AlertDialog.Builder builder = new AlertDialog.Builder();
//                builder.setTitle("Xóa Loại Thu");
//                builder.setMessage("Bạn chắc chắn muốn xóa dữ liệu này ?")
//                        .setCancelable(false)
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                mSumLoaiThu m = mSumLoaiThuList.get(position);
//                db.delLoaiThu(m);
//            }
//        })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//        AlertDialog dialog = builder.create();
//                dialog.show();

    }
}
