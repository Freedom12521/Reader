package com.cheng.room.dao;

import com.cheng.room.bean.Chapter;

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
public interface ChapterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertChapter(Chapter...chapters);

    @Update
    void updateChapter(Chapter chapter);

    @Delete
    void deleteChapter(Chapter chapter);


    @Query("DELETE FROM chapters WHERE bookId = :bookId")
    void deleteAll(int bookId);

    @Query(value = "SELECT * FROM CHAPTERS WHERE bookId = :bookId AND chapterId >= :nowReadChapterId LIMIT :limit")
    Flowable<List<Chapter>> loadChapter(int bookId,int nowReadChapterId,int limit);
}
