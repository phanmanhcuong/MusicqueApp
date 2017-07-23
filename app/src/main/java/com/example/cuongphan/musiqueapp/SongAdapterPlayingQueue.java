package com.example.cuongphan.musiqueapp;

import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by CuongPhan on 7/20/2017.
 */

public class SongAdapterPlayingQueue extends BaseAdapter {
    private ArrayList<Song> songs;
    private Context context;

    public SongAdapterPlayingQueue(Context context, ArrayList<Song> songs) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.song_view_playing_queue, null);
        Song currentSong = songs.get(position);

        TextView tvSongName = (TextView)convertView.findViewById(R.id.tv_songname);
        tvSongName.setText(currentSong.getTitle());

        TextView tvArtist = (TextView)convertView.findViewById(R.id.tv_artist);
        tvArtist.setText(currentSong.getArtist());

        ImageButton imgbtn_MoveSong = (ImageButton)convertView.findViewById(R.id.imgbtn_moveSong);
        final View finalConvertView = convertView;
        final PlayingQueue playingQueue = new PlayingQueue();
        imgbtn_MoveSong.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                playingQueue.moveSong(v, position, finalConvertView);
                return true;
            }
        });

        convertView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if(event.getAction() == DragEvent.ACTION_DROP){
                    playingQueue.dropSong(position);
                }
                return true;
            }
        });
        return convertView;
    }
}
