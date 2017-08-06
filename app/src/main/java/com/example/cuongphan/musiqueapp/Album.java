package com.example.cuongphan.musiqueapp;

/**
 * Created by CuongPhan on 6/28/2017.
 */

public class Album {
    private long mId;
    private String mAlbumName;
    private String mArtist;
    private String mAlbumArt;

    public Album(long mId, String mAlbumName, String mArtist, String mAlbumArt) {
        this.mId = mId;
        this.mAlbumName = mAlbumName;
        this.mArtist = mArtist;
        this.mAlbumArt = mAlbumArt;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmAlbumName() {
        return mAlbumName;
    }

    public void setmAlbumName(String mAlbumName) {
        this.mAlbumName = mAlbumName;
    }

    public String getmArtist() {
        return mArtist;
    }

    public void setmArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public String getmAlbumArt() {
        return mAlbumArt;
    }
}
