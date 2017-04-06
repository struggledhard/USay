package com.skh.universitysay.ui;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.skh.universitysay.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SKH on 2017/3/28 0028.
 * 闲读
 */

public class WebViewXianDuActivity extends AppCompatActivity {

    @BindView(R.id.xiandu_webview)
    WebView mXianDuWebView;

    private ProgressDialog mProgressDialog;
    private String url = "http://www.gank.io/xiandu";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiandu_webview);
        ButterKnife.bind(this);

        initProgressDialog();
        initWebView();
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void initWebView() {
        WebSettings webSettings = mXianDuWebView.getSettings();
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

        mXianDuWebView.loadUrl(url);

        mXianDuWebView.setWebViewClient(new WebViewClient() {
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
                Log.d("TAG", "发生错误！");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mXianDuWebView.canGoBack()) {
            mXianDuWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
