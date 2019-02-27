package com.cheng.http.utils;

public class ErrorException extends RuntimeException{

    private int errorCode;

     public ErrorException(int code,String message){
         super(message);
         this.errorCode = code;
     }

     public int getErrorCode(){
         return errorCode;
     }

}
