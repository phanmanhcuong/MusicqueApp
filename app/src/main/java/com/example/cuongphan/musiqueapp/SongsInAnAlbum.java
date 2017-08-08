package com.example.cuongphan.musiqueapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Admin on 8/7/2017.
 */

public class SongsInAnAlbum extends AppCompatActivity {
    private static final String STARTFOREGROUND_ACTION = "startforeground";
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_in_an_album);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String albumName = bundle.getString(String.valueOf(R.string.album_name));
        String artist = bundle.getString(String.valueOf(R.string.artist));
        String albumArt = bundle.getString(String.valueOf(R.string.album_art));
        final ArrayList<Song> songList = bundle.getParcelableArrayList((String.valueOf(R.string.album_songs)));

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.rl_album);
        TextView tv_albumname = (TextView)findViewById(R.id.tv_album_name);
        TextView tv_artist = (TextView)findViewById(R.id.tv_artist);
        GridView gv_songList = (GridView)findViewById(R.id.gv_songlist);

        if(albumArt == null){
            relativeLayout.setBackgroundResource(R.drawable.album_background);
        } else{
            relativeLayout.setBackground(Drawable.createFromPath(albumArt));
        }
        tv_albumname.setText(albumName);
        tv_artist.setText(artist);

        SongAdapter songAdapter = new SongAdapter(this, songList);
        gv_songList.setAdapter(songAdapter);
        gv_songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent songNotification = new Intent(view.getContext(), PlayingSongNotification.class);
                songNotification.setAction(STARTFOREGROUND_ACTION);
                Song currentSong = songList.get(position);
                songNotification.putExtra(String.valueOf(R.string.song_name), currentSong.getTitle());
                songNotification.putExtra(String.valueOf(R.string.artist), currentSong.getArtist());
                view.getContext().startService(songNotification);

                Intent playingMusicControl = new Intent(view.getContext(), PlayingMusicControl.class);
                Bundle bundle = new Bundle();
                bundle.putInt("currentSongIndex", position);
                bundle.putParcelableArrayList(String.valueOf(R.string.song_list), songList);
                playingMusicControl.putExtras(bundle);
                startActivity(playingMusicControl);
            }
        });
    }
}
