package com.cheng.common.utils;

import android.util.Log;

import com.cheng.common.BuildConfig;

public class LogUtil {

    private static final String TAG = "reader";

    public static void i(String msg){
          if(BuildConfig.DEBUG){
              Log.i(TAG, msg);
          }
    }

    public static void d(String msg){
        if(BuildConfig.DEBUG){
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg){
        if(BuildConfig.DEBUG){
            Log.e(TAG, msg);
        }
    }

    public static void w(String msg){
        if(BuildConfig.DEBUG){
            Log.w(TAG, msg);
        }
    }



}
