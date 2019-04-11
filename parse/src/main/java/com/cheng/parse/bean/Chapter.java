package com.cheng.parse.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 章节内容
 */

public class Chapter implements Parcelable {

    public int bookId;
    public int chapterId;
    public String content;
    public String title;

    public Chapter(String content, String title) {
        this.content = content;
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bookId);
        dest.writeInt(this.chapterId);
        dest.writeString(this.content);
    }


    public Chapter() {
    }

    protected Chapter(Parcel in) {
        this.bookId = in.readInt();
        this.chapterId = in.readInt();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<Chapter> CREATOR = new Parcelable.Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel source) {
            return new Chapter(source);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };
}
