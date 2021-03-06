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
import com.skh.universitysay.adapter.TabAndroidAdapter;
import com.skh.universitysay.bean.AndroidItemBean;
import com.skh.universitysay.bean.AndroidResultBean;
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
 *android资讯显示
 */

public class TabAndroidFragment extends Fragment {

    @BindView(R.id.tab_refresh)
    SwipeRefreshLayout mTabAndroidRefresh;
    @BindView(R.id.tab_recycler)
    RecyclerView mTabAndroidRecycler;

    private HttpUtils mHttpUtils;
    private TabAndroidAdapter mAndroidAdapter;
    private LinearLayoutManager mLayoutManager;
    private AndroidResultBean mResultBean;
    private List<AndroidItemBean> mAndroidItemBeanList;

    private int page = 1;
    private int lastVisibleItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.android_fragment, container, false);
        ButterKnife.bind(this, view);

        mHttpUtils = new HttpUtils();
        mAndroidAdapter = new TabAndroidAdapter(getActivity());
        initAndroidRecycler();
        initAndroidRefresh();
        loadMoreAndroidData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        autoRefresh();
    }

    // 首次进入自动刷新
    private void autoRefresh() {
//        mTabAndroidRefresh.measure(0, 0);
//        mTabAndroidRefresh.setRefreshing(true);
//        mTabAndroidRefresh.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (mTabAndroidRefresh.isRefreshing()) {
//                    getData();
//                    mTabAndroidRefresh.setRefreshing(false);
//                }
//            }
//        }, 3000);
        mTabAndroidRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTabAndroidRefresh.setRefreshing(true);
                getData();
            }
        }, 100);
    }

    private void initAndroidRecycler() {
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mTabAndroidRecycler.setLayoutManager(mLayoutManager);
        mTabAndroidRecycler.setAdapter(mAndroidAdapter);
    }

    private void initAndroidRefresh() {
        mTabAndroidRefresh.setColorSchemeColors(Color.RED, Color.BLACK, Color.YELLOW);
        mTabAndroidRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                Toast.makeText(getActivity(), "数据刷新", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMoreAndroidData() {
        mTabAndroidRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int totalItemCount = mTabAndroidRecycler.getLayoutManager().getItemCount();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastVisibleItem >= (totalItemCount - 1)) {
                        getMoreData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) mTabAndroidRecycler.getLayoutManager())
                        .findLastVisibleItemPosition();
            }
        });
    }

    private void getData() {
        page = 1;
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String result = mHttpUtils.getAndroidData(page);
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
                        AndroidResultBean androidResult = gson.fromJson(s, AndroidResultBean.class);
                        mAndroidItemBeanList = androidResult.getResults();
                        mAndroidAdapter.setAndroidItemBeen(mAndroidItemBeanList);
                        mAndroidAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mTabAndroidRefresh.setRefreshing(false);
                    }
                });
    }

    private void getMoreData() {
        ++page;
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String result = mHttpUtils.getAndroidData(page);
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
                        AndroidResultBean androidResult = gson.fromJson(s, AndroidResultBean.class);
                        mAndroidItemBeanList = androidResult.getResults();
                        mAndroidAdapter.setMoreAndroidItemBean(mAndroidItemBeanList);
                        mAndroidAdapter.notifyDataSetChanged();
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
}
