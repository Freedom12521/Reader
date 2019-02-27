package com.cheng.parse.parse;


import android.os.Handler;
import android.os.Message;

import com.cheng.parse.bean.Book;
import com.cheng.parse.source.SourceType;

import java.util.List;

/**
 * 网页解析类  开启子线程 抓取数据
 *  通过handler 把抓取的数据发送回主线程
 *  该类的调用者需要传递 ParseCallback接口 来等待数据的返回
 *
 *  单例模式
 *
 */
public class ParseHtml {



    private ParseHtml(){

    }

    private static class SingleHolder{
        private static final ParseHtml INSTANCE = new ParseHtml();
    }

    public static ParseHtml getInstance(){
        return SingleHolder.INSTANCE;
    }


    /**
     * 解析html
     *
     * @param key 用户输入的关键字
     */
    public void parse(@SourceType int type, String key, ParseCallback callback) {
        ParseHandler handler = new ParseHandler(callback);
        switch (type) {
            case SourceType.SEARCH:
                SearchParseThread thread = new SearchParseThread(key, handler);
                thread.start();
                break;
            case SourceType.CATALOG:
                break;
            case SourceType.CONTENT:
                break;
        }

    }

    public static class ParseHandler extends Handler {
        private ParseCallback mCallback;

        public ParseHandler(ParseCallback callback) {
            this.mCallback = callback;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SourceType.SEARCH:
                    if (mCallback != null) {
                        mCallback.onSearchResult((List<Book>) msg.obj);
                    }
                    break;
                case SourceType.CATALOG:
                    break;
                case SourceType.CONTENT:
                    break;
            }
        }
    }


}
