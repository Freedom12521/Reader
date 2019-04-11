package com.cheng.room.bean;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
//, indices = {
//        @Index(value = {"chapterId", "name"}, unique = true)
//}
@Entity(tableName = "chapters")
public class Chapter {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int chapterId;
    private int bookId;
    private String name;
    private String contentUrl;
    private String content;

    @Ignore
    public Chapter(int chapterId, int bookId, String name, String contentUrl) {
        this.chapterId = chapterId;
        this.bookId = bookId;
        this.name = name;
        this.contentUrl = contentUrl;
    }
    public Chapter() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
