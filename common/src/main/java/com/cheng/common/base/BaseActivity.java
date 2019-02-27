package com.cheng.common.base;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.cheng.common.listener.PermissionsListener;
import com.cheng.common.utils.ActivityStack;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 * 使用lifecycle监听activity的生命周期
 * 被监听的activity实现 LifecycleOwner接口  activity已经默认实现了LifecycleOwner接口
 * 然后进行注册对应的LifecycleObserver
 */
public abstract class BaseActivity extends AppCompatActivity {


    private PermissionsListener mPermissionsListener;
    private static final int PERMISSION_REQ_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }
        ActivityStack.addActivity(this);
        initView();

        //注册监听
        getLifecycle().addObserver(new BaseLifecycleObserver());

    }

    //初始化控件
    protected abstract void initView();

    //获取布局
    public abstract int getLayoutId();


    /**
     * 申请权限
     * @param permissions
     * @param listener
     */
    public void requestPermissions(String[] permissions,PermissionsListener listener){
          this.mPermissionsListener = listener;

        List<String> permissionList = new ArrayList<>();

        for(String permission: permissions){

            if(ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                 permissionList.add(permission);
            }
        }

      if(!permissionList.isEmpty()){
          ActivityCompat.requestPermissions(this,permissionList.toArray(new String[0]),PERMISSION_REQ_CODE);
      }else{
          mPermissionsListener.onGranted();
      }


    }

    /**
     * 权限申请结果回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_REQ_CODE:
                if(grantResults.length>0){
                   List<String> deniedPermissions = new ArrayList<>();
                   for(int i = 0 ;i< grantResults.length; i++){
                     int result = grantResults[i];
                     if(result != PackageManager.PERMISSION_GRANTED){
                           String permission = permissions[i];
                           deniedPermissions.add(permission);
                         Log.i("search", "requestPermissions: =================");
                     }
                   }

                   if(deniedPermissions.isEmpty()){
                     mPermissionsListener.onGranted();
                   }else{
                       mPermissionsListener.onDenied(deniedPermissions);
                   }
                }
                break;
        }
    }
}
