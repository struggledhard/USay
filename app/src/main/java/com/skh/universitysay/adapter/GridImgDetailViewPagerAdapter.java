package com.skh.universitysay.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.skh.universitysay.fragment.ImageDetailFragment;

import java.util.List;

/**
 * Created by SKH on 2017/4/13 0013.
 * 浏览图片适配器
 */

public class GridImgDetailViewPagerAdapter extends FragmentStatePagerAdapter{

    private List<String> fileList;

    public GridImgDetailViewPagerAdapter(FragmentManager fm, List<String> fileList) {
        super(fm);
        this.fileList = fileList;
    }

    @Override
    public Fragment getItem(int position) {
        String url = fileList.get(position);
        return ImageDetailFragment.newInstance(url);
    }

    @Override
    public int getCount() {
        if (fileList != null) {
            return fileList.size();
        }
        return 0;
    }
}
