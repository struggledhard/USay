package com.skh.universitysay.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.litao.android.lib.Utils.GridSpacingItemDecoration;
import com.litao.android.lib.entity.PhotoEntry;
import com.skh.universitysay.R;
import com.skh.universitysay.adapter.CreatePostAdapter;
import com.skh.universitysay.bean.MyUser;
import com.skh.universitysay.bean.Post;
import com.skh.universitysay.other.EventEntry;
import com.skh.universitysay.utils.CompressPhotoUtils;
import com.skh.universitysay.utils.FileUtil;
import com.skh.universitysay.utils.SystemUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

import static com.skh.universitysay.utils.CompressPhotoUtils.TEMP_IMAGES_UPLOAD_DIR;

/**
 * Created by SKH on 2017/4/10 0010.
 * 创建帖子
 */

public class CreatePostActivity extends AppCompatActivity implements CreatePostAdapter.OnItmeClickListener{

    @BindView(R.id.create_post_toolbar)
    Toolbar mCreatePostToolbar;
    @BindView(R.id.create_post_content)
    EditText mCreatePostContent;
    @BindView(R.id.create_post_recycler)
    RecyclerView mCreatePostRecycler;

    private CreatePostAdapter mAdapter;
    private List<String> urlsList = new ArrayList<>();
    private List<String> imagePathList;
    private boolean isConnected;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initToolbar();
        initRecycler();
        initProgressDialog();
    }

    private void initToolbar() {
        mCreatePostToolbar.setTitle("创建帖子");
        setSupportActionBar(mCreatePostToolbar);
        mCreatePostToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mCreatePostToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    private void initRecycler() {
        mAdapter = new CreatePostAdapter(CreatePostActivity.this);
        mCreatePostRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        mCreatePostRecycler.setAdapter(mAdapter);
        mCreatePostRecycler.addItemDecoration(new GridSpacingItemDecoration(3, 4, true));
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("请稍后...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    /**
    *点击发表事件
     */
    private void clickPostSend() {
        if (!TextUtils.isEmpty(mCreatePostContent.getText().toString()) || !mAdapter.getData().isEmpty()) {
            isConnected = SystemUtils.checkNetworkConnection(CreatePostActivity.this);
            if (isConnected) {
                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
                imagePathList = new ArrayList<>();
                for (PhotoEntry photoEntry : mAdapter.getData()) {
                    imagePathList.add(photoEntry.getPath());
                }
                if (!imagePathList.isEmpty()) {
                    // 压缩图片，返回路径
                    new CompressPhotoUtils().CompressPhoto(CreatePostActivity.this, imagePathList,
                            new CompressPhotoUtils.CompressCallBack() {
                                @Override
                                public void success(List<String> list) {
                                    imagePathList = list;
                                    String[] bmobImagePaths = new String[imagePathList.size()];
                                    bmobImagePaths = imagePathList.toArray(bmobImagePaths);
                                    final String[] finalBmobImagePaths = bmobImagePaths;
                                    BmobFile.uploadBatch(finalBmobImagePaths, new UploadBatchListener() {
                                        @Override
                                        public void onSuccess(List<BmobFile> list, List<String> urls) {
                                            if (urls.size() == finalBmobImagePaths.length) {
                                                FileUtil.deletePathFiles(TEMP_IMAGES_UPLOAD_DIR);
                                                urlsList = urls;

                                                MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
                                                Post post = new Post();
                                                post.setAuthor(myUser);
                                                post.setContent(mCreatePostContent.getText().toString());
                                                post.setCommentNum(0);
                                                post.setLikeNum(0);
                                                post.setImageUrlList(urlsList);
                                                post.save(new SaveListener<String>() {
                                                    @Override
                                                    public void done(String s, BmobException e) {
                                                        if (e == null) {
                                                            if (mProgressDialog.isShowing()) {
                                                                mProgressDialog.cancel();
                                                            }
                                                            Toast.makeText(CreatePostActivity.this,
                                                                    "发表成功!",
                                                                    Toast.LENGTH_SHORT).show();
                                                            finishAfterTransition();
                                                        } else {
                                                            Toast.makeText(CreatePostActivity.this,
                                                                    "发表失败" + e.getMessage(),
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onProgress(int i, int i1, int i2, int i3) {

                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            if (mProgressDialog.isShowing()) {
                                                mProgressDialog.cancel();
                                            }
                                            Toast.makeText(CreatePostActivity.this, "Error" +
                                            i + s, Toast.LENGTH_SHORT).show();
                                            FileUtil.deletePathFiles(TEMP_IMAGES_UPLOAD_DIR);
                                        }
                                    });
                                }
                            });
                } else {
                    MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
                    Post post = new Post();
                    post.setAuthor(myUser);
                    post.setContent(mCreatePostContent.getText().toString());
                    post.setCommentNum(0);
                    post.setLikeNum(0);
                    post.setImageUrlList(urlsList);
                    post.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                if (mProgressDialog.isShowing()) {
                                    mProgressDialog.cancel();
                                }
                                Toast.makeText(CreatePostActivity.this,
                                        "发表成功!",
                                        Toast.LENGTH_SHORT).show();
                                finishAfterTransition();
                            } else {
                                Toast.makeText(CreatePostActivity.this,
                                        "发表失败" + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            } else {
                SystemUtils.noNetworkAlert(CreatePostActivity.this);
            }
        } else {
            Toast.makeText(CreatePostActivity.this, "内容或图片不能为空!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_post_send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_post_send:
                clickPostSend();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onItemClicked(int position) {
        if (position == mAdapter.getItemCount()-1) {
            startActivity(new Intent(CreatePostActivity.this, PhotosSelectActivity.class));
            EventBus.getDefault().postSticky(new EventEntry(mAdapter.getData(),EventEntry.SELECTED_PHOTOS_ID));
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void photosMessageEvent(EventEntry entries){
        if (entries.id == EventEntry.RECEIVED_PHOTOS_ID) {
            mAdapter.reloadList(entries.photos);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void photoMessageEvent(PhotoEntry entry){
        mAdapter.appendPhoto(entry);
    }
}
