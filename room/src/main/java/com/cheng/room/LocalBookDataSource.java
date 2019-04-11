package com.cheng.room;

import com.cheng.room.bean.Book;
import com.cheng.room.bean.Chapter;
import com.cheng.room.dao.BookDao;
import com.cheng.room.dao.ChapterDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class LocalBookDataSource implements BookDataSource {

    private final BookDao mBookDao;
    private final ChapterDao mChapterDao;

    public LocalBookDataSource(BookDao mBookDao, ChapterDao mChapterDao) {
        this.mBookDao = mBookDao;
        this.mChapterDao = mChapterDao;
    }

    @Override
    public Flowable<List<Book>> getBooks() {
        return mBookDao.loadAllBook();
    }

    @Override
    public Flowable<List<Chapter>> getChaptersByChapterId(int bookId, int chapterId, int offset) {
        return mChapterDao.loadChapter(bookId, chapterId, offset);
    }

    @Override
    public Completable insertOrUpdateBook(Book book) {
        return mBookDao.insertBook(book);
    }

    @Override
    public Completable insertOrUpdateChapter(Chapter... chapter) {
        return mChapterDao.insertChapter(chapter);
    }

    @Override
    public void deleteBook(int bookId) {
        mBookDao.deleteBook(bookId);
    }

    @Override
    public void deleteAllBook() {
        mBookDao.deleteAll();
    }

    @Override
    public void deleteChapter(int chapterId) {
        mChapterDao.deleteAll(chapterId);
    }
}
