package com.example.cuongphan.musiqueapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Admin on 8/7/2017.
 */

public class SongsInAnAlbum extends AppCompatActivity {
    private static final String STARTFOREGROUND_ACTION = "startforeground";
    private ArrayList<Song> mSongList = new ArrayList<>();
    private ArrayList<Song> mSwapSongList = new ArrayList<>();
    private SongAdapter songAdapter;
    private GridView gv_songList;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_in_an_album);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String albumName = bundle.getString(String.valueOf(R.string.album_name));
        String artist = bundle.getString(String.valueOf(R.string.artist));
        String albumArt = bundle.getString(String.valueOf(R.string.album_art));
        //final ArrayList<Song> songList = bundle.getParcelableArrayList((String.valueOf(R.string.album_songs)));
        mSongList = bundle.getParcelableArrayList((String.valueOf(R.string.album_songs)));


        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.rl_album);
        TextView tv_albumname = (TextView)findViewById(R.id.tv_album_name);
        TextView tv_artist = (TextView)findViewById(R.id.tv_artist);
        gv_songList = (GridView)findViewById(R.id.gv_songlist);

        if(albumArt == null){
            relativeLayout.setBackgroundResource(R.drawable.album_background);
        } else{
            relativeLayout.setBackground(Drawable.createFromPath(albumArt));
        }
        tv_albumname.setText(albumName);
        tv_artist.setText(artist);

        songAdapter = new SongAdapter(this, mSongList);
        gv_songList.setAdapter(songAdapter);
        gv_songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent songNotification = new Intent(view.getContext(), PlayingSongNotification.class);
                songNotification.setAction(STARTFOREGROUND_ACTION);
                Song currentSong = mSongList.get(position);
                songNotification.putExtra(String.valueOf(R.string.song_name), currentSong.getTitle());
                songNotification.putExtra(String.valueOf(R.string.artist), currentSong.getArtist());
                view.getContext().startService(songNotification);

                Intent playingMusicControl = new Intent(view.getContext(), PlayingMusicControl.class);
                Bundle bundle = new Bundle();
                bundle.putInt("currentSongIndex", position);
                bundle.putParcelableArrayList(String.valueOf(R.string.song_list), mSongList);
                playingMusicControl.putExtras(bundle);
                startActivity(playingMusicControl);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.playing_music_actionbar_menu, menu);
        final SearchView searchView = (SearchView)menu.findItem(R.id.btn_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {
                ArrayList<Song> searchSongList = new ArrayList<Song>();
                for (Song song : mSongList){
                    if(song.getTitle().toLowerCase().contains(keyword.toLowerCase())){
                        searchSongList.add(song);
                    }
                }
                if(searchSongList.size() != 0){
                    mSwapSongList = new ArrayList<Song>(mSongList);
                    mSongList.clear();
                    mSongList = new ArrayList<Song>(searchSongList);
                    songAdapter = new SongAdapter(SongsInAnAlbum.this, mSongList);
                    gv_songList.setAdapter(null);
                    gv_songList.setAdapter(songAdapter);
                } else{
                    Toast.makeText(SongsInAnAlbum.this, "No Result !", Toast.LENGTH_LONG).show();
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
                if(mSwapSongList.size() != 0){
                    mSongList.clear();
                    mSongList = new ArrayList<Song>(mSwapSongList);
                }
                SongAdapter songAdapter = new SongAdapter(SongsInAnAlbum.this, mSongList);
                gv_songList.setAdapter(null);
                gv_songList.setAdapter(songAdapter);
                mSwapSongList.clear();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(this, MainScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        return true;
    }
}
