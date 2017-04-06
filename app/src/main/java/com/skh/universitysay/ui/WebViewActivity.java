package com.skh.universitysay.ui;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.skh.universitysay.R;
import com.skh.universitysay.utils.ContentSpliderUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/3/13 0013.
 * 博客详情页
 */

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.content_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.content_webview)
    WebView contentWebView;

    private String authorId = null;
    private String imgHeadLink = null;
    private String contentUrl;

    private ContentSpliderUtils mContentSpliderUtils;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        mContentSpliderUtils = new ContentSpliderUtils();

        initProgressDialog();
        initData();
//        initContentData(contentUrl);
        initWebView();
        initToolBar();
    }

    // 从NewsItemsAdapter发过来的数据
    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Bundle data = bundle.getBundle("data");
            if (data != null) {
                authorId = data.getString("authorId");
                imgHeadLink = data.getString("imgHeadLink");
                contentUrl = data.getString("contentUrl");
                Log.d("contentUrl", contentUrl);
            }
        }
    }

    private void initWebView() {
//        contentWebView.canGoBack();
        WebSettings webSettings = contentWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);  // 启用JS
        // 设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);  // 将图片调整到适合WebView的大小
        webSettings.setLoadWithOverviewMode(true);   // 缩放至屏幕大小
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);  // 支持内容重新布局
        webSettings.setBlockNetworkImage(false); // 解决图片不显示
        webSettings.setDefaultTextEncodingName("utf-8"); // 设置编码格式
        webSettings.setLoadsImagesAutomatically(true);   // 支持自动加载图片
//        webSettings.setDefaultFontSize(35);
//        webSettings.setMinimumFontSize(25);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        contentWebView.loadUrl(contentUrl);
        contentWebView.setWebViewClient(new WebViewClient() {
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
                Toast.makeText(WebViewActivity.this, "发生错误!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });
    }

//    public void initContentData(final String contentUrl) {
//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                String articleContent = mContentSpliderUtils.getContent(contentUrl);
//                String content = "<html><body>" + articleContent + "</body></html>";
////                Log.d("CONTENT", content);
//                e.onNext(content);
//                e.onComplete();
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.d("TAG", "onSubscribe");
//                    }
//
//                    @Override
//                    public void onNext(String s) {
////                        Log.d("CONTENT", s);
////                        contentWebView.loadData(s, "text/html", "utf-8");
//                        contentWebView.loadDataWithBaseURL(null, s, "text/html", "utf-8", null);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d("TAG", "onComplete");
//                    }
//                });
//    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && contentWebView.canGoBack()) {
            contentWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initToolBar() {
        Glide.with(WebViewActivity.this).load(imgHeadLink).asBitmap()
                .centerCrop()
                .into(new SimpleTarget<Bitmap>(96, 96) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        RoundedBitmapDrawable drawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        drawable.setCircular(true);
                        mToolbar.setLogo(drawable);
                    }
                });
        mToolbar.setTitle(authorId);
        setSupportActionBar(mToolbar);
        mToolbar.setOverflowIcon(
                ContextCompat.getDrawable(WebViewActivity.this, R.drawable.ic_more_vert_black_24dp)
        );
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, contentUrl);
                startActivity(Intent.createChooser(intent, getTitle()));
                break;
            case R.id.web_open:
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(contentUrl));
                startActivity(intent1);
                break;
            case R.id.copy_link:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newRawUri("label", Uri.parse(contentUrl));
                clipboardManager.setPrimaryClip(clipData);
                Snackbar.make(contentWebView, "已复制到黏贴版", Snackbar.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
