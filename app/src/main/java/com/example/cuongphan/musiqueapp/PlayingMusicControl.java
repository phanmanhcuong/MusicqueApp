package com.example.cuongphan.musiqueapp;

import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by CuongPhan on 7/7/2017.
 */

public class PlayingMusicControl extends AppCompatActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, SeekBar.OnSeekBarChangeListener {
    public static MediaPlayer sMediaPlayer = new MediaPlayer();
    public  SeekBar sSongProgressBar;
    private static ImageButton sImgBtnPlay ;
    private static ArrayList<Song> mSongList;
    private static Song sCurrentSong;
    Handler mHandler = new Handler();
    public MainScreen sMainScreen;
    private static boolean isRepeat;
    private static boolean isShuffle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playing_song_screen);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sSongProgressBar = (SeekBar)findViewById(R.id.seekBar_song_process);
        sSongProgressBar.setProgress(0);
        sSongProgressBar.setMax(100);

        //update seekbar
        sSongProgressBar.setOnSeekBarChangeListener(this);
        mHandler.postDelayed(updateSeekBar, 100);

        sImgBtnPlay = (ImageButton)findViewById(R.id.imgbtn_play_pause);
        sImgBtnPlay.setOnClickListener(new handleImageButtonsOnClick(R.id.imgbtn_play_pause));

        Intent intent = getIntent();
        Bundle songInformation = intent.getExtras();

        //check if intent started from main screen's list song clicked or music control process clicked
        ListView songListView;
        TextView tv_songname;
        TextView tv_artist;
        if(songInformation != null || sCurrentSong != null){
            if(songInformation != null){
                sCurrentSong = songInformation.getParcelable("currentSong");
                playSong(sCurrentSong);

                mSongList = songInformation.getParcelableArrayList("songList");
            }

            tv_songname = (TextView)findViewById(R.id.tv_songname);
            tv_songname.setText(sCurrentSong.getTitle());

            tv_artist = (TextView)findViewById(R.id.tv_artist);
            tv_artist.setText(sCurrentSong.getArtist());

            songListView = (ListView)findViewById(R.id.lv_playing_music);

            SongAdapter songAdapter = new SongAdapter(this, mSongList);
            songListView.setAdapter(songAdapter);
            songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Song song = mSongList.get(position);
                    playSong(song);
                }
            });
        }

        ImageButton imgbtn_previous_song;
        ImageButton imgbtn_repeat_song;
        ImageButton imgbtn_shuffle_song;
        ImageButton imgbtn_next_song;

        TextView tv_duration = (TextView)findViewById(R.id.tv_duration);
        int duration = sMediaPlayer.getDuration();
        int minutes = (int) Math.floor(duration/1000/60);
        int seconds = ((duration/1000) - (minutes*60));
        tv_duration.setText(minutes + ":" + String.format("%02d", seconds));

        sMainScreen = new MainScreen();
    }


    private  Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            int duration = sMediaPlayer.getDuration();
            int currentProsition = sMediaPlayer.getCurrentPosition();
            sSongProgressBar.setMax(duration);
            sSongProgressBar.setProgress(currentProsition);

            //update current position textview
            TextView tv_current_position = (TextView)findViewById(R.id.tv_current_position);
            int current_position = sMediaPlayer.getCurrentPosition();
            int minutes = (int) Math.floor(current_position/1000/60);
            int seconds = ((current_position/1000) - (minutes*60));
            tv_current_position.setText(minutes + ":" + String.format("%02d", seconds));

            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        sSongProgressBar.setProgress(progress);
        sMediaPlayer.seekTo(progress);
        sMainScreen.getProgressChanged(progress, sMediaPlayer.getDuration());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.playing_music_actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return true;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    public void playSong(Song song) {
        Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.getId());
        try {
            sMediaPlayer.reset();
            sMediaPlayer.setDataSource(contentUri.toString());
            sMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            sMediaPlayer.prepare();
            sMediaPlayer.setOnCompletionListener(this);
            sMediaPlayer.setOnPreparedListener(this);
            sMediaPlayer.setOnSeekCompleteListener(this);
            sMediaPlayer.start();

            sImgBtnPlay.setImageResource(R.drawable.ic_media_pause);

            MainScreen mainScreen = new MainScreen();
            mainScreen.startPlayingASong(song);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    //play or pause playing music
    public void playPauseSong() {
        if(sMediaPlayer.isPlaying()){
            sMediaPlayer.pause();
            sImgBtnPlay.setImageResource(R.drawable.ic_media_play);
        }
        else{
            if(sMediaPlayer != null){
                sMediaPlayer.start();
                sImgBtnPlay.setImageResource(R.drawable.ic_media_pause);
            }
        }
    }


    private class handleImageButtonsOnClick implements View.OnClickListener {
        int imgbtn_id;
        public handleImageButtonsOnClick(int imgbtnID) {
            imgbtn_id = imgbtnID;
        }

        @Override
        public void onClick(View v) {
            switch (imgbtn_id){
                case R.id.imgbtn_play_pause:
                    playPauseSong();
                    sMainScreen.playPauseSongMethod();
                    break;

            }
        }
    }
}
