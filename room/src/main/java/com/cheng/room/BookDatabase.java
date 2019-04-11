package com.cheng.room;

import android.content.Context;

import com.cheng.room.bean.Book;
import com.cheng.room.bean.Chapter;
import com.cheng.room.bean.History;
import com.cheng.room.dao.BookDao;
import com.cheng.room.dao.ChapterDao;
import com.cheng.room.dao.HistoryDao;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Book.class, Chapter.class, History.class}, version =3, exportSchema = false)
public abstract class BookDatabase extends RoomDatabase {

    private static volatile BookDatabase INSTANCE;

    public abstract BookDao bookDao();

    public abstract ChapterDao chapterDao();

    public abstract HistoryDao historyDao();

    public static BookDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BookDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext()
                            , BookDatabase.class
                            , "reader.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Migration migration = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };


}
