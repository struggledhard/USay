package com.skh.universitysay.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by SKH on 2017/4/12 0012.
 * 系统工具类
 */

public class SystemUtils {
    public static boolean checkNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activityNetwork = cm.getActiveNetworkInfo();
        return activityNetwork != null && activityNetwork.isConnected();
    }

    public static void noNetworkAlert(Context context) {
        Toast.makeText(context, "No Network", Toast.LENGTH_SHORT).show();
    }

    public static void checkNetWork(Context context) {
        if (!isWifi(context) && !isInternet(context)) {
            Toast.makeText(context.getApplicationContext(), "亲， 检测到网络有问题，请设置！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context.getApplicationContext(), "网络连接正常！", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if (wifi) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isInternet(Context context) {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

        if (internet) {
            return true;
        } else {
            return false;
        }
    }
}
