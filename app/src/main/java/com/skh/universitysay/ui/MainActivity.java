package com.skh.universitysay.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.skh.universitysay.R;
import com.skh.universitysay.fragment.FenLeiFragment;
import com.skh.universitysay.fragment.GeRenFragment;
import com.skh.universitysay.fragment.SheQuFragment;
import com.skh.universitysay.fragment.TouTiaoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_layout)
    FrameLayout mFrameLayout;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mNavigationView;

    Fragment mFragment;
    private long clickTime = 0;

    public static MainActivity sMainActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化BmobSDK
        Bmob.initialize(this, "ff4e56e6c767e4ddbb1fdc738095bc58");

        sMainActivity = this;

        ButterKnife.bind(this);
        init();
    }

    private void init() {
        final FragmentManager fragmentManager = getSupportFragmentManager();

        mFragment = new TouTiaoFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.main_layout, mFragment)
                .commit();

        mNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tou_tiao:
                        mFragment = new TouTiaoFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_layout, mFragment)
                                .commit();
                        break;
                    case R.id.fen_lei:
                        mFragment = new FenLeiFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_layout, mFragment)
                                .commit();
                        break;
                    case R.id.she_qu:
                        mFragment = new SheQuFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_layout, mFragment)
                                .commit();
                        break;
                    case R.id.ge_ren:
                        mFragment = new GeRenFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_layout, mFragment)
                                .commit();
                        break;
                    default:
                        mFragment = new TouTiaoFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_layout, mFragment)
                                .commit();
                        break;
                }
                return true;
            }
        });
    }

    // 点击两次退出程序
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }
}
