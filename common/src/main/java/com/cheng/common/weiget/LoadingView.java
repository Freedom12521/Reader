package com.cheng.common.weiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheng.common.R;
import com.cheng.http.utils.HttpUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.cheng.common.loading.Loading.STATUS_EMPTY_DATA;
import static com.cheng.common.loading.Loading.STATUS_LOADING;
import static com.cheng.common.loading.Loading.STATUS_LOAD_FAILED;
import static com.cheng.common.loading.Loading.STATUS_LOAD_SUCCESS;

public class LoadingView extends LinearLayout implements View.OnClickListener {

    private final TextView mTextView;
    private final Runnable mRetryTask;
    private final ImageView mImageView;


    public LoadingView(@NonNull Context context, Runnable retryTask) {
        super(context);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.common_view_loading_status, this, true);
        mTextView = findViewById(R.id.common_loading_text);
        mImageView = findViewById(R.id.common_loading_image);
        mRetryTask = retryTask;
        setBackgroundColor(0xFFF0F0F0);
    }

    public void setMsgViewVisibility(boolean visible) {
        mTextView.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setStatus(int status) {
        boolean show = true;
        OnClickListener onClickListener = null;
        int image = R.drawable.common_loading;
        int str = R.string.str_none;
        switch (status) {
            case STATUS_LOAD_SUCCESS:
                show = false;
                break;
            case STATUS_LOADING:
                str = R.string.loading;
                break;
            case STATUS_LOAD_FAILED:
                str = R.string.load_failed;
                Boolean networkConn = HttpUtils.hasNetworkConn(getContext());
                if (networkConn != null && !networkConn) {
                    str = R.string.load_failed_no_network;
                }
                onClickListener = this;
                image = R.drawable.common_load_failed;
                break;
            case STATUS_EMPTY_DATA:
                str = R.string.empty;
                image = R.drawable.common_load_empty;
                break;
            default:
                break;
        }

        mImageView.setImageResource(image);
        setOnClickListener(onClickListener);
        mTextView.setText(str);
        setVisibility(show ? VISIBLE : GONE);
    }


    @Override
    public void onClick(View v) {
        if (mRetryTask != null) {
            mRetryTask.run();
        }
    }
}
