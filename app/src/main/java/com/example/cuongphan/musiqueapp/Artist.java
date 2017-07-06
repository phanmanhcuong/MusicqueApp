package com.example.cuongphan.musiqueapp;

/**
 * Created by CuongPhan on 7/4/2017.
 */

public class Artist {
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
}
