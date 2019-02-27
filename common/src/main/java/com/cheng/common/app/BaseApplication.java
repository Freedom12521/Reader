package com.cheng.common.app;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cheng.common.BuildConfig;

import timber.log.Timber;

public class BaseApplication extends Application {


    private static BaseApplication baseApplication;


    public static Context getAppContext(){
        return baseApplication.getApplicationContext();
    }

    public static BaseApplication getInstance(){
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

      initARouter();
    }

    private void initARouter() {
         if(BuildConfig.DEBUG){
             ARouter.openDebug();
             ARouter.openLog();

         }
         ARouter.init(this);
    }
}
