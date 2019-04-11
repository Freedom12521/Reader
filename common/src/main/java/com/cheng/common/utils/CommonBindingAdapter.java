package com.cheng.common.utils;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.cheng.common.R;

import androidx.databinding.BindingAdapter;

public class CommonBindingAdapter {

    @BindingAdapter("imageFromUrl")
    public static void bindImageFromUrl(ImageView view, String url) {

        RequestOptions options = new RequestOptions()
                .error(R.mipmap.logo)
                .placeholder(R.mipmap.logo);
        Glide.with(view.getContext())
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(options)
                .into(view);
    }
}
