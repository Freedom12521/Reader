package com.cheng.common.utils;

import android.app.Activity;

import java.util.Stack;

public class ActivityStack {

    private static Stack<Activity> mStack;

    public static void addActivity(Activity activity){
        if(mStack ==null){
           mStack = new Stack<>();
        }
        mStack.push(activity);
    }
    //移除栈中指定activity
    public static void removeActivity(Activity activity){
         if(mStack !=null && !mStack.isEmpty()&&activity !=null){
             activity.finish();
            mStack.remove(activity);
         }
    }
    //移除全部activity
    public static void removeAllAcitivy(){
        if(mStack !=null && mStack.size()>0){
            while (!mStack.empty()){
                mStack.pop().finish();
            }
        }
    }

    //得到某个activity
    public static Activity getActivity(Activity activity){
         if(mStack !=null && !mStack.isEmpty()){
             return mStack.get(mStack.search(activity));
         }
         return null;
    }

}
