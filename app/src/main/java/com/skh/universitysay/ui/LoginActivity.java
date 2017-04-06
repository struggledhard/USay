package com.skh.universitysay.ui;

import android.app.ProgressDialog;
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
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by SKH on 2017/3/31 0031.
 * 登录界面
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_toolbar)
    Toolbar mLoginToolbar;
    @BindView(R.id.login_phone)
    EditText mLoginPhone;
    @BindView(R.id.login_password)
    EditText mLoginPassword;
    @BindView(R.id.btn_forget_password)
    Button mBtnForgetPassword;
    @BindView(R.id.btn_register_account)
    Button mBtnRegisterAccount;
    @BindView(R.id.btn_login)
    Button mBtnLogin;

    private MyUser mMyUser;
    // 登录信息
    private String mPhone;
    private String mPassword;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mMyUser = new MyUser();

        initToolbar();
        initProgressDialog();
        initLogin();
        btnOnClick();
    }

    private void initToolbar() {
        //setSupportActionBar(mLoginToolbar);
        mLoginToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mLoginToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("正在登录...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void btnOnClick() {
        mBtnRegisterAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
//                finishAfterTransition();
            }
        });
        mBtnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetActivity.class);
                startActivity(intent);
//                finishAfterTransition();
            }
        });
    }

    private void initLogin() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }

                mPhone = mLoginPhone.getText().toString().trim();
                mPassword = mLoginPassword.getText().toString().trim();

                if (TextUtils.isEmpty(mPhone)) {
                    Toast.makeText(LoginActivity.this, "请输入手机号!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mPassword)) {
                    Toast.makeText(LoginActivity.this, "请输入密码!", Toast.LENGTH_SHORT).show();
                } else {
                    BmobUser.loginByAccount(mPhone, mPassword, new LogInListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            if (myUser != null) {
                                MainActivity.sMainActivity.finishAfterTransition();
                                Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                                if (mProgressDialog.isShowing()) {
                                    mProgressDialog.cancel();
                                }
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finishAfterTransition();
                            } else {
                                Toast.makeText(LoginActivity.this, "登录失败!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
