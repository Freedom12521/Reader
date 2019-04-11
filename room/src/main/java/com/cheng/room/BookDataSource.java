package com.cheng.room;

import com.cheng.room.bean.Book;
import com.cheng.room.bean.Chapter;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface BookDataSource {

    /**
     * 获取书架上的全部书籍
     * @return
     */
   Flowable<List<Book>> getBooks();


    /**
     * 获取 某本书的章节
     * @param bookId  书id
     * @param chapterId 章节id
     * @param offset 取多少条
     * @return 章节列表
     */
   Flowable<List<Chapter>> getChaptersByChapterId(int bookId, int chapterId, int offset);


  Completable insertOrUpdateBook(Book book);


  Completable insertOrUpdateChapter(Chapter...chapter);


  void deleteBook(int bookId);

  void deleteAllBook();

  void deleteChapter(int chapterId);

}
