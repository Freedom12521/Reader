package com.cheng.room.dao;

import com.cheng.room.bean.Book;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertBook(Book book);

    @Update
    void updateBook(Book book);

    @Query("DELETE FROM books where id = :bookId")
    void deleteBook(int bookId);

    /**
     * 查询所有
     *
     * @return
     */
    @Query("SELECT * FROM books")
    Flowable<List<Book>> loadAllBook();

    @Query("SELECT * FROM books WHERE id= :id")
    Flowable<Book> loadBookById(int id);


    /**
     * 删除所有
     */

    @Query("DELETE FROM books")
    void deleteAll();


}
