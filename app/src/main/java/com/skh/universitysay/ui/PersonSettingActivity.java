package com.skh.universitysay.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.skh.universitysay.R;
import com.skh.universitysay.bean.MyUser;
import com.skh.universitysay.utils.FileUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by SKH on 2017/4/4 0004.
 * 个人中心,资料设置
 */

public class PersonSettingActivity extends AppCompatActivity {

    @BindView(R.id.person_setting_toolbar)
    Toolbar mSettingToolbar;

    @BindView(R.id.setting_head)
    RelativeLayout mSettingHead;
    @BindView(R.id.head_image)
    ImageView mHeadImage;

    @BindView(R.id.setting_name)
    RelativeLayout mSettingName;
    @BindView(R.id.user_name)
    TextView mUserName;

    @BindView(R.id.setting_phone)
    RelativeLayout mSettingPhone;
    @BindView(R.id.person_phone)
    TextView mPersonPhone;

    @BindView(R.id.setting_desc)
    RelativeLayout mSettingDesc;
    @BindView(R.id.jian_jie)
    TextView mJianJie;

    @BindView(R.id.password_reset)
    TextView mPasswordReset;

    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称

    private static final String TAG = "PersonSettingActivity";
    private static final int REQUEST_CODE_CAPTURE_IMAGE_10 = 10;
    private static final int REQUEST_CODE_ALBUM_11 = 11;
    private static final int REQUEST_CODE_CROP_12 = 12;

    // bmob中图片URL
    private String avatarBmobPathStr;

    public static PersonSettingActivity sSettingActivity = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_setting);
        ButterKnife.bind(this);

        sSettingActivity = this;

        initToolbar();
        fullUserData();
        eventDeals();
    }

    private void initToolbar() {
        mSettingToolbar.setTitle(R.string.person_info);
        setSupportActionBar(mSettingToolbar);
        mSettingToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mSettingToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    /**
     * 从Bmob云端下载数据,填充"个人资料"详情页面
     */
    private void fullUserData() {
        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        if (myUser != null) {
            mUserName.setText(myUser.getUserNickName());
            mPersonPhone.setText(myUser.getMobilePhoneNumber());

            if (TextUtils.isEmpty(myUser.getHeadImgUrl())) {
                mHeadImage.setImageResource(R.drawable.ic_islogin_circle_gray_24dp);
            } else {
                Glide.with(PersonSettingActivity.this)
                        .load(myUser.getHeadImgUrl())
                        .asBitmap()
                        .centerCrop()
                        .into(new SimpleTarget<Bitmap>(160, 160) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                RoundedBitmapDrawable bitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                bitmapDrawable.setCircular(true);
                                mHeadImage.setImageDrawable(bitmapDrawable);
                            }
                        });
            }

            if (TextUtils.isEmpty(myUser.getDescription())) {
                mJianJie.setText("未设置");
            } else {
                mJianJie.setText(myUser.getDescription());
            }
        }
    }

    // 事件处理
    private void eventDeals() {
        nameClickEvent();
        headClickEvent();
        descClickEvent();
        passwordClickEvent();
    }

    /**
     *  用户名修改
     */
    private void nameClickEvent () {
        mSettingName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(PersonSettingActivity.this);
                View mViewDialog = inflater.inflate(R.layout.setting_view_item, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonSettingActivity.this);
                builder.setView(mViewDialog);
                final EditText editText = (EditText) mViewDialog.findViewById(R.id.setting_dialog_view);
                builder.setTitle("用户名修改");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String string = editText.getText().toString().trim();
                        if (TextUtils.isEmpty(string)) {
                            Toast.makeText(PersonSettingActivity.this,
                                    "没有数据,请仔细检查!", Toast.LENGTH_SHORT).show();
                        } else {
                            MyUser myUser = new MyUser();
                            myUser.setUserNickName(string);
                            MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
                            myUser.update(currentUser.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        mUserName.setText(string);
                                        Toast.makeText(PersonSettingActivity.this,
                                                "修改成功!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PersonSettingActivity.this,
                                                "修改失败!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    /**
     * 点击“简介处理事件”
     */
    private void descClickEvent() {
        mSettingDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(PersonSettingActivity.this);
                View mViewDialog = inflater.inflate(R.layout.setting_view_item, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonSettingActivity.this);
                builder.setView(mViewDialog);
                final EditText editText = (EditText) mViewDialog.findViewById(R.id.setting_dialog_view);
                builder.setTitle("个性简介");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String string = editText.getText().toString().trim();
                        if (TextUtils.isEmpty(string)) {
                            Toast.makeText(PersonSettingActivity.this,
                                    "没有数据,请仔细检查!", Toast.LENGTH_SHORT).show();
                        } else {
                            MyUser myUser = new MyUser();
                            myUser.setDescription(string);
                            MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
                            myUser.update(currentUser.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        mJianJie.setText(string);
                                        Toast.makeText(PersonSettingActivity.this,
                                                "添加成功!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PersonSettingActivity.this,
                                                "添加失败!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    /**
    *点击头像处理事件
     */
    private void headClickEvent() {
        mSettingHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] select = {"从相册中选择", "拍照"};
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonSettingActivity.this);
                builder.setTitle("头像设置");
                builder.setItems(select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                selectAlbumEvents();
                                break;
                            case 1:
                                captureImageEvents();
                                break;
                            default:
                                break;
                        }
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
        });
    }

    /**
     * 获取相册服务
     */
    private void selectAlbumEvents() {
        Intent intentAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        intentAlbum.addCategory(Intent.CATEGORY_OPENABLE);
        intentAlbum.setType("image/*");
        intentAlbum.putExtra("return-data", true);
        startActivityForResult(intentAlbum, REQUEST_CODE_ALBUM_11);
    }

    /**
     * 获取相机服务
     */
    private void captureImageEvents() {
        Intent intentCaptureImage = new Intent("android.media.action.IMAGE_CAPTURE");
        intentCaptureImage.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                        IMAGE_FILE_NAME)));
        try {
            startActivityForResult(intentCaptureImage, REQUEST_CODE_CAPTURE_IMAGE_10);
        } catch (Exception e) {
            Toast.makeText(PersonSettingActivity.this, "无相机服务", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CAPTURE_IMAGE_10:
                    File temp = new File(Environment.getExternalStorageDirectory() + File.separator
                            + IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(temp));
                    break;
                case REQUEST_CODE_ALBUM_11:
                    try {
                        startPhotoZoom(data.getData());
                    } catch (NullPointerException e) {
                        Log.i(TAG, e.getMessage());
                    }
                    break;
                case REQUEST_CODE_CROP_12:   // 取得裁剪后的图片
                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");  // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("aspectX", 1);   // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);  // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CODE_CROP_12);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata intent
     */
    private void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            // 取得SDCard图片路径做显示
            Bitmap bmobAvatarBitmap = bundle.getParcelable("data");
            String avatarFilePathStr = FileUtil.saveFile(PersonSettingActivity.this,
                    "tempAvatar.jpg", bmobAvatarBitmap);
            final BmobFile bmobFile = new BmobFile(new File(avatarFilePathStr));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        avatarBmobPathStr = bmobFile.getFileUrl();

                        // 修改云端头像地址
                        MyUser myUser = new MyUser();
                        myUser.setHeadImgUrl(avatarBmobPathStr);
                        MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
                        myUser.update(currentUser.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e != null) {
                                    Toast.makeText(PersonSettingActivity.this, "Error! " +
                                                    e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(PersonSettingActivity.this, "头像上传失败, 请稍后再试!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
            if (myUser != null) {
                Glide.with(PersonSettingActivity.this)
                        .load(myUser.getHeadImgUrl())
                        .asBitmap()
                        .centerCrop()
                        .into(new SimpleTarget<Bitmap>(192, 192) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                RoundedBitmapDrawable bitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                bitmapDrawable.setCircular(true);
                                mHeadImage.setImageDrawable(bitmapDrawable);
                            }
                        });
            }
        }
    }

    /**
     * 跳转到密码修改界面
     */
    private void passwordClickEvent() {
        mPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonSettingActivity.this, UpdatePasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
