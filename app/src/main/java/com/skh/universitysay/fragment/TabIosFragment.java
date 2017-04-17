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
import com.skh.universitysay.adapter.TabIosAdapter;
import com.skh.universitysay.bean.IosItemBean;
import com.skh.universitysay.bean.IosResultBean;
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
 * IOS页面
 */

public class TabIosFragment extends Fragment {

    @BindView(R.id.tab_ios_refresh)
    SwipeRefreshLayout mTabIosRefresh;
    @BindView(R.id.tab_ios_recycler)
    RecyclerView mTabIosRecycler;

    private HttpUtils mHttpUtils;
    private TabIosAdapter mTabIosAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<IosItemBean> mIosItemBeanList;

    private int page = 1;
    private int lastVisibleItem;

    //是否可见
    protected boolean isVisble;
    // 标志位，标志Fragment已经初始化完成。
    public boolean isPrepared = false;

    // fragment从不可见到完全可见的时候，会调用该方法
    // Fragment生命周期中，setUserVisibleHint先于onCreateView执行。
    // 实例中，当TabFragment可见时，先进入onVisible方法，
    // 当判断各控件未初始化完毕，则进入onCreateView方法，
    // 当控件初始化完毕后，会再次调用onVisible。
    // 在onVisible中判断isPrepared和isVisible,只要有一个不为true就不往下执行。
    // 因此，只有初始化完成并且fragment可见情况下，才会加载数据，这样就避免了未初始化带来的问题。
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
        View view = inflater.inflate(R.layout.ios_fragment, container, false);
        ButterKnife.bind(this, view);

        isPrepared = true;
        mHttpUtils = new HttpUtils();
        mTabIosAdapter = new TabIosAdapter(getActivity());
        initIosRecycler();
        initIosRefresh();
        loadMoreIosData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        autoRefresh();
    }

    // 首次进入自动刷新
    private void autoRefresh() {
        mTabIosRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTabIosRefresh.setRefreshing(true);
                onVisible();

            }
        }, 100);
    }

    private void initIosRecycler() {
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mTabIosRecycler.setLayoutManager(mLinearLayoutManager);
        mTabIosRecycler.setAdapter(mTabIosAdapter);
    }

    private void initIosRefresh() {
        mTabIosRefresh.setColorSchemeColors(Color.RED, Color.BLACK, Color.YELLOW);
        mTabIosRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                Toast.makeText(getActivity(), "数据刷新", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMoreIosData() {
        mTabIosRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int totalItemCount = mTabIosRecycler.getLayoutManager().getItemCount();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastVisibleItem >= (totalItemCount - 1)) {
                        getMoreData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) mTabIosRecycler.getLayoutManager())
                        .findLastVisibleItemPosition();
            }
        });
    }

    private void getData() {
        page = 1;
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String result = mHttpUtils.getIosData(page);
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
                        IosResultBean iosResultBean = gson.fromJson(s, IosResultBean.class);
                        mIosItemBeanList = iosResultBean.getResults();
                        mTabIosAdapter.setIosItemBeen(mIosItemBeanList);
                        mTabIosAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mTabIosRefresh.setRefreshing(false);
                    }
                });
    }

    private void getMoreData() {
        ++page;
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String result = mHttpUtils.getIosData(page);
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
                        IosResultBean iosResultBean = gson.fromJson(s, IosResultBean.class);
                        mIosItemBeanList = iosResultBean.getResults();
                        mTabIosAdapter.setMoreIosItemBean(mIosItemBeanList);
                        mTabIosAdapter.notifyDataSetChanged();
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
        // 懒加载数据
        if (isVisble && isPrepared) {
            getData();
        }
    }

    private void onInVisible() {

    }
}
