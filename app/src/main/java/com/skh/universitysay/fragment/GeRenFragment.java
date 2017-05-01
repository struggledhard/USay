package com.skh.universitysay.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.skh.universitysay.R;
import com.skh.universitysay.bean.MyUser;
import com.skh.universitysay.ui.AboutActivity;
import com.skh.universitysay.ui.CollectionActivity;
import com.skh.universitysay.ui.FeedBackActivity;
import com.skh.universitysay.ui.LoginActivity;
import com.skh.universitysay.ui.MainActivity;
import com.skh.universitysay.ui.PersonSettingActivity;
import com.skh.universitysay.ui.QuizActivity;
import com.skh.universitysay.ui.WebViewGeRenActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * Created by SKH on 2017/3/10 0010.
 * 个人中心页
 */

public class GeRenFragment extends Fragment {

    @BindView(R.id.has_login)
    RelativeLayout mHasLogin;
    @BindView(R.id.no_login)
    RelativeLayout mNoLogin;
    @BindView(R.id.geren_home)
    RelativeLayout mGeRenHome;
    @BindView(R.id.geren_game)
    RelativeLayout mGeRenGame;
    @BindView(R.id.geren_shopping)
    RelativeLayout mGeRenShopping;
    @BindView(R.id.geren_feedback)
    RelativeLayout mGeRenFeedback;
    @BindView(R.id.geren_about)
    RelativeLayout mGeRenAbout;
    @BindView(R.id.geren_collect)
    RelativeLayout mGeRenCollect;
    @BindView(R.id.geren_quiz)
    RelativeLayout mGeRenQuiz;
    @BindView(R.id.geren_item_ctrl)
    LinearLayout mItemCtrl;
    @BindView(R.id.exit_login)
    Button mExitLogin;
    @BindView(R.id.head_img)
    ImageView mHeadImg;
    @BindView(R.id.person_name)
    TextView mNickName;
    @BindView(R.id.person_desc)
    TextView mPersonDesc;

    //private boolean isLogin = false;
    private MyUser mMyUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ge_ren_fragment, container, false);
        ButterKnife.bind(this, view);

        mMyUser = BmobUser.getCurrentUser(MyUser.class);

        init();
        RlClickEvent();
        return view;
    }

    private void init() {
        if (mMyUser == null) {
            mHasLogin.setVisibility(View.GONE);
            mItemCtrl.setVisibility(View.INVISIBLE);
            mExitLogin.setVisibility(View.INVISIBLE);
            mNoLogin.setVisibility(View.VISIBLE);
            // 没有登录,进入登录界面
            mNoLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
//                    getActivity().finish();
                }
            });
        }
        if (mMyUser != null) {
            mHasLogin.setVisibility(View.VISIBLE);
            mItemCtrl.setVisibility(View.VISIBLE);
            mExitLogin.setVisibility(View.VISIBLE);
            mNoLogin.setVisibility(View.GONE);

            if (TextUtils.isEmpty(mMyUser.getHeadImgUrl())) {
                mHeadImg.setImageResource(R.drawable.ic_islogin_circle_gray_24dp);
            } else {
                Glide.with(getActivity())
                        .load(mMyUser.getHeadImgUrl())
                        .asBitmap()
                        .centerCrop()
                        .into(new SimpleTarget<Bitmap>(200, 200) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                RoundedBitmapDrawable bitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                bitmapDrawable.setCircular(true);
                                mHeadImg.setImageDrawable(bitmapDrawable);
                            }
                        });
            }

            if (TextUtils.isEmpty(mMyUser.getDescription())) {
                mPersonDesc.setText("填写你的简介!");
            } else {
                mPersonDesc.setText(mMyUser.getDescription());
            }
            mNickName.setText(mMyUser.getUserNickName());
            // 进入个人资料页面
            enterPersonSetting();
            // 点击退出登录
            initExitLogin();
        }
    }

    // 点击退出登录
    private void initExitLogin() {
        mExitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMyUser != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("提示:");
                    builder.setMessage("确定退出后将不能看见你的收藏和提问!");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getActivity(), "退出成功!", Toast.LENGTH_SHORT).show();
                            MyUser.logOut();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    // 点击进入个人资料界面
    private void enterPersonSetting() {
        mHasLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMyUser != null) {
                    Intent intent = new Intent(getActivity(), PersonSettingActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 六个RelativeLayout点击事件
     */
    private void RlClickEvent() {
        projectMainEvent();
        gameEvent();
        shoppingEvent();
        feedbackEvent();
        aboutEvent();
        myCollentionEvent();
        myQuizEvent();
    }

    /**
     * 项目主页点击事件
     */
    private void projectMainEvent() {
        mGeRenHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebViewGeRenActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
    }

    /**
    *游戏点击事件
     * Mob API
     */
    private void gameEvent() {
        mGeRenGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebViewGeRenActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });
    }

    /**
     * 购物点击事件
     */
    private void shoppingEvent() {
        mGeRenShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebViewGeRenActivity.class);
                intent.putExtra("type", 3);
                startActivity(intent);
            }
        });
    }

    /**
     * 问题反馈点击事件
     */
    private void feedbackEvent() {
        mGeRenFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FeedBackActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 关于点击事件
     */
    private void aboutEvent() {
        mGeRenAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 我的收藏点击事件
     */
    private void myCollentionEvent() {
        mGeRenCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CollectionActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 我的提问点击事件
     */
    private void myQuizEvent() {
        mGeRenQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QuizActivity.class);
                startActivity(intent);
            }
        });
    }
}
