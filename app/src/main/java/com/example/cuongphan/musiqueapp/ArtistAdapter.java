package com.example.cuongphan.musiqueapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by CuongPhan on 7/4/2017.
 */

public class ArtistAdapter extends BaseAdapter{
    private ArrayList<Artist> artists;
    private Context context;

    public ArtistAdapter(ArrayList<Artist> artists, Context context) {
        this.artists = artists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return artists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.artist_view, null);
        Artist currentArtist = artists.get(position);

        TextView tv_artist_name = (TextView)convertView.findViewById(R.id.tv_artist_name);
        tv_artist_name.setText(currentArtist.getmName());

        TextView tv_album_number = (TextView)convertView.findViewById(R.id.tv_artist_albumnumber);
        if(currentArtist.getmAlbumNumber() == 1){
            tv_album_number.setText(String.valueOf(currentArtist.getmAlbumNumber()) + " Album");
        } else{
            tv_album_number.setText(String.valueOf(currentArtist.getmAlbumNumber()) + " Albums");
        }

        TextView tv_song_number = (TextView)convertView.findViewById(R.id.tv_artist_songnumber);
        if(currentArtist.getmSongNumber() == 1){
            tv_song_number.setText(String.valueOf(currentArtist.getmSongNumber()) + " Song");
        } else{
            tv_song_number.setText(String.valueOf(currentArtist.getmSongNumber()) + " Songs");
        }
        return convertView;
    }
}
