package com.cheng.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;


//添加通用参数 添加到 url或者body中
public class CommonInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        return null;
    }
}
