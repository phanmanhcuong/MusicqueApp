package com.example.cuongphan.musiqueapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainScreen extends AppCompatActivity   {
    private ViewPager viewPager;
    public static SeekBar sSongProgressBar;
    private static ImageButton sImgBtnPlay;
    private static int currentDuration;
    private static int currentPosition;
    private static boolean isPlaying;
    private static TextView tvSongName;
    private static TextView tvArtist;
    private boolean isListView;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        isListView = true;
        //add actionbar
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.app_icon);

        //initialize tablayyout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.string_songs));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.string_albums));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.string_artists));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //initialize viewpager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
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
        mHandler.postDelayed(updateSeekBar, 100);

        sImgBtnPlay = (ImageButton) findViewById(R.id.imgbtn_play);
        sImgBtnPlay.setOnClickListener(new playPauseSong());

        tvArtist = (TextView) findViewById(R.id.tv_artist);
        tvSongName = (TextView) findViewById(R.id.tv_songname);

        RelativeLayout rl_song_process = (RelativeLayout) findViewById(R.id.rl_song_process);
        rl_song_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this, PlayingMusicControl.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            sSongProgressBar.setMax(currentDuration);
            sSongProgressBar.setProgress(currentPosition);
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainscreen_actionbar_menu, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.btn_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {
                Fragment currentTab = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                if (viewPager.getCurrentItem() == 0) {
                    SongsTab songsTab = (SongsTab) currentTab;
                    songsTab.searchSong(MainScreen.this, keyword);
                } else if (viewPager.getCurrentItem() == 1) {

                } else {

                }
                //close keyboard
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
                Fragment currentTab = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                if (viewPager.getCurrentItem() == 0) {
                    SongsTab songsTab = (SongsTab) currentTab;
                    songsTab.searchviewClose(MainScreen.this);
                } else if (viewPager.getCurrentItem() == 1) {

                } else {

                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = 0;
        switch (item.getItemId()){
            case R.id.btn_menu:

                break;
            case R.id.btn_search:

                break;
            case R.id.menu_listview:
                //now is gridview
                if (!isListView) {
                    isListView = true;
                    Fragment currentTab = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                    if (viewPager.getCurrentItem() == 0) {
                        SongsTab songsTab = (SongsTab) currentTab;
                        songsTab.changeGridView(1);
                    }
                }
                break;
            case R.id.menu_gridview:
                //now is listview
                if(isListView){
                    isListView = false;
                    Fragment currentTab = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                    if (viewPager.getCurrentItem() == 0) {
                        SongsTab songsTab = (SongsTab) currentTab;
                        songsTab.changeGridView(2);
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    //get current position and duration from PlayingMusicControl to update Seekbar
    public void getProgressChanged(int progress, int duration) {
        sSongProgressBar.setProgress(progress);
        currentDuration = duration;
        currentPosition = progress;
    }

    public void startPlayingASong(Song song) {
        isPlaying = true;
        sImgBtnPlay.setImageResource(R.drawable.ic_media_pause);

        tvSongName.setText(song.getTitle());
        tvArtist.setText(song.getArtist());
    }

    public void playPauseSongMethod() {
        if (isPlaying) {
            sImgBtnPlay.setImageResource(R.drawable.ic_media_play);
            isPlaying = false;
        } else {
            sImgBtnPlay.setImageResource(R.drawable.ic_media_pause);
            isPlaying = true;
        }
    }

    class playPauseSong implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            PlayingMusicControl playingMusicControl = new PlayingMusicControl();
            playingMusicControl.playPauseSong();
            playPauseSongMethod();
        }
    }

    public static int getCurrentDuration() {
        return currentDuration;
    }

    public static int getCurrentPosition() {
        return currentPosition;
    }

    public static boolean isPlaying() {
        return isPlaying;
    }
}
