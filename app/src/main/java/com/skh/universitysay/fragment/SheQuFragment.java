package com.skh.universitysay.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skh.universitysay.R;

/**
 * Created by SKH on 2017/3/10 0010.
 * 社区页
 */

public class SheQuFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.she_qu_fragment, container, false);
        return view;
    }
}
