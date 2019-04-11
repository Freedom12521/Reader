package com.cheng.common.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cheng.common.R;
import com.cheng.common.databinding.ActivityBaseBinding;
import com.cheng.common.listener.PermissionsListener;
import com.cheng.common.loading.Loading;
import com.cheng.common.utils.ActivityStack;
import com.cheng.common.utils.FixMemLeak;
import com.cheng.common.utils.LogUtil;
import com.cheng.common.utils.PermissionUtil;
import com.cheng.common.utils.StatusBarUtil;
import com.walkud.rom.checker.Rom;
import com.walkud.rom.checker.RomIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;


public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity implements PermissionUtil.PermissionResult {


    private Toolbar mToolbar;
    public Context mContext;
    private TextView mTitle;
    private TextView mLeftText;
    private ImageView mLeftImg;
    private TextView mRightText;
    private ImageView mRightImg;
    private FrameLayout mTitleLayout;
    private FrameLayout mLeftLayout;
    private FrameLayout mRightLayout;
    private ActivityBaseBinding mActivityBaseBinding;
    public T mViewDataBinding;
    private Loading.Holder mHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setBeforeContentView();

        //setContentView(R.layout.activity_base);
        mActivityBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base);
        initToolbar();
        //初始化权限工具类
        PermissionUtil.init(this);
        PermissionUtil.setPermissionResult(this);
        // ActivityStack.addActivity(this);
        initView(savedInstanceState);

        //注册监听
        initListener();

    }

    protected abstract void initListener();

    //初始化控件
    protected abstract void initView(Bundle savedInstanceState);

    //获取布局
    public abstract int getLayoutId();


    protected void initLoadingIfNeed() {
        if (mHolder == null) {
            mHolder = Loading.getDefault().wrap(mActivityBaseBinding.content).withRetry(this::onLoadRetry);
        }
    }

    protected void onLoadRetry() {

    }

    public void showLoading() {
        initLoadingIfNeed();
        mHolder.showLoading();
    }

    public void showLoadSuccess() {
        initLoadingIfNeed();
        mHolder.showLoadSuccess();
    }

    public void showLoadFailed() {
        initLoadingIfNeed();
        mHolder.showLoadFailed();
    }

    public void showLoadEmpty() {
        initLoadingIfNeed();
        mHolder.showLoadEmpty();
    }


    private void initToolbar() {
        mToolbar = mActivityBaseBinding.toolbar;
        mTitle = mActivityBaseBinding.name;
        mLeftText = mActivityBaseBinding.toolbarLeftText;
        mLeftImg = mActivityBaseBinding.toolbarLeftImg;
        mTitleLayout = mActivityBaseBinding.titleLayout;
        mRightText = mActivityBaseBinding.toolbarRightText;
        mRightImg = mActivityBaseBinding.toolbarRightImg;
        mLeftLayout = mActivityBaseBinding.toolbarLeftLayout;
        mRightLayout = mActivityBaseBinding.toolbarRightLayout;
        FrameLayout frameLayout = mActivityBaseBinding.content;
        if (getLayoutId() > 0) {
            //View view = View.inflate(this, getLayoutId(), null);
            //frameLayout.addView(view);
            mViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), getLayoutId(), frameLayout, true);

        }
        setSupportActionBar(mToolbar);
        //隐藏toolbar左侧的title
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    public void setToolbarBackground(int colorRes) {
        if (checkToolbarVisible()) {
            return;
        }
        mToolbar.setBackgroundResource(colorRes);
    }

    public void setRightText(String text) {
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText(text);
        mRightImg.setVisibility(View.GONE);

    }

    public void setRightText(int res) {
        if (res <= 0) {
            return;
        }

        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText(res);
        mRightImg.setVisibility(View.GONE);

    }

    public void setRightTextColor(int colorRes) {
        if (colorRes <= 0) {
            return;
        }
        mRightText.setTextColor(getResources().getColor(colorRes));
    }

    public void setRightImg(int res) {
        if (res <= 0) {
            return;
        }

        mRightImg.setVisibility(View.VISIBLE);
        mRightImg.setImageResource(res);
        mRightText.setVisibility(View.GONE);
    }

    public void setLeftTextColor(int colorRes) {
        mLeftText.setTextColor(getResources().getColor(colorRes));
    }

    public void setLeftTextRes(int res) {
        if (res <= 0) {
            return;
        }
        mLeftText.setText(res);
        mLeftText.setVisibility(View.VISIBLE);
        mLeftImg.setVisibility(View.GONE);
    }

    public void setLeftText(String text) {
        mLeftText.setText(text);
        mLeftText.setVisibility(View.VISIBLE);
        mLeftImg.setVisibility(View.GONE);
    }

    public void setLeftImg(int res) {
        mLeftImg.setVisibility(View.VISIBLE);
        mLeftImg.setImageResource(res);
        mLeftText.setVisibility(View.GONE);
    }


    public void setLeftClickListener(View.OnClickListener listener) {
        mLeftLayout.setOnClickListener(listener);
    }

    public void setRightClickListener(View.OnClickListener listener) {
        mRightLayout.setOnClickListener(listener);
    }

    public View addTitleView(int layoutRes) {
        if (checkToolbarVisible()) {
            return null;
        }
        mTitleLayout.removeAllViews();
        //返回的view就是 mTitleLayout
        FrameLayout frameLayout = (FrameLayout) View.inflate(this, layoutRes, mTitleLayout);
        //mTitleLayout.addView(view);
        mTitle = null;
        //titleLayout中只能有一个子view
        return frameLayout.getChildAt(0);

    }

    public View addTitleView(View view) {
        if (checkToolbarVisible()) {
            return null;
        }
        mTitleLayout.removeAllViews();
        mTitleLayout.addView(view);
        return view;
    }

    public void setToolbarGone(boolean isGone) {
        mToolbar.setVisibility(isGone ? View.GONE : View.VISIBLE);
    }

    public void setTitle(String title) {
        if (checkToolbarVisible() || mTitle == null) {
            return;
        }
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(title);
    }

    public void setTitleRes(int res) {
        if (checkToolbarVisible() || res <= 0 || mTitle == null) {
            return;
        }
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(res);
    }

    public void setTitleColor(int colorRes) {
        if (checkToolbarVisible() || colorRes <= 0 || mTitle == null) {
            return;
        }
        mTitle.setTextColor(getResources().getColor(colorRes));
    }


    private boolean checkToolbarVisible() {
        if (mToolbar.getVisibility() == View.GONE) {
            return true;

        }
        return false;
    }

    private void setBeforeContentView() {
        Window window = getWindow();
        setTheme(R.style.BaseAppTheme);
        // window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();

        }
        //StatusBarUtil.setTranslucent(this);
        if (getStatusBarColor() > 0) {
            StatusBarUtil.setColor(this, getResources().getColor(getStatusBarColor()));
        }


    }

    protected abstract int getStatusBarColor();


    public void startNewActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        enterAnim();
    }

    public void staryNewActivity(Bundle bundle, Class clazz) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
        enterAnim();
    }

    public void startNewModuleActivity(String path) {
        ARouter.getInstance().build(path)
                .withTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                .navigation();

    }

    public void startNewModuleActivity(String path, Bundle bundle) {
        ARouter.getInstance().build(path)
                .with(bundle)
                .withTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                .navigation();
    }

    public void exit() {
        finish();
        exitAnim();
    }

    private void enterAnim() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void exitAnim() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onDestroy() {
        Rom rom = RomIdentifier.getRom();
        if (rom.getManufacturer().equals(Rom.EMUI.getManufacturer())) { //修复华为 mLastSrvView 内存泄漏问题
            FixMemLeak.fixLeak(this);
        }
        super.onDestroy();
        // ActivityStack.removeActivity(this);

    }
}
