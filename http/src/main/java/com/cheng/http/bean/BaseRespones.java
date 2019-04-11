package com.cheng.http.bean;

/**
 * 网络请求结果  基类
 * @param <T>
 */

public class BaseRespones<T> {

    private int code;
    public String message;
    public T  data;

    public boolean isSuccess(){
          return code ==200;
    }
}
