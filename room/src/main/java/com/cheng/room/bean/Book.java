package com.cheng.room.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "books"
        , indices = {@Index(value = {"name", "author"}, unique = true)})  //设置name和author唯一
public class Book {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "author")
    private String author;
    private int sourceId;
    private String newChapter;
    private String desc;
    private String img;
    private String url;
    /**
     * 已读的最后一个章节id
     */
    private int readChapterId;


    @Ignore
    public Book(String name, String author, int sourceId, String newChapter, String desc, String img, String url) {
        this.name = name;
        this.author = author;
        this.sourceId = sourceId;
        this.newChapter = newChapter;
        this.desc = desc;
        this.img = img;
        this.url = url;
    }

    public int getReadChapterId() {
        return readChapterId;
    }

    public void setReadChapterId(int readChapterId) {
        this.readChapterId = readChapterId;
    }

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getNewChapter() {
        return newChapter;
    }

    public void setNewChapter(String newChapter) {
        this.newChapter = newChapter;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
