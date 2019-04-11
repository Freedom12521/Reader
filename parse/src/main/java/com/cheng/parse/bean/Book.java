package com.cheng.parse.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.cheng.parse.source.SourceID;

import androidx.annotation.Nullable;

public class Book implements Parcelable {

    public int bookId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bookId);
        dest.writeString(this.name);
        dest.writeString(this.author);
        dest.writeString(this.desc);
        dest.writeString(this.type);
        dest.writeString(this.updateTime);
        dest.writeString(this.newChapter);
        dest.writeString(this.bookUrl);
        dest.writeString(this.imgUrl);
        dest.writeInt(this.sourceId);
        dest.writeString(this.sourceStr);
    }

    protected Book(Parcel in) {
        this.bookId = in.readInt();
        this.name = in.readString();
        this.author = in.readString();
        this.desc = in.readString();
        this.type = in.readString();
        this.updateTime = in.readString();
        this.newChapter = in.readString();
        this.bookUrl = in.readString();
        this.imgUrl = in.readString();
        this.sourceId = in.readInt();
        this.sourceStr = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
