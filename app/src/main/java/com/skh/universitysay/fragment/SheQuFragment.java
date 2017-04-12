package com.skh.universitysay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.skh.universitysay.bean.MyUser;
import com.skh.universitysay.ui.CreatePostActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.she_qu_fragment, container, false);
        ButterKnife.bind(this, view);

        mMyUser = BmobUser.getCurrentUser(MyUser.class);
        initToolbar();
        // 想让Fragment中的onCreateOptionsMenu生效必须先调用setHasOptionsMenu方法
        // 否则Toolbar没有菜单
        setHasOptionsMenu(true);
        return view;
    }

    private void initToolbar() {
        mShequToolbar.setTitle("帖子");
        mShequToolbar.setTitleTextAppearance(getContext(), R.style.ToolBarTextAppearance);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mShequToolbar);
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
