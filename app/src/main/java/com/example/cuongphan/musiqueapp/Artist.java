package com.example.cuongphan.musiqueapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CuongPhan on 7/4/2017.
 */

public class Artist implements Parcelable {
    private long mId;
    private String mName;
    private int mAlbumNumber;
    private int mSongNumber;

    public Artist(long mId, String mName, int mAlbumNumber, int mSongNumber) {
        this.mId = mId;
        this.mName = mName;
        this.mAlbumNumber = mAlbumNumber;
        this.mSongNumber = mSongNumber;
    }

    public Artist(Parcel in) {
        mId = in.readLong();
        mName = in.readString();
        mAlbumNumber = in.readInt();
        mSongNumber = in.readInt();
    }
    public static final Creator<Artist> CREATOR = new Creator<Artist>(){
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmAlbumNumber() {
        return mAlbumNumber;
    }

    public void setmAlbumNumber(int mAlbumNumber) {
        this.mAlbumNumber = mAlbumNumber;
    }

    public int getmSongNumber() {
        return mSongNumber;
    }

    public void setmSongNumber(int mSongNumber) {
        this.mSongNumber = mSongNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mName);
        dest.writeInt(mSongNumber);
        dest.writeInt(mAlbumNumber);
    }
}
