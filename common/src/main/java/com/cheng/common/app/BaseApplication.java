package com.cheng.common.app;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cheng.common.BuildConfig;
import com.cheng.common.loading.Loading;
import com.cheng.common.loading.LoadingAdapter;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

public class BaseApplication extends Application {


    private static BaseApplication baseApplication;


    public Context getAppContext() {
        return baseApplication.getApplicationContext();
    }

    public static BaseApplication getInstance() {
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        initLeakCanary();

        initARouter();
        initLoading();
    }

    private void initLoading() {
        Loading.initDefault(new LoadingAdapter());
    }

    //内存泄漏检查工具
    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    private void initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openDebug();
            ARouter.openLog();

        }
        ARouter.init(this);
    }
}
