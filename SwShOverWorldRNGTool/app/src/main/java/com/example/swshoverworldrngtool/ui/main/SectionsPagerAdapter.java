package com.example.swshoverworldrngtool.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.swshoverworldrngtool.Main_Result;
import com.example.swshoverworldrngtool.Main_Search;
import com.example.swshoverworldrngtool.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // 指定されたページのフラグメントをインスタンス化するためにgetItemが呼び出し
        //return PlaceholderFragment.newInstance(position + 1);

        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Main_Search();
                break;
            case 1:
                fragment = new Main_Result();

        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}