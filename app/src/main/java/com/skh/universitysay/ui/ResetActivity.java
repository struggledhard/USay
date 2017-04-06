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
import android.widget.ImageButton;
import android.widget.Toast;

import com.skh.universitysay.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by SKH on 2017/3/31 0031.
 * 忘记密码后重新设置密码
 */

public class ResetActivity extends AppCompatActivity {

    @BindView(R.id.reset_toolbar)
    Toolbar mResetToolbar;
    @BindView(R.id.reset_phone)
    EditText mResetPhone;
    @BindView(R.id.reset_confirm)
    EditText mResetConfirm;
    @BindView(R.id.confirm_send)
    Button mConfirmSend;
    @BindView(R.id.new_paw)
    EditText mNewPaw;
    @BindView(R.id.reset_onpassword)
    ImageButton mResetOnpassword;
    @BindView(R.id.confirm_compile)
    Button mConfirmCompile;

    private String mPhone;
    private String mPassword;
    private String mCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        ButterKnife.bind(this);

        initToolbar();
        initReset();
    }

    private void initToolbar() {
        //setSupportActionBar(mLoginToolbar);
        mResetToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mResetToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    private void initReset() {
        mConfirmSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhone = mResetPhone.getText().toString().trim();

                if (TextUtils.isEmpty(mPhone)) {
                    Toast.makeText(ResetActivity.this, "请输入手机号!", Toast.LENGTH_SHORT).show();
                } else {
                    // 发送验证码
                    BmobSMS.requestSMSCode(mPhone, "USay", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e == null) {
                                Toast.makeText(ResetActivity.this, "发送成功,请注意短信!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ResetActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        mConfirmCompile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPassword = mNewPaw.getText().toString().trim();
                mCode = mResetConfirm.getText().toString().trim();

                if (TextUtils.isEmpty(mCode)) {
                    Toast.makeText(ResetActivity.this, "请输入验证码!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mPassword)) {
                    Toast.makeText(ResetActivity.this, "请输入新密码!", Toast.LENGTH_SHORT).show();
                } else {
                    // 使用验证码
                    BmobUser.resetPasswordBySMSCode(mCode, mPassword, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(ResetActivity.this, "密码重置成功!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ResetActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finishAfterTransition();
                            } else {
                                Toast.makeText(ResetActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
