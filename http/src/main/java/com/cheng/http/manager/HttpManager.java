package com.cheng.http.manager;

import android.content.Context;
import android.util.Log;

import com.cheng.http.interceptor.CacheInterceptor;

import com.cheng.http.interceptor.HeaderInterceptor;
import com.cheng.http.utils.HttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;

import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {

    private final static int CONN_TIME_OUT = 30;
    private final static int READ_TIME_OUT = 30;
    private final static int WRITE_TIME_OUT = 30;
    private int connTimeOut = CONN_TIME_OUT;


    private int readTimeOut = READ_TIME_OUT;
    private int writeTimeOut = WRITE_TIME_OUT;


    private Retrofit mRetrofit;
    private OkHttpClient.Builder mBuilder;
    private Context mContext;
    private String mBaseUrl;
    private Map<String, String> headerParams = new HashMap<>();

    //暂时留位，以后添加通用参数时使用
    private Map<String, String> commomParams = new HashMap<>();

    public HttpManager init(Context context) {
        mContext = context.getApplicationContext();
        if (mContext == null) {
            return null;
        }

        return this;
    }

    private void retrofitInit() {
        mBuilder = new OkHttpClient.Builder();
        mBuilder.connectTimeout(connTimeOut, TimeUnit.SECONDS);
        mBuilder.readTimeout(readTimeOut, TimeUnit.SECONDS);
        mBuilder.writeTimeout(writeTimeOut, TimeUnit.SECONDS);

        //设置缓存
        File file = new File(mContext.getExternalCacheDir(), CacheInterceptor.CACHE_NAME);
        Cache cache = new Cache(file, 1024 * 1024 * 5);
        CacheInterceptor cacheInterceptor = new CacheInterceptor(mContext);
        mBuilder.cache(cache)
                .addInterceptor(cacheInterceptor);

        //设置请求头
        if (headerParams.size() > 0) {
/*
            builder .addHeader("Accept-Encoding","gzip")
                    .addHeader("Accept","application/json")
                    .addHeader("Content-Type","application/json; charset=utf-8")
            builder.addHeader("Authorization", "Bearer " + "token");*/
            HeaderInterceptor interceptor = new HeaderInterceptor(headerParams);
            mBuilder.addInterceptor(interceptor);
        }

        //设置log打印  请求和相应打印log
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i(HttpUtils.HTTP_TAG, "log: " + message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mBuilder.addInterceptor(loggingInterceptor);

        //进行错误重连
        mBuilder.retryOnConnectionFailure(true);
    }

    public HttpManager settRetrofit() {

        if (mContext == null) {
            throw new NullPointerException("context is null,please init()");
        }
        if (mBaseUrl == null) {
            throw new NullPointerException("base url is null");
        }

        retrofitInit();
        mRetrofit = new Retrofit.Builder()
                .client(mBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(mBaseUrl)
                .build();
        return this;
    }


    public HttpManager changeBaseUrl(String url) {
        if (mRetrofit == null) {
            throw new NullPointerException("retrofit is null,use setBaseUrl() to set url");
        }
        this.mBaseUrl = url;
        mRetrofit.newBuilder()
                .client(mBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mBaseUrl)
                .build();
        return this;
    }


    public <T> T create(Class<T> tClass) {
        if (mContext == null) {
            throw new NullPointerException("context is null,please call init()");
        }
        if (mRetrofit == null) {
            throw new NullPointerException("retrofit is null , call getRetrofit() before call create()");
        }
        return mRetrofit.create(tClass);
    }

    public HttpManager setBaseUrl(String url) {
        this.mBaseUrl = url;
        return this;
    }


    private static class SingleHolder {
        private static final HttpManager INSTANCE = new HttpManager();
    }

    public static HttpManager getHttp() {
        return SingleHolder.INSTANCE;
    }

    private HttpManager() {
    }


    public int getConnTimeOut() {
        return connTimeOut;
    }

    public HttpManager setConnTimeOut(int connTimeOut) {
        this.connTimeOut = connTimeOut;
        return this;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public HttpManager setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public HttpManager addHeaders(Map<String, String> params) {
        this.headerParams.putAll(params);
        return this;
    }

    public HttpManager addHeader(String key,String value){
         this.headerParams.put(key,value);
         return this;
    }

    public HttpManager addCommonParams(Map<String,String> params ){
         this.commomParams.putAll(params);
         return this;
    }

    public HttpManager addCommonParam(String key,String value){
         this.commomParams.put(key,value);
         return this;
    }

}
