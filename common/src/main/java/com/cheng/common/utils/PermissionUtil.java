package com.cheng.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class PermissionUtil implements LifecycleObserver {


    private static final int REQUEST_CODE = 1001;

    private static PermissionResult mPermissionResult;
    private static WeakReference mWeakReference;

    private PermissionUtil() {

    }

    private static PermissionUtil getInstance() {
        return HOLDER.INSTANCE;
    }

    private static final class HOLDER {
        private static final PermissionUtil INSTANCE = new PermissionUtil();
    }


    public static void setPermissionResult(PermissionResult permissionResult) {
        mPermissionResult = permissionResult;
    }


    public static void init(AppCompatActivity activity) {
        if (mWeakReference == null) {
            mWeakReference = new WeakReference<>(activity);
            activity.getLifecycle().addObserver(getInstance());
        }

    }

    public static void init(Fragment fragment) {
        if (mWeakReference == null) {
            mWeakReference = new WeakReference<>(fragment);
            fragment.getLifecycle().addObserver(getInstance());
        }
    }

    //监听Activity或者Fragment的生命周期，在destroy时，清空引用
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        if (mWeakReference != null) {
            mWeakReference.clear();
            mWeakReference = null;
        }
        if (mPermissionResult != null) {
            mPermissionResult = null;
        }


    }

    /**
     * 请求权限
     *
     * @param permissions 要请求的权限
     */
    public static void request(String... permissions) {
        if (mWeakReference == null) {
            throw new NullPointerException("permission util  请先调用init()");
        }
        List<String> deniedPermissionList = new ArrayList<>();

        Fragment fragment = null;
        AppCompatActivity appCompatActivity = null;
        Context context = null;
        if (mWeakReference.get() instanceof Fragment) {
            fragment = (Fragment) mWeakReference.get();
            context = Objects.requireNonNull(fragment.getActivity()).getApplicationContext();
        } else if (mWeakReference.get() instanceof Activity) {
            appCompatActivity = (AppCompatActivity) mWeakReference.get();
            context = appCompatActivity.getApplicationContext();
        }
        if (context == null) {
            throw new NullPointerException("permission util context is null");
        }
        //检查权限是否通过
        for (String permission : permissions) {
            //如果权限未通过，则添加到deniedPermissionList
            if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                deniedPermissionList.add(permission);
            }
        }


        if (deniedPermissionList.size() <= 0) { //所有权限已通过
            if (mPermissionResult != null) {
                mPermissionResult.onGranted();
            }
        } else {
            String[] requestPermission = new String[deniedPermissionList.size()];
            deniedPermissionList.toArray(requestPermission);
            if (appCompatActivity != null) { //activity中申请权限
                ActivityCompat.requestPermissions(appCompatActivity, requestPermission, REQUEST_CODE);
            } else {//fragment 中申请权限
                fragment.requestPermissions(requestPermission, REQUEST_CODE);
            }
        }
    }

    /**
     * 获取权限申请结果
     *
     * @param requestCode  请求码
     * @param permissions  请求的权限
     * @param grantResults 权限是否通过
     */
    public static void onResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_CODE) { //请求结果
            if (mPermissionResult == null) {
                return;
            }
            List<String> deniedList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) { //权限未通过
                    deniedList.add(permissions[i]); //添加到deniedList
                }
            }

            if (deniedList.size() <= 0) {
                mPermissionResult.onGranted();
                return;
            }

            //判断是否有设置了不再询问的权限
            List<String> rationales = getRationale(deniedList);
            if (rationales.size() > 0) {
                String[] rationaleArray = new String[rationales.size()];
                rationales.toArray(rationaleArray);
                mPermissionResult.onRationale(rationaleArray);
            }

            String[] denied = new String[deniedList.size()];
            deniedList.toArray(denied);
            mPermissionResult.onDenied(denied);


        }
    }

    private static List<String> getRationale(List<String> list) {
        List<String> rationalePermissions = new ArrayList<>();
        for (String permission : list) {
            if (mWeakReference.get() instanceof Activity) {
                Activity activity = (Activity) mWeakReference.get();
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    //不再询问的权限
                    rationalePermissions.add(permission);
                }
            } else if (mWeakReference.get() instanceof Fragment) {
                Fragment fragment = (Fragment) mWeakReference.get();
                if (!fragment.shouldShowRequestPermissionRationale(permission)) {
                    rationalePermissions.add(permission);
                }
            }
        }

        return rationalePermissions;
    }


    public interface PermissionResult {
        /**
         * 权限通过
         */
        void onGranted();

        /**
         * 权限被拒绝
         *
         * @param deniedPermissions 未通过的权限
         */
        void onDenied(String[] deniedPermissions);

        /**
         * 设置了不再询问
         *
         * @param rationale 需要向用户解释的权限
         */
        void onRationale(String[] rationale);
    }

}
