package com.skh.universitysay.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.skh.universitysay.fragment.TabAndroidFragment;
import com.skh.universitysay.fragment.TabExpandFragment;
import com.skh.universitysay.fragment.TabIosFragment;
import com.skh.universitysay.fragment.TabWebFragment;

/**
 * Created by SKH on 2017/3/20 0020.
 * tablayout
 */

public class TabViewPager extends FragmentStatePagerAdapter{

    private int mNumOfTabs;

    public TabViewPager(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabAndroidFragment androidFragment = new TabAndroidFragment();
                return androidFragment;
            case 1:
                TabIosFragment iosFragment = new TabIosFragment();
                return iosFragment;
            case 2:
                TabWebFragment webFragment = new TabWebFragment();
                return webFragment;
            case 3:
                TabExpandFragment expandFragment = new TabExpandFragment();
                return expandFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
