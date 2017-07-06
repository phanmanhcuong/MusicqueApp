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
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by CuongPhan on 6/21/2017.
 */

public class ArtistsTab extends Fragment {
    private ArrayList<Artist> mArtistList;
    private ListView mArtistListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mArtistList = new ArrayList<Artist>();

        View view = inflater.inflate(R.layout.tab_artists, container, false);
        mArtistListView = (ListView)view.findViewById(R.id.lv_artist);

        getArtistList();

        ArtistAdapter artistAdapter = new ArtistAdapter(mArtistList, this.getActivity());
        mArtistListView.setAdapter(artistAdapter);

        return view;
    }

    private void getArtistList() {
        ContentResolver artistResolver = getActivity().getApplicationContext().getContentResolver();
        Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor artistCursor = artistResolver.query(artistUri, null, null, null, null);
        if(artistCursor != null && artistCursor.moveToFirst()){
            int idColumn = artistCursor.getColumnIndex(MediaStore.Audio.Artists._ID);
            int nameColumn = artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
            int albumNumberColumn = artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
            int trackNumberColumn = artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
            do {
                long id = artistCursor.getLong(idColumn);
                String artistName = artistCursor.getString(nameColumn);
                int albumNumber = artistCursor.getInt(albumNumberColumn);
                int trackNumber = artistCursor.getInt(trackNumberColumn);
                mArtistList.add(new Artist(id, artistName, albumNumber, trackNumber));
            }
            while (artistCursor.moveToNext());
        }
    }
}
