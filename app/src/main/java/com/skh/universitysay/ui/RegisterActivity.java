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
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by SKH on 2017/3/31 0031.
 * 注册界面
 */

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.register_phone)
    EditText mRegisterPhone;
    @BindView(R.id.register_user)
    EditText mRegisterUser;
    @BindView(R.id.register_password)
    EditText mRegisterPassword;
    @BindView(R.id.register_toolbar)
    Toolbar mRegisterToolbar;
    @BindView(R.id.btn_register)
    Button mBtnRegister;

    private MyUser mMyUser;
    // 注册信息
    private String mPhone;
    private String mNickUser;
    private String mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mMyUser = new MyUser();
        initToolbar();
        initRegister();
    }

    private void initToolbar() {
        //setSupportActionBar(mLoginToolbar);
        mRegisterToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mRegisterToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    private void initRegister() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhone = mRegisterPhone.getText().toString().trim();
                mNickUser = mRegisterUser.getText().toString().trim();
                mPassword = mRegisterPassword.getText().toString().trim();

                // 输入框的内容的简单校验
                if (TextUtils.isEmpty(mPhone)) {
                    Toast.makeText(RegisterActivity.this, "请输入手机号!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mNickUser)) {
                    Toast.makeText(RegisterActivity.this, "请输入用户名!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mPassword)) {
                    Toast.makeText(RegisterActivity.this, "请输入密码!", Toast.LENGTH_SHORT).show();
                } else {
                    mMyUser.setUserNickName(mNickUser);
                    mMyUser.setUsername(mPhone);
                    mMyUser.setMobilePhoneNumber(mPhone);
                    mMyUser.setPassword(mPassword);

                    mMyUser.signUp(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            if (e == null) {
                                Toast.makeText(RegisterActivity.this, "注册成功,快去登录!",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this,
                                        "注册失败!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
