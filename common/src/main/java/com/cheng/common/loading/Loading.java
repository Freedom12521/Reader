package com.cheng.common.loading;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cheng.common.utils.LogUtil;

/**
 * 适配器模式实现Loading
 * 低耦合
 * 页面的LoadingView可切换，且不需要改动页面代码
 * 页面中可指定LoadingView的显示区域（例如导航栏Title不希望被LoadingView覆盖）
 * 支持在Fragment中使用
 * 支持加载失败页面中点击重试
 * 兼容不同页面显示的UI有细微差别（例如提示文字可能不同）
 * <p>
 * 作者：齐翊
 * 链接：https://juejin.im/post/5c9649145188252d665f5229
 * 来源：掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 * 作者github地址：https://github.com/luckybilly/Gloading
 */

public class Loading {

    public static final int STATUS_LOADING = 1;
    public static final int STATUS_LOAD_SUCCESS = 2;
    public static final int STATUS_LOAD_FAILED = 3;
    public static final int STATUS_EMPTY_DATA = 4;


    private Adapter mAdapter;
    private static volatile Loading mDefault;

    private Loading() {

    }

    //获取loadingView
    public interface Adapter {

        View getView(Holder holder, View convertView, int status);
    }

    /*
    单例
     */
    public static Loading getDefault() {
        if (mDefault == null) {
            synchronized (Loading.class) {
                if (mDefault == null) {
                    mDefault = new Loading();
                }
            }

        }
        return mDefault;
    }


    public static void initDefault(Adapter adapter) {
        getDefault().mAdapter = adapter;
    }


    //
    public Holder wrap(Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        return new Holder(mAdapter, activity, viewGroup);
    }


    //包装View  在View外层添加一层FrameLayout
    public Holder wrap(View view) {
        FrameLayout wrapper = new FrameLayout(view.getContext());
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp != null) {
            wrapper.setLayoutParams(lp);
        }

        if (view.getParent() != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            int index = parent.indexOfChild(view);
            parent.removeView(view);
            parent.addView(wrapper, index);
        }

        ViewGroup.LayoutParams newLp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wrapper.addView(view, newLp);
        return new Holder(mAdapter, view.getContext(), wrapper);
    }

    /**
     * 用于展示各种状态的View
     * 如：loading状态
     * 加载出错
     * 加载内容为空
     * 等状态
     */
    public static class Holder {
        private Adapter mAdapter;
        private Context mContext;
        private Runnable mRetry;
        private View mCurrentStatusView;
        private ViewGroup mWrapper;
        private int mCurrentState;
        private SparseArray<View> mStatusView = new SparseArray<>(4);
        private Object mData;


        private Holder(Adapter adapter, Context context, ViewGroup wrapper) {
            this.mAdapter = adapter;
            this.mContext = context;
            this.mWrapper = wrapper;
        }

        //当点击重试时执行该Runnable
        public Holder withRetry(Runnable retry) {
            this.mRetry = retry;
            return this;
        }


        public Holder withData(Object data) {
            this.mData = data;
            return this;
        }


        public void showLoading() {
            showLoadingStatus(STATUS_LOADING);
        }

        public void showLoadSuccess() {
            showLoadingStatus(STATUS_LOAD_SUCCESS);
        }

        public void showLoadFailed() {
            showLoadingStatus(STATUS_LOAD_FAILED);
        }

        public void showLoadEmpty() {
            showLoadingStatus(STATUS_EMPTY_DATA);
        }

        private void showLoadingStatus(int status) {
            //如果当前状态和status相同，并且 validate 返回false
            //则返回
            if (mCurrentState == status && !validate()) {
                return;
            }
            //设置当前状态
            mCurrentState = status;
            //首先看 有没有保存过当前状态的View （重用View）
            View convertView = mStatusView.get(status);
            if (convertView == null) {
                //没有则赋值为mCurrentStatusView  （重用View）
                convertView = mCurrentStatusView;
            }

            View view = mAdapter.getView(this, convertView, status);
            if (view == null) {
                LogUtil.e("getView return  null");
                return;
            }
            //判断getView拿到的View的是否已存在
            if (view != mCurrentStatusView || mWrapper.indexOfChild(view) < 0) {
                if (mCurrentStatusView != null) { //表示view group中已存在该子View
                    mWrapper.removeView(mCurrentStatusView); //移除
                }

                mWrapper.addView(view);
                //设置View大小为填充父窗体
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                }
                //如果不是ViewGroup中的最后一个子View
            } else if (mWrapper.indexOfChild(view) != mWrapper.getChildCount() - 1) {
                //更改显示顺序
                view.bringToFront();
            }

            mCurrentStatusView = view;
            mStatusView.put(status, view);

        }

        private boolean validate() {
            if (mAdapter == null) {
                LogUtil.e("adapter is null");
            }

            if (mContext == null) {
                LogUtil.e("context is null");
            }

            if (mWrapper == null) {
                LogUtil.e("view group is null");
            }

            return mAdapter != null && mContext != null && mWrapper != null;

        }

        public Runnable getRetry() {
            return mRetry;
        }

        public Context getContext() {
            return mContext;
        }

        public ViewGroup getWrapper() {
            return mWrapper;
        }

        public <T> T getData() {
            try {
                return (T) mData;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
