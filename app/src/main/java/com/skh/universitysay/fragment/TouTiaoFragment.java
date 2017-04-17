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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skh.universitysay.R;
import com.skh.universitysay.adapter.NewsItemsAdapter;
import com.skh.universitysay.bean.NewsItems;
import com.skh.universitysay.ui.WebViewXianDuActivity;
import com.skh.universitysay.utils.HttpUtils;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.skh.universitysay.R.id.tou_recycler_view;

/**
 * Created by SKH on 2017/3/10 0010.
 * 头条页的Fragment
 */

public class TouTiaoFragment extends Fragment {

    @BindView(R.id.tou_tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.tou_swipe_refresh)
    SwipeRefreshLayout touSwipeRefresh;
    @BindView(tou_recycler_view)
    RecyclerView touRecyclerView;

    private LinearLayoutManager mLinearLayoutManager;
    private List<NewsItems> mNewsItemsList;
    private List<NewsItems> mItemsList;
    private HttpUtils mHttpUtils;
    private NewsItemsAdapter mNewsAdapter;

    private int page = 1;
//    boolean isLoadMore = false;
    private int lastVisibleItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tou_tiao_fragment, container, false);
        ButterKnife.bind(this, view);

        mHttpUtils = new HttpUtils();
        mNewsAdapter = new NewsItemsAdapter(getActivity());
        mNewsAdapter.notifyDataSetChanged();

        init();
        initSwipeRefresh();
        initLoadMore();

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

    // 首次进入自动刷新
    private void autoRefresh() {
        touSwipeRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                touSwipeRefresh.setRefreshing(true);
                pullData();

            }
        }, 100);
    }

    private void init() {
        mLinearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        touRecyclerView.setLayoutManager(mLinearLayoutManager);
        touRecyclerView.setAdapter(mNewsAdapter);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        touRecyclerView.addItemDecoration(dividerItemDecoration);

        mToolbar.setTitle(R.string.toolbar_title);
        mToolbar.setTitleTextAppearance(getContext(), R.style.ToolBarTextAppearance);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
    }

    private void initSwipeRefresh() {
        touSwipeRefresh.setColorSchemeColors(Color.BLACK, Color.RED, Color.YELLOW);
        touSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullData();
                Toast.makeText(getActivity(), "数据刷新", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pullData() {
        page = 1;
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String response = mHttpUtils.pageUrlContent(page);
                e.onNext(response);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // 创建的Observer中多了一个回调方法onSubscribe
                        // 传递参数为Disposable,Disposable 相当于RxJava1.x中的Subscription,
                        // 用于解除订阅
                        Log.d("TAG", "onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<NewsItems>>() {}.getType();
                        mNewsItemsList = gson.fromJson(s, type);
                        mNewsAdapter.setNewsItemsList(mNewsItemsList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "onComplete");
                        touSwipeRefresh.setRefreshing(false);
                    }
                });
    }

    private void initLoadMore() {
        touRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                int lastVisibleItem = ((LinearLayoutManager) touRecyclerView.getLayoutManager())
//                        .findLastVisibleItemPosition();
//                int totalItemCount = touRecyclerView.getLayoutManager().getItemCount();
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    if (!isLoadMore && lastVisibleItem >= (totalItemCount - 2)) {
//                        isLoadMore = true;
//                        pullMoreData();
//                    }
//                }
                int totalItemCount = touRecyclerView.getLayoutManager().getItemCount();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastVisibleItem >= (totalItemCount - 1)) {
                        pullMoreData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                int lastVisibleItem = ((LinearLayoutManager) touRecyclerView.getLayoutManager())
//                        .findLastVisibleItemPosition();
//                int totalItemCount = touRecyclerView.getLayoutManager().getItemCount();
//                if (lastVisibleItem >= (totalItemCount - 2) && dy > 0) {
//                    //还剩2个Item时加载更多
//                    if (isLoadMore) {
//                        isLoadMore = false;
//                        pullMoreData();
//                    }
//                }
                lastVisibleItem = ((LinearLayoutManager) touRecyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();
            }
        });
    }

    private void pullMoreData() {
        ++page;
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String response = mHttpUtils.pageUrlContent(page);
                e.onNext(response);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // 创建的Observer中多了一个回调方法onSubscribe
                        // 传递参数为Disposable,Disposable 相当于RxJava1.x中的Subscription,
                        // 用于解除订阅
                        Log.d("TAG", "onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<NewsItems>>() {}.getType();
                        mItemsList = gson.fromJson(s, type);
                        mNewsAdapter.setMoreNewsItemsList(mItemsList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "onComplete");
                        Toast.makeText(getActivity(), "加载完成", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.item_toutiao_xiandu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.xiandu:
                Intent intent = new Intent(getActivity(), WebViewXianDuActivity.class);
                getActivity().startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }
}
