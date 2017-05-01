package com.skh.universitysay.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.skh.universitysay.R;
import com.skh.universitysay.adapter.SheQuAdapter;
import com.skh.universitysay.bean.MyUser;
import com.skh.universitysay.bean.Post;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by SKH on 2017/4/28 0028.
 * 我的帖子界面
 */

public class QuizActivity extends AppCompatActivity {

    @BindView(R.id.quiz_toolbar)
    Toolbar mQuizToolbar;
    @BindView(R.id.quiz_recycler)
    RecyclerView mQuizRecycler;
    @BindView(R.id.quiz_refresh)
    SwipeRefreshLayout mQuizRefresh;

    private SheQuAdapter mSheQuAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);

        mSheQuAdapter = new SheQuAdapter(this);
        initToolbar();
        initRecycler();
        initRefresh();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        autoRefresh();
//    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        autoRefresh();
    }

    private void initToolbar() {
        mQuizToolbar.setTitle("我的帖子");
        setSupportActionBar(mQuizToolbar);
        mQuizToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mQuizToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    private void initRecycler() {
        mLayoutManager = new LinearLayoutManager(QuizActivity.this, LinearLayoutManager.VERTICAL, false);
        mQuizRecycler.setLayoutManager(mLayoutManager);
        mQuizRecycler.setAdapter(mSheQuAdapter);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(QuizActivity.this, DividerItemDecoration.VERTICAL);
        mQuizRecycler.addItemDecoration(dividerItemDecoration);
    }

    private void autoRefresh() {
        mQuizRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                mQuizRefresh.setRefreshing(true);
                getMyQuiz();
            }
        }, 100);
    }

    private void initRefresh() {
        mQuizRefresh.setColorSchemeColors(Color.RED, Color.BLACK, Color.YELLOW);
        mQuizRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyQuiz();
            }
        });
    }

    /**
     *查询当前用户发表的所有帖子
     */
    private void getMyQuiz() {
        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        BmobQuery<Post> query = new BmobQuery<>();
        query.addWhereEqualTo("author",myUser);
        query.order("-createdAt");
        // 查询完post表后想把发布者的信息查询出来 要用内部查询
        query.include("author");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (mQuizRefresh != null && mQuizRefresh.isRefreshing()) {
                    mQuizRefresh.setRefreshing(false);
                }
                if (e == null) {
                    if (list != null && list.size() != 0) {
                        mSheQuAdapter.setPostData(list);
                        mSheQuAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(QuizActivity.this,
                                "还没帖子，赶紧发布吧!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(QuizActivity.this,
                            "查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
