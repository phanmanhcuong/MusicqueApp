package com.example.cuongphan.musiqueapp;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.ArrayList;

/**
 * Created by CuongPhan on 6/21/2017.
 */

public class AlbumsTab extends Fragment {
    private ArrayList<Album> mAlbumList;
    private GridView mAlbumGridView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAlbumList = new ArrayList<Album>();

        View view = inflater.inflate(R.layout.tab_albums, container, false);
        mAlbumGridView = (GridView)view.findViewById(R.id.gv_albums);

        getAlbumList();

        AlbumAdapter albumAdapter = new AlbumAdapter(this.getActivity(), mAlbumList);
        mAlbumGridView.setAdapter(albumAdapter);

        mAlbumGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album album = mAlbumList.get(position);
                MainScreen mainScreen = new MainScreen();
            }
        });

        return view;
    }

    private void getAlbumList() {
        ContentResolver albumResolver = getActivity().getApplicationContext().getContentResolver();
        Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor albumCursor = albumResolver.query(albumUri, null, null, null, null);
        if(albumCursor != null && albumCursor.moveToFirst()){
            int idColumn = albumCursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            int artistColumn = albumCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            int albumNameColumn = albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            do {
                long id = albumCursor.getLong(idColumn);
                String artist = albumCursor.getString(artistColumn);
                String albumName = albumCursor.getString(albumNameColumn);
                mAlbumList.add(new Album(id, albumName, artist));
            }
            while (albumCursor.moveToNext());
        }
    }
}
