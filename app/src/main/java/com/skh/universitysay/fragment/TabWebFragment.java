package com.skh.universitysay.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.skh.universitysay.R;
import com.skh.universitysay.adapter.TabWebAdapter;
import com.skh.universitysay.bean.WebItemBean;
import com.skh.universitysay.bean.WebResultBean;
import com.skh.universitysay.utils.HttpUtils;

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

/**
 * Created by SKH on 2017/3/20 0020.
 * web详情页
 */

public class TabWebFragment extends Fragment {

    @BindView(R.id.tab_web_refresh)
    SwipeRefreshLayout mTabWebRefresh;
    @BindView(R.id.tab_web_recycler)
    RecyclerView mTabWebRecycler;

    private HttpUtils mHttpUtils;
    private TabWebAdapter mTabWebAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<WebItemBean> mWebItemBeanList;

    private int page = 1;
    boolean isLoadingMore = false;

    //是否可见
    protected boolean isVisble;
    // 标志位，标志Fragment已经初始化完成。
    public boolean isPrepared = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisble = true;
            onVisible();
        } else {
            isVisble = false;
            onInVisible();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.web_fragment, container, false);
        ButterKnife.bind(this, view);

        isPrepared = true;
        mHttpUtils = new HttpUtils();
        mTabWebAdapter = new TabWebAdapter(getActivity());
        initWebRecycler();
        initWebRefresh();
        loadMoreWebData();
//        onVisible();
        autoRefresh();
        return view;
    }

    // 首次进入自动刷新
    private void autoRefresh() {
        mTabWebRefresh.measure(0, 0);
        mTabWebRefresh.setRefreshing(true);
        mTabWebRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mTabWebRefresh.isRefreshing()) {
                    onVisible();
                    mTabWebRefresh.setRefreshing(false);
                }
            }
        }, 3000);
    }

    private void initWebRecycler() {
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mTabWebRecycler.setLayoutManager(mLinearLayoutManager);
        mTabWebRecycler.setAdapter(mTabWebAdapter);
    }

    private void initWebRefresh() {
        mTabWebRefresh.setColorSchemeColors(Color.RED, Color.BLACK, Color.YELLOW);
        mTabWebRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                Toast.makeText(getActivity(), "数据刷新", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMoreWebData() {
        mTabWebRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItem = ((LinearLayoutManager) mTabWebRecycler.getLayoutManager())
                        .findLastVisibleItemPosition();
                int totalItemCount = mTabWebRecycler.getLayoutManager().getItemCount();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isLoadingMore && lastVisibleItem >= (totalItemCount - 2)) {
                        isLoadingMore = true;
                        getMoreData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) mTabWebRecycler.getLayoutManager())
                        .findLastVisibleItemPosition();
                int totalItemCount = mTabWebRecycler.getLayoutManager().getItemCount();
                if (lastVisibleItem >= (totalItemCount - 2) && dy > 0) {
                    //还剩2个Item时加载更多
                    if (isLoadingMore) {
                        isLoadingMore = false;
                        getMoreData();
                    }
                }
            }
        });
    }

    private void getData() {
        page = 1;
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String result = mHttpUtils.getWebData(page);
                e.onNext(result);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("TAG", "onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        Gson gson = new Gson();
                        WebResultBean webResultBean = gson.fromJson(s, WebResultBean.class);
                        mWebItemBeanList = webResultBean.getResults();
                        mTabWebAdapter.setWebItemBeen(mWebItemBeanList);
                        mTabWebAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mTabWebRefresh.setRefreshing(false);
                    }
                });
    }

    private void getMoreData() {
        ++page;
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String result = mHttpUtils.getWebData(page);
                e.onNext(result);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("TAG", "onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        Gson gson = new Gson();
                        WebResultBean webResultBean = gson.fromJson(s, WebResultBean.class);
                        mWebItemBeanList = webResultBean.getResults();
                        mTabWebAdapter.setMoreWebItemBean(mWebItemBeanList);
                        mTabWebAdapter.notifyDataSetChanged();
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

    private void onVisible() {
        if (isVisble && isPrepared) {
            getData();
        }
    }

    private void onInVisible() {

    }
}
