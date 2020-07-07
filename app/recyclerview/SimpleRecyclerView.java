package com.example.assignment_pd03241.RecyclerView;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleRecyclerView extends RecyclerView {
    public SimpleRecyclerView(@NonNull Context context) {
        super(context);
    }

    public SimpleRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnItemAction(OnItemActionListener onItemAction){
        if (this.getAdapter() != null && this.getAdapter() instanceof RecylerViewAdapter){
            RecylerViewAdapter adapter = (RecylerViewAdapter) this.getAdapter();
            adapter.setHandler(onItemAction);
        }
    }
}
