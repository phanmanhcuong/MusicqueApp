package com.example.cuongphan.musiqueapp;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by CuongPhan on 7/7/2017.
 */

public class PlayingMusicControl extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, SeekBar.OnSeekBarChangeListener {
    public static MediaPlayer sMediaPlayer = new MediaPlayer();
    public SeekBar sSongProgressBar;
    private static ImageButton sImgBtnPlay;
    private static ArrayList<Song> sSongList;
    private static ArrayList<Song> sSwapSongList;
    private static Song sCurrentSong;
    private static int sCurrentSongIndex;
    private GridView songListView;
    private static SongAdapter sSongAdapter;
    private TextView tv_songname;
    private TextView tv_artist;
    private TextView tv_duration;
    private TextView tv_current_position;
    Handler mHandler = new Handler();
    public MainScreen sMainScreen;
    private static boolean isRepeat;
    private static boolean isShuffle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playing_song_screen);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sSongProgressBar = (SeekBar) findViewById(R.id.seekBar_song_process);

        //update seekbar
        sSongProgressBar.setOnSeekBarChangeListener(this);
        mHandler.postDelayed(updateSeekBar, 100);

        sImgBtnPlay = (ImageButton) findViewById(R.id.imgbtn_play_pause);
        if (sMediaPlayer.isPlaying()) {
            sImgBtnPlay.setImageResource(R.drawable.ic_media_pause);
        } else {
            sImgBtnPlay.setImageResource(R.drawable.ic_media_play);
        }
        sImgBtnPlay.setOnClickListener(new handleImageButtonsOnClick(sImgBtnPlay, R.id.imgbtn_play_pause));

        Intent intent = getIntent();
        Bundle songInformation = intent.getExtras();

        //check if intent started from main screen's list song clicked or music control process clicked
        songListView = (GridView) findViewById(R.id.lv_playing_music);
        if (songInformation != null || sCurrentSong != null) {
            if (songInformation != null) {
                sSongList = songInformation.getParcelableArrayList("songList");
                sCurrentSongIndex = songInformation.getInt("currentSongIndex");
                sCurrentSong = sSongList.get(sCurrentSongIndex);
                playSong(sCurrentSong);
            }

            sSongAdapter = new SongAdapter(this, sSongList);
            songListView.setAdapter(sSongAdapter);
            songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    sCurrentSongIndex = position;
                    sCurrentSong = sSongList.get(sCurrentSongIndex);
                    playSong(sCurrentSong);
                }
            });
        }

        ImageButton imgbtn_previous_song = (ImageButton) findViewById(R.id.imgbtn_previous_song);
        imgbtn_previous_song.setOnClickListener(new handleImageButtonsOnClick(imgbtn_previous_song, R.id.imgbtn_previous_song));

        ImageButton imgbtn_repeat_song = (ImageButton) findViewById(R.id.imgbtn_repeat);
        imgbtn_repeat_song.setOnClickListener(new handleImageButtonsOnClick(imgbtn_repeat_song, R.id.imgbtn_repeat));
        if(isRepeat){
            imgbtn_repeat_song.setImageResource(R.drawable.active_repeat);
        }

        ImageButton imgbtn_shuffle_song = (ImageButton) findViewById(R.id.imgbtn_shuffle);
        imgbtn_shuffle_song.setOnClickListener(new handleImageButtonsOnClick(imgbtn_shuffle_song, R.id.imgbtn_shuffle));
        if(isShuffle){
            imgbtn_shuffle_song.setImageResource(R.drawable.shuffle);
        }

        ImageButton imgbtn_next_song = (ImageButton) findViewById(R.id.imgbtn_next_song);
        imgbtn_next_song.setOnClickListener(new handleImageButtonsOnClick(imgbtn_next_song, R.id.imgbtn_next_song));

        tv_current_position = (TextView) findViewById(R.id.tv_current_position);
        sSwapSongList = new ArrayList<>();
        sMainScreen = new MainScreen();
    }

    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            int duration = sMediaPlayer.getDuration();
            int currentProsition = sMediaPlayer.getCurrentPosition();
            sSongProgressBar.setMax(duration);
            sSongProgressBar.setProgress(currentProsition);

            //update current position textview
            int current_position = sMediaPlayer.getCurrentPosition();
            int minutes = (int) Math.floor(current_position / 1000 / 60);
            int seconds = ((current_position / 1000) - (minutes * 60));
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
        final SearchView searchView = (SearchView)menu.findItem(R.id.btn_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {
                ArrayList<Song> searchSongList = new ArrayList<Song>();
                for (Song song : sSongList){
                    if(song.getTitle().toLowerCase().contains(keyword.toLowerCase())){
                        searchSongList.add(song);
                    }
                }
                if(searchSongList.size() != 0){
                    sSwapSongList = new ArrayList<Song>(sSongList);
                    sSongList.clear();
                    sSongList = new ArrayList<Song>(searchSongList);
                    sSongAdapter = new SongAdapter(PlayingMusicControl.this, sSongList);
                    songListView.setAdapter(null);
                    songListView.setAdapter(sSongAdapter);
                    songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            sCurrentSongIndex = position;
                            sCurrentSong = sSongList.get(sCurrentSongIndex);
                            playSong(sCurrentSong);
                        }
                    });
                } else{
                    Toast.makeText(PlayingMusicControl.this, "No Result !", Toast.LENGTH_LONG).show();
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.btn_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if(sSwapSongList.size() != 0){
                    sSongList.clear();
                    sSongList = new ArrayList<Song>(sSwapSongList);
                }
                SongAdapter songAdapter = new SongAdapter(PlayingMusicControl.this, sSongList);
                songListView.setAdapter(null);
                songListView.setAdapter(songAdapter);
                songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        sCurrentSongIndex = position;
                        sCurrentSong = sSongList.get(sCurrentSongIndex);
                        playSong(sCurrentSong);
                    }
                });
                sSwapSongList.clear();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.btn_search:

                break;
            case R.id.btn_menu:
                Intent playingQueueIntent = new Intent(getApplicationContext(), PlayingQueue.class);
                Bundle bundle = new Bundle();
                bundle.putString("songName", sCurrentSong.getTitle());
                bundle.putString("Artist", sCurrentSong.getArtist());
                bundle.putParcelableArrayList("songList", sSongList);
                playingQueueIntent.putExtras(bundle);
                playingQueueIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(playingQueueIntent);
                break;

        }
        return true;
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
            sMediaPlayer.setLooping(false);
            sMediaPlayer.prepare();
            sMediaPlayer.start();

            tv_songname = (TextView) findViewById(R.id.tv_songname);
            tv_artist = (TextView) findViewById(R.id.tv_artist);
            tv_duration = (TextView) findViewById(R.id.tv_duration);

            tv_songname.setText(sCurrentSong.getTitle());
            tv_artist.setText(sCurrentSong.getArtist());
            int duration = sMediaPlayer.getDuration();
            int minutes = (int) Math.floor(duration / 1000 / 60);
            int seconds = ((duration / 1000) - (minutes * 60));
            tv_duration.setText(minutes + ":" + String.format("%02d", seconds));

            sMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (isRepeat) {
                        playSong(sCurrentSong);
                        //if shuffle is on
                    } else if (isShuffle) {
                        Random random = new Random();
                        sCurrentSongIndex = random.nextInt(sSongList.size());
                        sCurrentSong = sSongList.get(sCurrentSongIndex);
                        playSong(sCurrentSong);
                    } else {
                        if (sCurrentSongIndex < (sSongList.size() - 1)) {
                            sCurrentSongIndex += 1;
                        } else {
                            sCurrentSongIndex = 0;
                        }
                        sCurrentSong = sSongList.get(sCurrentSongIndex);
                        playSong(sCurrentSong);
                    }
                }
            });

            sMediaPlayer.setOnPreparedListener(this);
            sMediaPlayer.setOnSeekCompleteListener(this);
            sImgBtnPlay.setImageResource(R.drawable.ic_media_pause);

            songListView.setSelection(sCurrentSongIndex);

            MainScreen mainScreen = new MainScreen();
            mainScreen.startPlayingASong(song);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    //play or pause playing music
    public void playPauseSong() {
        if (sMediaPlayer.isPlaying()) {
            sMediaPlayer.pause();
            sImgBtnPlay.setImageResource(R.drawable.ic_media_play);
        } else {
            if (sMediaPlayer != null) {
                sMediaPlayer.start();
                sImgBtnPlay.setImageResource(R.drawable.ic_media_pause);
            }
        }
    }

    public void moveDropSongs(int movedSongPosition, int position) {
        Song movedSong = sSongList.get(movedSongPosition);
        sSongList.remove(movedSongPosition);
        if(movedSongPosition > position){
            sSongList.add(position+1, movedSong);
        } else{
            sSongList.add(position, movedSong);
        }
        sSongAdapter.notifyDataSetChanged();
    }


    private class handleImageButtonsOnClick implements View.OnClickListener {
        int imgbtn_id;
        ImageButton imgbtn;

        public handleImageButtonsOnClick(ImageButton imgBtn, int imgbtnID) {
            imgbtn_id = imgbtnID;
            imgbtn = imgBtn;
        }

        @Override
        public void onClick(View v) {
            switch (imgbtn_id) {
                case R.id.imgbtn_play_pause:
                    playPauseSong();
                    sMainScreen.playPauseSongMethod();
                    break;
                case R.id.imgbtn_repeat:
                    if (isRepeat) {
                        isRepeat = false;
                        imgbtn.setImageResource(R.drawable.ic_repeat_white_18dp);
                        Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    } else {
                        isRepeat = true;
                        imgbtn.setImageResource(R.drawable.active_repeat);
                        Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.imgbtn_shuffle:
                    if (isShuffle) {
                        isShuffle = false;
                        imgbtn.setImageResource(R.drawable.ic_shuffle_white_18dp);
                        Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    } else {
                        isShuffle = true;
                        imgbtn.setImageResource(R.drawable.shuffle);
                        Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.imgbtn_previous_song:
                    if (sCurrentSongIndex == 0) {
                        sCurrentSongIndex = sSongList.size() - 1;
                    } else {
                        sCurrentSongIndex += -1;
                    }
                    sCurrentSong = sSongList.get(sCurrentSongIndex);
                    playSong(sCurrentSong);
                    break;
                case R.id.imgbtn_next_song:
                    if (sCurrentSongIndex == (sSongList.size() - 1)) {
                        sCurrentSongIndex = 0;
                    } else {
                        sCurrentSongIndex += 1;
                    }
                    sCurrentSong = sSongList.get(sCurrentSongIndex);
                    playSong(sCurrentSong);
                    break;
            }
        }
    }
}
