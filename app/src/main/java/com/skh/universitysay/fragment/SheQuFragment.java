package com.skh.universitysay.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.skh.universitysay.R;
import com.skh.universitysay.adapter.SheQuAdapter;
import com.skh.universitysay.bean.MyUser;
import com.skh.universitysay.bean.Post;
import com.skh.universitysay.ui.CreatePostActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by SKH on 2017/3/10 0010.
 * 社区页
 */

public class SheQuFragment extends Fragment {

    @BindView(R.id.shequ_toolbar)
    Toolbar mShequToolbar;
    @BindView(R.id.shequ_refresh)
    SwipeRefreshLayout mShequRefresh;
    @BindView(R.id.shequ_recycler)
    RecyclerView mShequRecycler;

    private MyUser mMyUser;
    private SheQuAdapter mSheQuAdapter;
    private LinearLayoutManager mLayoutManager;

    private static final int PAGE_SIZE = 10;
    private int currentPageIndex = 0;
    private int lastVisibleItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.she_qu_fragment, container, false);
        ButterKnife.bind(this, view);

        mMyUser = BmobUser.getCurrentUser(MyUser.class);
        mSheQuAdapter = new SheQuAdapter(getActivity());
        initToolbar();
        initRecycler();
        initRefresh();
        scrollMoreData();
        // 想让Fragment中的onCreateOptionsMenu生效必须先调用setHasOptionsMenu方法
        // 否则Toolbar没有菜单
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        autoRefresh();
    }

    private void initToolbar() {
        mShequToolbar.setTitle("帖子");
        mShequToolbar.setTitleTextAppearance(getContext(), R.style.ToolBarTextAppearance);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mShequToolbar);
    }

    private void initRecycler() {
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mShequRecycler.setLayoutManager(mLayoutManager);
        mShequRecycler.setAdapter(mSheQuAdapter);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mShequRecycler.addItemDecoration(dividerItemDecoration);
    }

    private void autoRefresh() {
        mShequRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                mShequRefresh.setRefreshing(true);
                loadPost();
            }
        }, 100);
    }

    private void initRefresh() {
        mShequRefresh.setColorSchemeColors(Color.RED, Color.BLACK, Color.YELLOW);
        mShequRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPost();
            }
        });
    }

    private void scrollMoreData() {
        mShequRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int totalItemCount = mShequRecycler.getLayoutManager().getItemCount();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastVisibleItem >= (totalItemCount - 1)) {
                        loadMorePost();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) mShequRecycler.getLayoutManager())
                        .findLastVisibleItemPosition();
            }
        });
    }

    /**
     * 分页加载数据
     * 从Bmob上加载数据
     */
    private void loadPost() {
        currentPageIndex = 0;
        BmobQuery<Post> query = new BmobQuery<>();
        query.order("-createdAt");
        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.setLimit(PAGE_SIZE);
        query.setSkip(PAGE_SIZE * currentPageIndex);
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (mShequRefresh != null && mShequRefresh.isRefreshing()) {
                    mShequRefresh.setRefreshing(false);
                }
                if (e == null) {
                    if (list != null && list.size() != 0) {
                        mSheQuAdapter.setPostData(list);
                        mSheQuAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "还没帖子，赶紧发布吧!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 分页加载数据
     * 从Bmob上加载更多数据
     */
    private void loadMorePost() {
        ++currentPageIndex;
        BmobQuery<Post> query = new BmobQuery<>();
        query.order("-createdAt");
        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.setLimit(PAGE_SIZE);
        query.setSkip(PAGE_SIZE * currentPageIndex);
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (mShequRefresh != null && mShequRefresh.isRefreshing()) {
                    mShequRefresh.setRefreshing(false);
                }
                if (e == null) {
                    if (list != null && list.size() != 0) {
                        mSheQuAdapter.setPostMoreData(list);
                        mSheQuAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "没有更多数据!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.item_shequ_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shequ_edit:
                if (mMyUser != null) {
                    Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "请先登录!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        return true;
    }
}
