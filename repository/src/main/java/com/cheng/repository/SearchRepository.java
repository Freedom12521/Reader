package com.cheng.repository;

import com.cheng.common.base.BaseRepository;
import com.cheng.common.utils.LogUtil;
import com.cheng.parse.parse.ParseHtml;
import com.cheng.parse.parse.SearchCallback;
import com.cheng.room.bean.History;
import com.cheng.room.dao.HistoryDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class SearchRepository extends BaseRepository {
    //network 爬虫抓取网络数据
    private ParseHtml mParseHtml;
    //local 本地加载搜索历史记录
    private static HistoryDao mHistoryDao;


    private SearchRepository() {

    }

    private static class HOLDER {
        private static SearchRepository INSTANCE = new SearchRepository();
    }

    public static SearchRepository getInstance(HistoryDao historyDao) {
        mHistoryDao = historyDao;
        return HOLDER.INSTANCE;
    }

    /**
     * 搜索小说
     *
     * @param keyword  关键字
     * @param callback 回调
     */
    public void search(String keyword, SearchCallback callback) {
        mParseHtml = new ParseHtml().searchExecute(keyword, callback);
    }

    /**
     * 取消搜索
     */
    public void cancel() {
        mParseHtml.cancel(true);
    }

    public boolean isCancelled() {
        return mParseHtml == null || mParseHtml.isCancelled();
    }


    public Completable addHistory(History history) {
        return mHistoryDao.insertOrUpdate(history);
    }

    //获取历史记录
    public Flowable<List<History>> getHistory() {
        return mHistoryDao.loadAll();
    }

    /**
     * 删除历史记录
     */
    public Completable deleteAllHistory(List<History> list) {
        History[] histories = new History[list.size()];
        list.toArray(histories);
        return mHistoryDao.deleteAll(histories);
    }
}
