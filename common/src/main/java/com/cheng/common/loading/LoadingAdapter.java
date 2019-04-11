package com.cheng.common.loading;

import android.view.View;

import com.cheng.common.weiget.LoadingView;


public class LoadingAdapter implements Loading.Adapter {


    @Override
    public View getView(Loading.Holder holder, View convertView, int status) {
        LoadingView loadingView = null;
        if (convertView != null && convertView instanceof LoadingView) {
            loadingView = (LoadingView) convertView;
        }

        if (convertView == null) {
            loadingView = new LoadingView(holder.getContext(), holder.getRetry());
        }

        loadingView.setStatus(status);
        Object data = holder.getData();
        loadingView.setMsgViewVisibility(true);
        return loadingView;
    }
}
