package com.skh.universitysay.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.skh.universitysay.R;
import com.skh.universitysay.bean.MyUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by SKH on 2017/4/6 0006.
 * 密码修改界面
 */

public class UpdatePasswordActivity extends AppCompatActivity {

    @BindView(R.id.update_toolbar)
    Toolbar mUpdateToolbar;
    @BindView(R.id.update_new_password)
    EditText mUpdateNewPassword;
    @BindView(R.id.update_old_password)
    EditText mUpdateOldPassword;
    @BindView(R.id.update_compile)
    Button mUpdateCompile;

    private MyUser mMyUser;
    private String newPaw;
    private String oldPaw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        ButterKnife.bind(this);

        mMyUser = BmobUser.getCurrentUser(MyUser.class);
        initToolbar();
        updatePasswordClickEvent();
    }

    private void initToolbar() {
        //setSupportActionBar(mLoginToolbar);
        mUpdateToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mUpdateToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    private void updatePasswordClickEvent() {
        mUpdateCompile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPaw = mUpdateOldPassword.getText().toString().trim();
                newPaw = mUpdateNewPassword.getText().toString().trim();

                if (TextUtils.isEmpty(oldPaw)) {
                    Toast.makeText(UpdatePasswordActivity.this, "请输入旧密码!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(newPaw)) {
                    Toast.makeText(UpdatePasswordActivity.this, "请输入新密码!", Toast.LENGTH_SHORT).show();
                } else {
                    if (mMyUser != null) {
                        MyUser.updateCurrentUserPassword(oldPaw, newPaw, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    PersonSettingActivity.sSettingActivity.finishAfterTransition();
                                    Toast.makeText(UpdatePasswordActivity.this,
                                            "修改成功,请用新密码登录!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UpdatePasswordActivity.this,
                                            LoginActivity.class);
                                    startActivity(intent);
                                    finishAfterTransition();
                                } else {
                                    Toast.makeText(UpdatePasswordActivity.this,
                                            "修改失败,请重新检查!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
