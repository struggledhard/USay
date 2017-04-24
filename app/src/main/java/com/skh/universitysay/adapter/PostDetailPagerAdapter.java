package com.skh.universitysay.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.skh.universitysay.bean.Post;
import com.skh.universitysay.fragment.PostDetailCommentFragment;
import com.skh.universitysay.fragment.PostDetailLikesFragment;

/**
 * Created by SKH on 2017/4/19 0019.
 * 帖子详情界面的评论和喜欢界面适配器
 */

public class PostDetailPagerAdapter extends FragmentStatePagerAdapter{

    private int mNumOfTabs;
    private Post mPost;

    public PostDetailPagerAdapter(FragmentManager fm, Post post, int numOfTabs) {
        super(fm);
        this.mPost = post;
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PostDetailCommentFragment.newInstance(mPost);
            case 1:
                return PostDetailLikesFragment.newInstance(mPost);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
