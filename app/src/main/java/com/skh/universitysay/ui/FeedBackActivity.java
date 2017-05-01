package com.skh.universitysay.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.skh.universitysay.R;
import com.skh.universitysay.bean.FeedBack;
import com.skh.universitysay.bean.MyUser;
import com.skh.universitysay.utils.SystemUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by SKH on 2017/4/26 0026.
 * 反馈界面
 */

public class FeedBackActivity extends AppCompatActivity {

    @BindView(R.id.feedback_toolbar)
    Toolbar mFeedBackToolbar;
    @BindView(R.id.feedback_content)
    EditText mFeedBackContent;

    private MyUser mMyUser;
    private boolean isConnected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);

        initToolbar();
        mMyUser = BmobUser.getCurrentUser(MyUser.class);
        isConnected = SystemUtils.checkNetworkConnection(FeedBackActivity.this);
    }

    private void initToolbar() {
        mFeedBackToolbar.setTitle("反馈");
        setSupportActionBar(mFeedBackToolbar);
        mFeedBackToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mFeedBackToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    /**
     * 发送点击事件
     */
    private void clickFeedSend() {
        String mFeedBack = mFeedBackContent.getText().toString();
        if (isConnected) {
            if (mMyUser != null) {
                if (!TextUtils.isEmpty(mFeedBack)) {
                    FeedBack feedBack = new FeedBack();
                    feedBack.setUser(mMyUser);
                    feedBack.setFeedBack(mFeedBack);
                    feedBack.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(FeedBackActivity.this, "发表成功!",
                                        Toast.LENGTH_SHORT).show();
                                mFeedBackContent.setText("");
                            } else {
                                Toast.makeText(FeedBackActivity.this, "发表失败!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(FeedBackActivity.this, "请输入内容!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(FeedBackActivity.this, "请先登录!", Toast.LENGTH_SHORT).show();
            }
        } else {
            SystemUtils.noNetworkAlert(FeedBackActivity.this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_post_send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_post_send:
                clickFeedSend();
                break;
            default:
                break;
        }
        return true;
    }
}
