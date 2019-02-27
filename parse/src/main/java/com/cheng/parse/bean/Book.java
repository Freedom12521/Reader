package com.cheng.parse.bean;

import com.cheng.parse.source.SourceID;

import androidx.annotation.Nullable;

public class Book {

    public String name;
    public String author;
    public String desc;
    public String type;
    public String updateTime;
    public String newChapter;
    //书地址
    public String bookUrl;
    public String imgUrl;
    @SourceID
    public int sourceId;


    public String sourceStr;


    public Book(@SourceID int sourceId) {
        this.sourceId = sourceId;
    }

    public Book(String name, String author, String desc, String type, String updateTime, String newChapter, String bookUrl,String imgUrl,@SourceID int sourceId) {
        this.name = name;
        this.author = author;
        this.desc = desc;
        this.type = type;
        this.updateTime = updateTime;
        this.newChapter = newChapter;
        this.bookUrl = bookUrl;
        this.imgUrl = imgUrl;
        this.sourceId = sourceId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
         if(obj instanceof Book){
             Book book = (Book) obj;
             if(this.name.equals(book.name)&& this.author.equals(book.author)){
                 return true;
             }
         }

        return false;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", desc='" + desc + '\'' +
                ", type='" + type + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", newChapter='" + newChapter + '\'' +
                ", bookUrl='" + bookUrl + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", sourceId=" + sourceId +
                ", sourceStr='" + sourceStr + '\'' +
                '}';
    }
}
