package com.cheng.parse.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 章节信息
 */
public class Catalog implements Parcelable {
    private int chapterId;
    private String chapterName;
    private String chapterUrl;
    private int bookId;

    public Catalog(String chapterName, String chapterUrl) {
        this.chapterName = chapterName;
        this.chapterUrl = chapterUrl;
    }

    public Catalog(String chapterName, String chapterUrl, int bookId) {
        this.chapterName = chapterName;
        this.chapterUrl = chapterUrl;
        this.bookId = bookId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.chapterId);
        dest.writeString(this.chapterName);
        dest.writeString(this.chapterUrl);
        dest.writeInt(this.bookId);
    }

    public Catalog() {
    }

    protected Catalog(Parcel in) {
        this.chapterId = in.readInt();
        this.chapterName = in.readString();
        this.chapterUrl = in.readString();
        this.bookId = in.readInt();
    }

    public static final Parcelable.Creator<Catalog> CREATOR = new Parcelable.Creator<Catalog>() {
        @Override
        public Catalog createFromParcel(Parcel source) {
            return new Catalog(source);
        }

        @Override
        public Catalog[] newArray(int size) {
            return new Catalog[size];
        }
    };
}
