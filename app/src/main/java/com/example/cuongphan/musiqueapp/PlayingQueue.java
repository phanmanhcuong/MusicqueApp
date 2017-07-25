package com.example.cuongphan.musiqueapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by CuongPhan on 7/20/2017.
 */

public class PlayingQueue extends AppCompatActivity {
    Handler mHandler = new Handler();
    private static ListView lv_songList;
    private static ArrayList<Song> sSongList = new ArrayList<>();
    private SeekBar seekBar;
    private ImageButton imgbtn_PlayPause;
    private boolean isPlaying;
    private static int movedSongPosition = -1;
    private static SongAdapterPlayingQueue sSongAdapter;
    private static View movedView;
    MainScreen mMainScreen = new MainScreen();
    PlayingMusicControl mPlayingMusicControl = new PlayingMusicControl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playing_queue);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        lv_songList = (ListView)findViewById(R.id.lv_songs);
        sSongList = bundle.getParcelableArrayList(String.valueOf(R.string.song_list));
        sSongAdapter = new SongAdapterPlayingQueue(this, sSongList);
        lv_songList.setAdapter(sSongAdapter);

        //update seekbar
        seekBar = (SeekBar)findViewById(R.id.seekBar_song_process);
        mHandler.postDelayed(updateSeekBar, 100);

        //set text for textview song and artist;
        TextView tv_songname = (TextView)findViewById(R.id.tv_songname);
        TextView tv_artist = (TextView)findViewById(R.id.tv_artist);
        tv_songname.setText(bundle.getString(String.valueOf(R.string.song_name)));
        tv_artist.setText(bundle.getString(String.valueOf(R.string.artist)));

        //update play pause button
        isPlaying = mMainScreen.isPlaying();
        imgbtn_PlayPause = (ImageButton)findViewById(R.id.imgbtn_play);
        if(isPlaying){
            imgbtn_PlayPause.setImageResource(R.drawable.ic_media_pause);
        } else{
            imgbtn_PlayPause.setImageResource(R.drawable.ic_media_play);
        }
        imgbtn_PlayPause.setOnClickListener(new playPauseSong());
    }

    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            seekBar.setMax(mMainScreen.getCurrentDuration());
            seekBar.setProgress(mMainScreen.getCurrentPosition());
            mHandler.postDelayed(this, 100);
        }
    };

    private void playPauseSongMethod() {
        if (isPlaying) {
            imgbtn_PlayPause.setImageResource(R.drawable.ic_media_play);
            isPlaying = false;
        } else {
            imgbtn_PlayPause.setImageResource(R.drawable.ic_media_pause);
            isPlaying = true;
        }
    }

    private class playPauseSong implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            PlayingMusicControl playingMusicControl = new PlayingMusicControl();
            playingMusicControl.playPauseSong();
            playPauseSongMethod();
        }
    }

    public void moveSong(View movedbtn, int position, View movedView){
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(movedView);
        movedSongPosition = position;
        this.movedView = movedView;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            movedbtn.startDragAndDrop(null, shadowBuilder, null, 0);
        } else{
            movedbtn.startDrag(null, shadowBuilder, null, 0);
        }
    }

    public void dropSong(int position) {
        Song movedSong = sSongList.get(movedSongPosition);
        sSongList.remove(movedSongPosition);
        if(movedSongPosition > position){
            sSongList.add(position+1, movedSong);
        } else{
            sSongList.add(position, movedSong);
        }
        sSongAdapter.notifyDataSetChanged();
        mPlayingMusicControl.moveDropSongs(movedSongPosition, position);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(this, PlayingMusicControl.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        return true;
    }


}
