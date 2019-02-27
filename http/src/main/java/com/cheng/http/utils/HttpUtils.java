package com.cheng.http.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class HttpUtils {

    public static final String HTTP_TAG = "httpTag";



    //是否有网络
    public static boolean hasNetworkConn(Context context) {
        ConnectivityManager
                connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;

    }


    //wifi是否已连接
    public static boolean hasWifiConn(Context context){
         ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
         NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
         if(networkInfo != null){
           return networkInfo.isAvailable();
         }
         return false;
    }
}
