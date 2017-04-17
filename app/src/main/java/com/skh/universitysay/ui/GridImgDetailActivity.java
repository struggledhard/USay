package com.skh.universitysay.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.skh.universitysay.R;
import com.skh.universitysay.adapter.GridImgDetailViewPagerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/4/12 0012.
 * 网格图片浏览
 */

public class GridImgDetailActivity extends AppCompatActivity {

    @BindView(R.id.gridimg_toolbar)
    Toolbar mGridimgToolbar;
    @BindView(R.id.gridimg_pager)
    ViewPager mGridimgPager;
    @BindView(R.id.indicator)
    TextView mIndicator;

    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";

    private GridImgDetailViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridimg_detail);
        ButterKnife.bind(this);

        init(savedInstanceState);
        initToolbar();
    }

    private void init(Bundle savedInstanceState) {
        int pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        List<String> urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);

        mAdapter = new GridImgDetailViewPagerAdapter(getSupportFragmentManager(), urls);
        mGridimgPager.setAdapter(mAdapter);

        CharSequence text = getString(R.string.viewpager_indicator,
                1, mGridimgPager.getAdapter().getCount());
        mIndicator.setText(text);
        // 更新下标
        mGridimgPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CharSequence text = getString(R.string.viewpager_indicator,
                        position + 1, mGridimgPager.getAdapter().getCount());
                mIndicator.setText(text);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }
        mGridimgPager.setCurrentItem(pagerPosition);
    }

    private void initToolbar() {
        mGridimgToolbar.setTitle("图片浏览");
        setSupportActionBar(mGridimgToolbar);
        mGridimgToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mGridimgToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mGridimgPager.getCurrentItem());
    }
}
