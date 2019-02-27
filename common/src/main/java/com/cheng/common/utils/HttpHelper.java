package com.cheng.common.utils;


import com.cheng.common.app.BaseApplication;
import com.cheng.http.manager.HttpManager;

public class HttpHelper {

    private static HttpManager mHttpManager;

    private HttpHelper(){
        mHttpManager = HttpManager
                .getHttp()
                .init(BaseApplication.getAppContext())
                .setBaseUrl(Constants.baseUrl)
                .settRetrofit();
    }

    private static class SingleHolder{
        private static final HttpHelper INSTANCE = new HttpHelper();
    }


    public static HttpHelper newInstance(){
        return SingleHolder.INSTANCE;
    }

    public HttpManager getHttpManager(){
        return mHttpManager;
    }
}
