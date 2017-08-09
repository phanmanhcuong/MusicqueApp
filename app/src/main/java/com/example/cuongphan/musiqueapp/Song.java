package com.example.cuongphan.musiqueapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CuongPhan on 6/21/2017.
 */

public class Song implements Parcelable{
    private long id;
    private String title;
    private String artist;
    private long albumId;
    private long artistId;

    public Song(long id, String title, String artist, long albumId, long artistId) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.albumId = albumId;
        this.artistId = artistId;
    }

    protected Song(Parcel in) {
        id = in.readLong();
        title = in.readString();
        artist = in.readString();
        albumId = in.readLong();
        artistId = in.readLong();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getAlbumId() {
        return albumId;
    }

    public long getArtistId() {
        return artistId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeLong(albumId);
        dest.writeLong(artistId);
    }
}
