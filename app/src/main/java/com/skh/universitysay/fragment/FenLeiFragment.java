package com.skh.universitysay.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skh.universitysay.R;
import com.skh.universitysay.adapter.TabViewPager;
import com.skh.universitysay.other.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/3/20 0020.
 * 分类浏览
 */

public class FenLeiFragment extends Fragment {

    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.banner_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.fen_lei_viewpager)
    ViewPager mViewPager;

    private TabViewPager mTabViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fen_lei_fragment, container, false);
        ButterKnife.bind(this, view);

        initBanner();
        initTabLayout();

        return view;
    }

    private void initTabLayout() {
        mTabLayout.addTab(mTabLayout.newTab().setText("Android"));
        mTabLayout.addTab(mTabLayout.newTab().setText("IOS"));
        mTabLayout.addTab(mTabLayout.newTab().setText("WEB"));
        mTabLayout.addTab(mTabLayout.newTab().setText("拓展资源"));

        mTabViewPager = new TabViewPager(getActivity().getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mTabViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initBanner() {
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.banner01);
        images.add(R.drawable.banner02);
        images.add(R.drawable.banner03);
        images.add(R.drawable.banner04);
        images.add(R.drawable.bnaner05);
        images.add(R.drawable.banner06);

        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        // 设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(images);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        //banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }
}
