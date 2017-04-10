package com.skh.universitysay.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skh.universitysay.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.she_qu_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
