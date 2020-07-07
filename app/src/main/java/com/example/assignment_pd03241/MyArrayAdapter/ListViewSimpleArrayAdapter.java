package com.example.assignment_pd03241.MyArrayAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.assignment_pd03241.R;
import com.example.assignment_pd03241.mDatabase.mMoney;
import com.example.assignment_pd03241.mDatabase.mSumLoaiThu;

import java.util.List;

public class ListViewSimpleArrayAdapter extends BaseAdapter {

    private Context context;
    private List<mMoney> list;

    public ListViewSimpleArrayAdapter(Context context, List<mMoney> object) {
        super();
        this.context = context;
        this.list = object;
    }


//    public ListViewSimpleArrayAdapter
//            (Context context, int resource, List<mMoney> objects) {
//        super(context, resource);
//
//        this.context = (Activity) context;
//        this.layout = resource;
//        this.list = objects;
//    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.listview_items_kt,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.txt1 = convertView.findViewById(R.id.textView1);
            viewHolder.txt2 = convertView.findViewById(R.id.textView2);
            viewHolder.txt3 = convertView.findViewById(R.id.textView3);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        mMoney m = list.get(position);
        viewHolder.txt1.setText(m.getName());
        viewHolder.txt2.setText(m.getMoney()+"");
        viewHolder.txt3.setText(m.getDate());

        return convertView;
    }

    private class ViewHolder {
        private TextView txt1;
        private TextView txt2;
        private TextView txt3;
    }
}
