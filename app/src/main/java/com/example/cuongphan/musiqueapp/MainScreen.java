package com.example.cuongphan.musiqueapp;

import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MainScreen extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener {
    public static MediaPlayer sMediaPlayer = new MediaPlayer();
    public static SeekBar sSongProgressBar;
    private static ImageButton sImgBtnPlay;
    private static TextView sTvSongName;
    private static TextView sTvArtist;
    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_listview);

        //add actionbar
        ActionBar actionBar = this.getSupportActionBar();
        //actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.app_icon);

        //initialize tablayyout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.string_songs));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.string_albums));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.string_artists));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //initialize viewpager
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        sSongProgressBar = (SeekBar) findViewById(R.id.seekBar_song_process);
        sSongProgressBar.setProgress(0);
        sSongProgressBar.setMax(100);

        //update seekbar
        sSongProgressBar.setOnSeekBarChangeListener(this);
        mHandler.postDelayed(updateSeekBar, 100);

        sTvSongName = (TextView)findViewById(R.id.tv_songname);
        sTvArtist = (TextView)findViewById(R.id.tv_artist);

        sImgBtnPlay = (ImageButton)findViewById(R.id.imgbtn_play);
        sImgBtnPlay.setOnClickListener(new playPauseSong());
    }


    private  Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            int duration = sMediaPlayer.getDuration();
            int currentProsition = sMediaPlayer.getCurrentPosition();
            sSongProgressBar.setMax(duration);
            sSongProgressBar.setProgress(currentProsition);
            mHandler.postDelayed(this, 100);
        }
    };
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        sSongProgressBar.setProgress(progress);
        sMediaPlayer.seekTo(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mainscreen_actionbar_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.btn_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchText = query;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
//        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                return false;
//            }
//        };
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.btn_menu){

        }
        else if(item.getItemId() == R.id.btn_search){

        }
        return true;
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

            sTvSongName.setText(song.getTitle());
            sTvArtist.setText(song.getArtist());

            sImgBtnPlay.setImageResource(R.drawable.ic_media_pause);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
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

    class playPauseSong implements View.OnClickListener {
        @Override
        public void onClick(View v) {
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
    }
}
