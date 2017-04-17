package com.skh.universitysay.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by SKH on 2017/3/12 0012.
 * 网络请求
 */

public class HttpUtils {
    private OkHttpClient mHttpClient = new OkHttpClient();

    // 头条
    public String pageUrlContent(int page) {
        String url = "http://172.16.103.241:8080/BlogSplider/BlogList?page=" + page;
//        String url = "http://192.168.1.100:8080/BlogSplider/BlogList?page=" + page;
        Log.d("TAG", url);
        String result = runByHttp(url);
        return result;
    }

    // Android数据请求
    public String getAndroidData(int page) {
        String url = "http://gank.io/api/data/Android/10" + "/" + page;
        return runByHttp(url);
    }

    // iOS数据请求
    public String getIosData(int page) {
        String url = "http://gank.io/api/data/iOS/10/" + page;
        return runByHttp(url);
    }

    // 前端数据
    public String getWebData(int page) {
        String url = "http://gank.io/api/data/前端/10/" + page;
        return runByHttp(url);
    }

    // 拓展资源
    public String getExpandData(int page) {
        String url = "http://gank.io/api/data/拓展资源/10/" + page;
        return runByHttp(url);
    }

    @Nullable
    private String runByHttp(String url) {

        Request request = new Request.Builder().url(url).get().build();

        try {
            System.setProperty("http.keepAlive", "false");
            Response response = mHttpClient.newCall(request).execute();
            String result = response.body().string();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
