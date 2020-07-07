package com.example.assignment_pd03241.FragmentAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.assignment_pd03241.KhoanThu.TKhoanThuFragment;
import com.example.assignment_pd03241.KhoanThu.TLoaiThuFragment;

public class ThuFragmentAdapter extends FragmentStatePagerAdapter {

    public ThuFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0: fragment = new TKhoanThuFragment(); break;
            case 1: fragment = new TLoaiThuFragment(); break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title = "";
        switch (position){
            case 0: title = "Khoản Thu"; break;
            case 1: title = "Loại Thu"; break;
        }

        return title;
    }
}
