package com.skh.universitysay.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.skh.universitysay.R;

/**
 * Created by SKH on 2017/4/10 0010.
 * 帖子详情
 * 可以显示评论和赞
 */

public class PostDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
    }
}
