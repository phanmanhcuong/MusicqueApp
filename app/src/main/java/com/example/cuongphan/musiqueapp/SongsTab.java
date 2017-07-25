package com.example.cuongphan.musiqueapp;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import android.Manifest;
import android.widget.Toast;

/**
 * Created by CuongPhan on 6/21/2017.
 */

public class SongsTab extends Fragment {
    private final static int PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private static boolean permissionResult;
    private static ArrayList<Song> sSongList;
    private static GridView mSongGridView;
    private static ArrayList<Song> sSongListSwap;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        sSongList = new ArrayList<Song>();

        View view = inflater.inflate(R.layout.tab_songs, container, false);
        mSongGridView = (GridView) view.findViewById(R.id.lv_songs);

        checkReadExternalStoragePermission();
        if (permissionResult) {
            getSongList();
        } else {
            Toast.makeText(getContext(), "READ EXTERNAL STORAGE PERMISSION DENIED !", Toast.LENGTH_LONG).show();
            System.exit(1);
        }
        SongAdapter songAdapter = new SongAdapter(this.getActivity(), sSongList);
        mSongGridView.setAdapter(songAdapter);

        mSongGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createIntent(view.getContext(), position);
            }
        });

        sSongListSwap = new ArrayList<>();
        return view;
    }

    private void checkReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            permissionResult = true;
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionResult = true;
            }
        }
    }

    private void getSongList() {
        ContentResolver musicResolver = getActivity().getApplicationContext().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        sSongList.clear();
        if (musicCursor != null && musicCursor.moveToFirst()) {
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            do {
                long id = musicCursor.getLong(idColumn);
                String title = musicCursor.getString(titleColumn);
                String artist = musicCursor.getString(artistColumn);
                sSongList.add(new Song(id, title, artist));
            } while (musicCursor.moveToNext());
        }
    }

    public void searchSong(final MainScreen mainScreen, String keyword) {
        final ArrayList<Song> searchSongList = new ArrayList<Song>();
        for (Song song : sSongList) {
            if (song.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                searchSongList.add(song);
            }
        }
        if (searchSongList.size() != 0) {
            sSongListSwap = new ArrayList<>(sSongList);
            sSongList.clear();
            sSongList = new ArrayList<>(searchSongList);
            SongAdapter songAdapter = new SongAdapter(mainScreen, sSongList);
            mSongGridView.setAdapter(null);
            mSongGridView.setAdapter(songAdapter);
            mSongGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    createIntent(view.getContext(), position);
                }
            });
        } else {
            Toast.makeText(mainScreen, "No Result !", Toast.LENGTH_LONG).show();
        }
    }

    public void searchviewClose(final MainScreen mainScreen) {
        if (sSongListSwap.size() != 0) {
            sSongList.clear();
            sSongList = new ArrayList<>(sSongListSwap);
        }
        SongAdapter songAdapter = new SongAdapter(mainScreen, sSongList);
        mSongGridView.setAdapter(null);
        mSongGridView.setAdapter(songAdapter);
        mSongGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createIntent(view.getContext(), position);
            }
        });
        sSongListSwap.clear();
    }

    private void createIntent(Context context, int position) {
        Intent playingMusicControl = new Intent(context, PlayingMusicControl.class);
        Bundle bundle = new Bundle();
        bundle.putInt("currentSongIndex", position);
        bundle.putParcelableArrayList(String.valueOf(R.string.song_list), sSongList);
        playingMusicControl.putExtras(bundle);
        startActivity(playingMusicControl);
    }

    public void changeGridView(int columnNumber) {
        mSongGridView.setNumColumns(columnNumber);
    }
}

