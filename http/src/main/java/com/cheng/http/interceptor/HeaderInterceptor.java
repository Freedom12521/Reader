package com.cheng.http.interceptor;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

//设置请求头信息
public class HeaderInterceptor implements Interceptor {
    private Map<String, String> headerParams;

    public HeaderInterceptor(Map<String, String> map) {
        this.headerParams = map;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        Request.Builder builder = request.newBuilder();
        for (Map.Entry<String, String> entry : headerParams.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        builder.method(request.method(), request.body());
        Request req = builder.build();
        return chain.proceed(req);
    }
}
