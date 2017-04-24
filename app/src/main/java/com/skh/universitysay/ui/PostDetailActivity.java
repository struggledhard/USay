package com.skh.universitysay.ui;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.skh.universitysay.R;
import com.skh.universitysay.adapter.PostDetailPagerAdapter;
import com.skh.universitysay.adapter.PostGridAdapter;
import com.skh.universitysay.bean.Post;
import com.skh.universitysay.utils.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/4/10 0010.
 * 帖子详情
 * 可以显示评论和赞
 */

public class PostDetailActivity extends AppCompatActivity {

    @BindView(R.id.post_detail_toolbar)
    Toolbar mPostDetailToolbar;
    @BindView(R.id.post_detail_head)
    ImageView mPostDetailHead;
    @BindView(R.id.post_detail_user)
    TextView mPostDetailUser;
    @BindView(R.id.post_detail_date)
    TextView mPostDetailDate;
    @BindView(R.id.post_detail_content)
    TextView mPostDetailContent;
    @BindView(R.id.post_detail_image)
    GridView mPostDetailImage;
    @BindView(R.id.post_detail_tablayout)
    TabLayout mPostDetailTablayout;
    @BindView(R.id.post_detail_viewpager)
    ViewPager mPostDetailViewPager;

    private Post mPost;
    private PostDetailPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);

        mPost = (Post) getIntent().getSerializableExtra("post");

        initToolbar();
        initTabLayout();
        getPostData();
    }

    private void initTabLayout() {
        mPostDetailTablayout.addTab(mPostDetailTablayout.newTab().setText("评论"));
        mPostDetailTablayout.addTab(mPostDetailTablayout.newTab().setText("赞"));

        mAdapter = new PostDetailPagerAdapter(getSupportFragmentManager(),
                mPost, mPostDetailTablayout.getTabCount());
        mPostDetailViewPager.setAdapter(mAdapter);
//        mPostDetailTablayout.setupWithViewPager(mPostDetailViewPager);
        mPostDetailViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mPostDetailTablayout));
        mPostDetailTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPostDetailViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initToolbar() {
        mPostDetailToolbar.setTitle("详情");
        setSupportActionBar(mPostDetailToolbar);
        mPostDetailToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mPostDetailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    private void getPostData() {
        if (mPost != null) {
            if (mPost.getAuthor() != null) {
                mPostDetailUser.setText(mPost.getAuthor().getUserNickName());
            } else {
                mPostDetailUser.setText("佚名");
            }

            if (mPost.getAuthor() != null) {
                if (mPost.getAuthor().getHeadImgUrl() != null) {
                    Glide.with(this).load(mPost.getAuthor().getHeadImgUrl())
                            .asBitmap().centerCrop()
                            .into(new SimpleTarget<Bitmap>(116, 116) {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    RoundedBitmapDrawable bitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                                    bitmapDrawable.setCircular(true);
                                    mPostDetailHead.setImageDrawable(bitmapDrawable);
                                }
                            });
                } else {
                    mPostDetailHead.setImageResource(R.drawable.ic_islogin_circle_gray_24dp);
                }
            } else {
                mPostDetailHead.setImageResource(R.drawable.ic_islogin_circle_gray_24dp);
            }

            if (!TextUtils.isEmpty(mPost.getContent())) {
                mPostDetailContent.setVisibility(View.VISIBLE);
                mPostDetailContent.setText(mPost.getContent());
            } else {
                mPostDetailContent.setVisibility(View.GONE);
            }

            List<String> imageList = mPost.getImageUrlList();
            if (!imageList.isEmpty()) {
                mPostDetailImage.setVisibility(View.VISIBLE);
                mPostDetailImage.setAdapter(new PostGridAdapter(imageList, PostDetailActivity.this));
            } else {
                mPostDetailImage.setVisibility(View.GONE);
            }

            String date = mPost.getCreatedAt();
            String strDate = TimeUtil.getDescriptionTimeFromTimestamp(TimeUtil.stringToLong(date,
                    TimeUtil.FORMAT_DATE_TIME_SECOND));
            mPostDetailDate.setText(strDate);
        }
    }
}
