package com.cheng.room.dao;

import com.cheng.room.bean.History;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface HistoryDao {

    /**
     * 添加和更新
     *
     * @return
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertOrUpdate(History history);

    /**
     * 读取全部历史记录
     *
     * @return
     */
    @Query("SELECT * FROM history")
    Flowable<List<History>> loadAll();

    /**
     * 删除全部历史记录
     */
    @Delete()
//"DELETE FROM HISTORY"
    Completable deleteAll(History... histories);
}
