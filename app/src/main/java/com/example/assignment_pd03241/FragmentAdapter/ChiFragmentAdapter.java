package com.example.assignment_pd03241.FragmentAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.assignment_pd03241.KhoanChi.CKhoanChiFragment;
import com.example.assignment_pd03241.KhoanChi.CLoaiChiFragment;

public class ChiFragmentAdapter extends FragmentStatePagerAdapter {
    public ChiFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0: title = "Khoản Chi"; break;
            case 1: title = "Loại Chi"; break;
        }
        return title;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0: fragment = new CKhoanChiFragment(); break;
            case 1: fragment = new CLoaiChiFragment(); break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
