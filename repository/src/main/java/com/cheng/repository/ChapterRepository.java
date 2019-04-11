package com.cheng.repository;

import com.cheng.common.base.BaseRepository;
import com.cheng.room.bean.Chapter;
import com.cheng.room.dao.ChapterDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * 章节列表
 */
public class ChapterRepository extends BaseRepository {

    private static ChapterDao mChapterDao;

    private ChapterRepository() {

    }

    private static class HOLDER {
        private static ChapterRepository INSTANCE = new ChapterRepository();
    }

    public static ChapterRepository getInstance(ChapterDao chapterDao) {
        mChapterDao = chapterDao;
        return HOLDER.INSTANCE;
    }

    public Flowable<List<Chapter>> loadChapter(int bookId, int chapterId, int offset) {
        return mChapterDao.loadChapter(bookId, chapterId, offset);
    }


    public Completable insert(Chapter... chapters) {
        return mChapterDao.insertChapter(chapters);
    }

    public void delete(int bookId) {
        mChapterDao.deleteAll(bookId);
    }



}
