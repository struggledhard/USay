package com.skh.universitysay.ui;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.skh.universitysay.R;
import com.skh.universitysay.bean.Favorite;
import com.skh.universitysay.db.FavoriteDB;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/3/26 0026.
 * android详情页
 */

public class WebViewFenLeiActivity extends AppCompatActivity {

    @BindView(R.id.collapse_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.img_fenlei_webview)
    ImageView mImageView;
    @BindView(R.id.toolbar_fenlei_webview)
    Toolbar mToolbar;
    @BindView(R.id.fenlei_webview)
    WebView mWebView;
    @BindView(R.id.fenlei_float_btn)
    FloatingActionButton mActionButton;

    private String author;
    private String url;
    private String newid;
    private String title;
    private List<String> images;
    private ProgressDialog mProgressDialog;
    private Favorite mFavorite;
    private FavoriteDB mFavoriteDB;

    private boolean isFavourite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenlei_webview);
        ButterKnife.bind(this);

        mFavorite = new Favorite();
        mFavoriteDB = FavoriteDB.getInstance(this);

//        isCollection();
        initData();
        initProgressDialog();
        initToolBar();
        initWebView();
        initImages();
        setFavoriteData();
        setActionButton();
        isFavourite = mFavoriteDB.isFavorite(mFavorite);
        if (isFavourite) {
            mActionButton.setImageResource(R.drawable.ic_favorite_gray_24dp);
        } else {
            mActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Bundle data = bundle.getBundle("extra");
            if (data != null) {
                author = data.getString("author");
                url = data.getString("url");
                images = data.getStringArrayList("images");
                newid = data.getString("new_id");
                title = data.getString("title");
            }
        }
    }

    private void setFavoriteData() {
        mFavorite.setNewId(newid);
        mFavorite.setAuthor(author);
        mFavorite.setUrl(url);
        mFavorite.setTitle(title);
        if (images != null) {
            mFavorite.setImage(images.get(0));
        } else {
            mFavorite.setImage("http://bmob-cdn-10359.b0.upaiyun.com/2017/05/01/50c24acdbd504ce09bdac971f7ecb1c7.jpg");
        }
    }

    /**
     * 是否已经收藏过
     */
//    private void isCollection() {
//        if (isFavourite) {
//            mActionButton.setImageResource(R.drawable.ic_favorite_gray_24dp);
//        } else {
//            mActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
//        }
//    }

    /**
     * 悬浮按钮点击事件
     */
    private void setActionButton() {
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFavourite) {
                    mFavorite.setClick(true);
                    mActionButton.setImageResource(R.drawable.ic_favorite_gray_24dp);
                    mFavoriteDB.saveFavorite(mFavorite);
                } else {
                    mFavorite.setClick(false);
                    mActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    mFavoriteDB.deleteFavorite(mFavorite);
                }
            }
        });
    }

    private void initImages() {
        if (images != null) {
            String img = images.get(0);
            Glide.with(this).load(img).fitCenter().into(mImageView);
        } else {
            Glide.with(this).load(R.drawable.banner01).fitCenter().into(mImageView);
        }
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });

        if (author != null) {
            mCollapsingToolbarLayout.setTitle(author);
        } else {
            mCollapsingToolbarLayout.setTitle("佚名");
        }

        mCollapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
    }

    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);  // 启用JS
        // 设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);  // 将图片调整到适合WebView的大小
        webSettings.setLoadWithOverviewMode(true);   // 缩放至屏幕大小
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);  // 支持内容重新布局
        webSettings.setBlockNetworkImage(false); // 解决图片不显示
        webSettings.setDefaultTextEncodingName("utf-8"); // 设置编码格式
        webSettings.setLoadsImagesAutomatically(true);   // 支持自动加载图片

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mWebView.loadUrl(url);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Toast.makeText(WebViewFenLeiActivity.this, "发生错误!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_fenlei_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fenlei_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, url);
                startActivity(Intent.createChooser(intent, getTitle()));
                break;
            case R.id.fenlei_web_open:
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(url));
                startActivity(intent1);
                break;
            case R.id.fenlei_copy_link:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newRawUri("label", Uri.parse(url));
                clipboardManager.setPrimaryClip(clipData);
                Snackbar.make(mWebView, "已复制到黏贴版", Snackbar.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
