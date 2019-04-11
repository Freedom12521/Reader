package com.cheng.parse.parse;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.cheng.parse.bean.Book;
import com.cheng.parse.bean.BookInfo;
import com.cheng.parse.bean.Catalog;
import com.cheng.parse.bean.Chapter;
import com.cheng.parse.source.SourceID;
import com.cheng.parse.source.SourceType;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 网页解析类  开启子线程 抓取数据
 * 通过handler 把抓取的数据发送回主线程
 * 该类的调用者需要传递 ParseCallback接口 来等待数据的返回
 * <p>
 * 单例模式
 */
public class ParseHtml {
    private static final int SEND_MESSAGE = 0x01;

    // 获取cpu数量
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //核心线程数
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 6));
    //最大线程数
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    //空闲线程存活时间
    private static final int KEEP_ALIVE_TIME = 30;
    //任务队列，排队任务最多64个
    private static final BlockingDeque<Runnable> mBlockingDeque = new LinkedBlockingDeque<>(64);
    //线程工厂
    private static final ThreadFactory mThreadFactory = r -> {
        final AtomicInteger mCount = new AtomicInteger(1);
        return new Thread(r, "ParseHtml#" + mCount);
    };
    //线程池
    private static final ThreadPoolExecutor mExecutor;

    static {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE
                , MAX_POOL_SIZE,
                KEEP_ALIVE_TIME
                , TimeUnit.SECONDS
                , mBlockingDeque
                , mThreadFactory);
        executor.allowCoreThreadTimeOut(true);
        mExecutor = executor;
    }

    private final FutureTask mFuture;
    private Callable<BookInfo> mCallable;
    @SourceType
    private int mType;
    private String mKeyWord;
    private Handler mHandler;
    private ChapterCallback mChapterCallback;
    private SearchCallback mSearchCallback;
    private CatalogCallback mCatalogCallback;
    private String mCatalogUrl;
    private String mChapterUrl;
    @SourceID
    private int mSourceId;


    public ParseHtml() {
        mHandler = getMainHandler();

        mCallable = () -> {
            BookInfo bookInfo = new BookInfo();
            switch (mType) {
                case SourceType.SEARCH:
                    bookInfo.type = SourceType.SEARCH;
                    bookInfo.books = SearchParse.newInstance().parseHtml(mKeyWord);
                    break;
                case SourceType.CATALOG:
                    bookInfo.type = SourceType.CATALOG;
                    bookInfo.catalogs = CatalogParse.newInstance().parse(mSourceId, mCatalogUrl);
                    break;
                case SourceType.CONTENT:
                    bookInfo.type = SourceType.CONTENT;
                    bookInfo.chapter = ChapterParse.newInstance().parse(mSourceId, mChapterUrl);
                    break;
            }

            return bookInfo;
        };

        mFuture = new FutureTask<BookInfo>(mCallable) {


            @Override
            protected void done() {
                super.done();

                try {
                    postMessage(get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //postMessage(null);
                }
            }
        };

    }


    private void postMessage(BookInfo bookInfo) {

        mHandler.obtainMessage(SEND_MESSAGE, new BookMessage(this, bookInfo)).sendToTarget();
    }

    private class BookMessage {
        ParseHtml parseHtml;
        BookInfo bookInfo;

        BookMessage(ParseHtml parseHtml, BookInfo bookInfo) {
            this.parseHtml = parseHtml;
            this.bookInfo = bookInfo;
        }

    }


    private Handler getMainHandler() {
        synchronized (ParseHtml.class) {
            if (mHandler == null) {
                mHandler = new ParseHandler(Looper.getMainLooper());
            }
        }

        return mHandler;
    }


  /*  private static class SingleHolder {
        private static final ParseHtml INSTANCE = new ParseHtml();
    }

    public static ParseHtml getInstance() {
        return SingleHolder.INSTANCE;
    }*/

    /**
     * 搜索
     *
     * @param keyword
     * @param callback
     * @return
     */
    public ParseHtml searchExecute(String keyword, SearchCallback callback) {
        return execute(-1, SourceType.SEARCH, keyword, "", "", callback);
    }

    /**
     * 书籍目录
     *
     * @param id
     * @param catalogUrl
     * @param callback
     * @return
     */
    public ParseHtml catalogExecute(@SourceID int id, String catalogUrl, CatalogCallback callback) {

        return execute(id, -1, "", catalogUrl, "", callback);
    }

    /**
     * 书籍章节
     *
     * @param id
     * @param chapterUrl
     * @param callback
     * @return
     */
    public ParseHtml chapterExecute(@SourceID int id, String chapterUrl, ChapterCallback callback) {
        return execute(id, -1, "", "", chapterUrl, callback);
    }

    /**
     * 执行任务
     *
     * @param sourceId
     * @param type
     * @param keyword
     * @param catalogUrl
     * @param chapterUrl
     * @param callback
     * @return
     */
    private ParseHtml execute(int sourceId
            , int type
            , String keyword
            , String catalogUrl
            , String chapterUrl
            , BaseParseCallback callback) {
        this.mSourceId = sourceId;
        this.mType = type;
        this.mKeyWord = keyword;
        this.mCatalogUrl = catalogUrl;
        this.mChapterUrl = chapterUrl;
        if (callback instanceof ChapterCallback) {
            this.mChapterCallback = (ChapterCallback) callback;
        } else if (callback instanceof SearchCallback) {
            this.mSearchCallback = (SearchCallback) callback;
        } else if (callback instanceof CatalogCallback) {
            this.mCatalogCallback = (CatalogCallback) callback;
        }
        mExecutor.execute(mFuture);
        return this;
    }

    /**
     * 取消任务
     *
     * @param cancel
     * @return
     */
    public Boolean cancel(boolean cancel) {
        return mFuture.cancel(cancel);
    }

    public boolean isCancelled() {
        return mFuture.isCancelled();
    }


    public static class ParseHandler extends Handler {
        public ParseHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SEND_MESSAGE) {
                BookMessage bookMessage = (BookMessage) msg.obj;

                switch (bookMessage.bookInfo.type) {
                    case SourceType.SEARCH:
                        bookMessage.parseHtml.onSearchResult(bookMessage.bookInfo.books);
                        break;
                    case SourceType.CATALOG:
                        bookMessage.parseHtml.onCatalogResult(bookMessage.bookInfo.catalogs);
                        break;
                    case SourceType.CONTENT:
                        bookMessage.parseHtml.onContent(bookMessage.bookInfo.chapter);
                        break;
                }
            }
        }


    }

    /**
     * 书籍的解析结果
     *
     * @param list
     */
    private void onSearchResult(List<Book> list) {
        if (mSearchCallback != null) {
            mSearchCallback.onSearchResult(list);
        }

    }

    /**
     * 目录的解析结果
     */

    private void onCatalogResult(List<Catalog> list) {
        if (mCatalogCallback != null) {
            mCatalogCallback.onCatalogResult(list);
        }
    }

    /**
     * 内容的解析结果
     */
    private void onContent(Chapter chapter) {
        if (mChapterCallback != null) {
            mChapterCallback.onContentResult(chapter);
        }
    }

}
