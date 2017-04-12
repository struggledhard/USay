package com.skh.universitysay.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.litao.android.lib.Utils.GridSpacingItemDecoration;
import com.litao.android.lib.entity.PhotoEntry;
import com.skh.universitysay.R;
import com.skh.universitysay.adapter.CreatePostAdapter;
import com.skh.universitysay.other.EventEntry;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/4/10 0010.
 * 创建帖子
 */

public class CreatePostActivity extends AppCompatActivity implements CreatePostAdapter.OnItmeClickListener{

    @BindView(R.id.create_post_toolbar)
    Toolbar mCreatePostToolbar;
    @BindView(R.id.create_post_content)
    EditText mCreatePostContent;
    @BindView(R.id.create_post_recycler)
    RecyclerView mCreatePostRecycler;

    private CreatePostAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initToolbar();
        initRecycler();
    }

    private void initToolbar() {
        mCreatePostToolbar.setTitle("创建帖子");
        setSupportActionBar(mCreatePostToolbar);
        mCreatePostToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mCreatePostToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
        mAdapter = new CreatePostAdapter(CreatePostActivity.this);
        mCreatePostRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        mCreatePostRecycler.setAdapter(mAdapter);
        mCreatePostRecycler.addItemDecoration(new GridSpacingItemDecoration(3, 4, true));
    }

    /**
    *点击发表事件
     */
    private void clickPostSend() {

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
                clickPostSend();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onItemClicked(int position) {
        if (position == mAdapter.getItemCount()-1) {
            startActivity(new Intent(CreatePostActivity.this, PhotosSelectActivity.class));
            EventBus.getDefault().postSticky(new EventEntry(mAdapter.getData(),EventEntry.SELECTED_PHOTOS_ID));
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void photosMessageEvent(EventEntry entries){
        if (entries.id == EventEntry.RECEIVED_PHOTOS_ID) {
            mAdapter.reloadList(entries.photos);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void photoMessageEvent(PhotoEntry entry){
        mAdapter.appendPhoto(entry);
    }
}
