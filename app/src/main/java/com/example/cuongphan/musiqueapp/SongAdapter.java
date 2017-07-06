package com.example.cuongphan.musiqueapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by CuongPhan on 6/22/2017.
 */

public class SongAdapter extends BaseAdapter {
    private ArrayList<Song> songs;
    private Context context;

    public SongAdapter(Context context, ArrayList<Song> songs) {
        this.songs = songs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return songs.size();
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
        convertView = LayoutInflater.from(context).inflate(R.layout.song_view, null);
        Song currentSong = songs.get(position);

        TextView tvSongName = (TextView)convertView.findViewById(R.id.tv_songname);
        tvSongName.setText(currentSong.getTitle());

        TextView tvArtist = (TextView)convertView.findViewById(R.id.tv_artist);
        tvArtist.setText(currentSong.getArtist());

        return convertView;
    }
}
