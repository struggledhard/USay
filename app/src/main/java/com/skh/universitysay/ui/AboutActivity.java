package com.skh.universitysay.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.skh.universitysay.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/4/26 0026.
 * 关于界面
 */

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.about_toolbar)
    Toolbar mAboutToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        initToolbar();
    }

    private void initToolbar() {
        mAboutToolbar.setTitle(R.string.guan_yu);
        setSupportActionBar(mAboutToolbar);
        mAboutToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mAboutToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
}
