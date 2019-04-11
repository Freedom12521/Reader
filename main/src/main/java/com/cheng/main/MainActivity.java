package com.cheng.main;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cheng.common.base.BaseActivity;
import com.cheng.common.utils.ARouterPath;
import com.cheng.common.utils.LogUtil;
import com.cheng.main.databinding.MainActivityMainBinding;
import com.cheng.room.bean.Book;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;


public class MainActivity extends BaseActivity<MainActivityMainBinding> {


    private MainViewModel mMainViewModel;
    private MainAdapter mainAdapter;

    @Override
    protected int getStatusBarColor() {
        return R.color.colorPrimaryDark;
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_activity_main;
    }

    @Override
    protected void initView(Bundle saveInstanceState) {
        setTitle("书架");
        setRightText("搜索");
        setTitleColor(R.color.white);
        setRightTextColor(R.color.white);
        setToolbarBackground(R.color.colorPrimaryDark);

        MainViewModelFactory factory = InjectorUtils.providerMainViewModelFactory(this);
        //得到一个MainViewModel，它的生命周期和Activity绑定，通过factory创建ViewModel实例
        mMainViewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);

        mainAdapter = new MainAdapter();
        mViewDataBinding.mainRv.setAdapter(mainAdapter);

        subscribeUi(mainAdapter);
    }

    private void subscribeUi(MainAdapter mainAdapter) {
        //注册订阅                                     使用方法引用
        mMainViewModel.getList().observe(this, mainAdapter::submitList);
    }


    @Override
    protected void initListener() {
        mainAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public View.OnClickListener onClick(int id) {
                return v -> {
                    //跳转到读书页面
                };
            }

            @Override
            public View.OnLongClickListener onLongClick(int id) {
                return v -> {
                    mMainViewModel.delete(id);
                    return true;
                };
            }
        });

        setRightClickListener(v -> startNewModuleActivity(ARouterPath.SEARCH_MAIN_PATH));

    }

    @Override
    public void onGranted() {

    }

    @Override
    public void onDenied(String[] deniedPermissions) {

    }

    @Override
    public void onRationale(String[] rationale) {

    }

    //在Home界面点击返回键时，相当于点击Home按键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);
    }


}
