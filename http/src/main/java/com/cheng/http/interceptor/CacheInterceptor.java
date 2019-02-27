package com.cheng.http.interceptor;

import android.content.Context;

import com.cheng.http.utils.HttpUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


//okhttp3 添加网络请求缓存
public class CacheInterceptor implements Interceptor {

    public static final String CACHE_NAME = "HttpCache";

    private Context mContext;

    public CacheInterceptor(Context context){
         this.mContext = context;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        if(!HttpUtils.hasNetworkConn(mContext)){
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if(!HttpUtils.hasNetworkConn(mContext)){
            //有网络时缓存超时时间为0
             int maxAge = 0;
             response.newBuilder()
                     .header("Cache-Control","public, max-age=" +maxAge)
                     .removeHeader(CACHE_NAME)//// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                     .build();
        }else{
            //设置无网络时 缓存超时时间为 30天
            int maxStale = 60 * 60 * 24 * 30;
            response.newBuilder()
                    .header("Cache-Control","public, only-if-cached, max-age=" + maxStale)
                    .removeHeader(CACHE_NAME)
                    .build();

        }

        return response;
    }


}
