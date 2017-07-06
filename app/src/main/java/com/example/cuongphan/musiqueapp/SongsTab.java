package com.example.cuongphan.musiqueapp;

import android.content.ContentResolver;
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
import android.widget.ListView;
import java.util.ArrayList;
import android.Manifest;
import android.widget.Toast;

/**
 * Created by CuongPhan on 6/21/2017.
 */

public class SongsTab extends Fragment {
    private final static int PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private static boolean permissionResult;
    private ArrayList<Song> mSongList;
    private ListView mSongListView;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mSongList = new ArrayList<Song>();

        View view = inflater.inflate(R.layout.tab_songs, container, false);
        mSongListView = (ListView) view.findViewById(R.id.lv_songs);

        checkReadExternalStoragePermission();
        if(permissionResult){
            getSongList();
        }
        else{
            Toast.makeText(getContext(), "READ EXTERNAL STORAGE PERMISSION DENIED !", Toast.LENGTH_LONG).show();
            System.exit(1);
        }
        SongAdapter songAdapter = new SongAdapter(this.getActivity(), mSongList);
        mSongListView.setAdapter(songAdapter);

        mSongListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = mSongList.get(position);
                MainScreen mainScreen = new MainScreen();
                mainScreen.playSong(song);
            }
        });

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
        if(requestCode == PERMISSION_READ_EXTERNAL_STORAGE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                permissionResult = true;
            }
        }
    }

    private void getSongList() {
        ContentResolver musicResolver = getActivity().getApplicationContext().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            do {
                long id = musicCursor.getLong(idColumn);
                String title = musicCursor.getString(titleColumn);
                String artist = musicCursor.getString(artistColumn);
                mSongList.add(new Song(id, title, artist));
            }
            while (musicCursor.moveToNext());
        }
    }
}

