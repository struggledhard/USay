package com.skh.universitysay.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.skh.universitysay.R;
import com.skh.universitysay.adapter.CollectionAdapter;
import com.skh.universitysay.bean.Favorite;
import com.skh.universitysay.db.FavoriteDB;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/5/1 0001.
 * 我的收藏
 */

public class CollectionActivity extends AppCompatActivity {

    @BindView(R.id.collection_toolbar)
    Toolbar mCollectionToolbar;
    @BindView(R.id.collection_recycler)
    RecyclerView mCollectionRecycler;

    private LinearLayoutManager mLayoutManager;
    private CollectionAdapter mAdapter;
    private List<Favorite> mFavorites;
    private FavoriteDB mFavoriteDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);

        mFavoriteDB = FavoriteDB.getInstance(this);
        mFavorites = mFavoriteDB.findFavorite();
        mAdapter = new CollectionAdapter(this, mFavorites);
        initToolbar();
        initCollectionRecycler();
    }

    private void initToolbar() {
        mCollectionToolbar.setTitle(R.string.shou_cang);
        setSupportActionBar(mCollectionToolbar);
        mCollectionToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mCollectionToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    private void initCollectionRecycler() {
        mLayoutManager = new LinearLayoutManager(CollectionActivity.this, LinearLayoutManager.VERTICAL, false);
        mCollectionRecycler.setLayoutManager(mLayoutManager);
        mCollectionRecycler.setAdapter(mAdapter);
    }
}
