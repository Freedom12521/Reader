package com.cheng.http.utils;

import android.content.Context;

import com.cheng.http.manager.HttpManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 把loader中的公共操作放入此父类中
 * 减少重复代码
 * @param <T>
 */
public class ObjectLoader<T> {

    private T mService;

    public ObjectLoader(Context context,String baseUrl,Class<T> t){
         mService = HttpManager.getHttp().init(context).setBaseUrl(baseUrl).settRetrofit().create(t);
    }

    protected T getService(){
        return mService;
    }


    protected <T> Observable<T> observe(Observable<T> observable){
       return observable
               .subscribeOn(Schedulers.io())
               .unsubscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread());
    }



}
