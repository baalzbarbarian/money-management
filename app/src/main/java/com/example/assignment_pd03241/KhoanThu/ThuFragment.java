package com.example.assignment_pd03241.KhoanThu;


import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.assignment_pd03241.FragmentAdapter.ThuFragmentAdapter;
import com.example.assignment_pd03241.R;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThuFragment extends Fragment {

    private ViewPager viewPagerThu;
    private TabLayout tabLayoutThu;
    private View view;



    public ThuFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_thu, container, false);

        addControl();

        //Edit line between fragments
        View root = tabLayoutThu.getChildAt(0);
        if (root instanceof LinearLayout){
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.tabColor));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }


        return view;
    }

    private void addControl(){
        viewPagerThu = view.findViewById(R.id.viewPagerThu);
        tabLayoutThu = view.findViewById(R.id.tabLayoutThu);

        FragmentManager fr = getFragmentManager();
        PagerAdapter adapter = new ThuFragmentAdapter(fr);
        viewPagerThu.setAdapter(adapter);
        tabLayoutThu.setupWithViewPager(viewPagerThu);
    }

}
