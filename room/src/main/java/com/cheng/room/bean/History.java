package com.cheng.room.bean;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "history", indices = {@Index(value = {"history"}, unique = true)})
public class History {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String history;


    @Ignore
    public History(String history) {
        this.history = history;
    }

    @Ignore
    public History(int id, String history) {
        this.id = id;
        this.history = history;
    }

    public History() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
