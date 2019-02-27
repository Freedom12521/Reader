package com.cheng.http.bean;

/**
 * 网络请求结果  基累
 * @param <T>
 */

public class BaseRespones<T> {

    public int code;
    public String message;
    public T  data;

    public boolean isSuccess(){
          return code ==200;
    }
}
