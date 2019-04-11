package com.cheng.repository;

import com.cheng.common.base.BaseRepository;
import com.cheng.parse.parse.ParseHtml;
import com.cheng.parse.parse.SearchCallback;
import com.cheng.parse.source.SourceID;
import com.cheng.room.bean.Book;
import com.cheng.room.dao.BookDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;


public class BookRepository extends BaseRepository {


    private static BookDao mBookDao;
    private final ParseHtml mParseHtml;


    private BookRepository() {
        mParseHtml = new ParseHtml();
    }


    private static class HOLDER {
        private static BookRepository INSTANCE = new BookRepository();
    }

    public static BookRepository getInstance(BookDao bookDao) {
        mBookDao = bookDao;

        return HOLDER.INSTANCE;
    }

    public Completable insertBook(@SourceID int sourceId
            , String name
            , String author
            , String newChapter
            , String desc
            , String imgUrl
            , String url) {
        Book book = new Book(name, author, sourceId, newChapter, desc, imgUrl, url);
        return addBook(book);
    }

    public Completable updateReadChapterId(Book book) {
        return addBook(book);
    }


    private Completable addBook(Book book) {
        return mBookDao.insertBook(book);
    }

    public Flowable<List<Book>> loadBook() {
        return mBookDao.loadAllBook();
    }

    public void deleteAll() {
        mBookDao.deleteAll();
    }

    public void deleteById(int id) {
        mBookDao.deleteBook(id);
    }


}
