package com.cheng.parse.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.cheng.parse.source.SourceType;

import java.util.List;

//书籍信息
public class BookInfo implements Parcelable {

    @SourceType
    public int type;

    //书列表
    public List<Book> books = null;
    //章节列表
    public List<Catalog> catalogs = null;
    //章节内容
    public Chapter chapter =null ;

    public BookInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeTypedList(this.books);
        dest.writeTypedList(this.catalogs);
        dest.writeParcelable(this.chapter, flags);
    }

    protected BookInfo(Parcel in) {
        this.type = in.readInt();
        this.books = in.createTypedArrayList(Book.CREATOR);
        this.catalogs = in.createTypedArrayList(Catalog.CREATOR);
        this.chapter = in.readParcelable(Chapter.class.getClassLoader());
    }

    public static final Creator<BookInfo> CREATOR = new Creator<BookInfo>() {
        @Override
        public BookInfo createFromParcel(Parcel source) {
            return new BookInfo(source);
        }

        @Override
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }
    };
}
