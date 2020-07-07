package com.example.assignment_pd03241.KhoanChi;


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

import com.example.assignment_pd03241.FragmentAdapter.ChiFragmentAdapter;
import com.example.assignment_pd03241.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChiFragment extends Fragment {

    private ViewPager viewPagerChi;
    private TabLayout tabLayoutChi;
    private View view;

    public ChiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chi, container, false);

        addControl();

        View root = tabLayoutChi.getChildAt(0);
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
        viewPagerChi = view.findViewById(R.id.viewPagerChi);
        tabLayoutChi = view.findViewById(R.id.tabLayoutChi);

        FragmentManager fr = getFragmentManager();
        PagerAdapter adapter = new ChiFragmentAdapter(fr);
        viewPagerChi.setAdapter(adapter);
        tabLayoutChi.setupWithViewPager(viewPagerChi);
    }

}
