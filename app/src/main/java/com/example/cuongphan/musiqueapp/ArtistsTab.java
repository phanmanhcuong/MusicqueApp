package com.example.cuongphan.musiqueapp;

import android.content.ContentResolver;
import android.content.Intent;
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
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by CuongPhan on 6/21/2017.
 */

public class ArtistsTab extends Fragment {
    private ArrayList<Artist> mArtistList;
    private GridView mArtistGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mArtistList = new ArrayList<Artist>();

        View view = inflater.inflate(R.layout.tab_artists, container, false);
        mArtistGridView = (GridView) view.findViewById(R.id.lv_artist);

        getArtistList();

        ArtistAdapter artistAdapter = new ArtistAdapter(mArtistList, this.getActivity());
        mArtistGridView.setAdapter(artistAdapter);
        mArtistGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = mArtistList.get(position);
                Intent intent = new Intent(view.getContext(), ArtistSongs.class);
                ArrayList<Song> songList = getArtistSongs(artist.getmId());
                Bundle bundle = new Bundle();
                bundle.putParcelable("artist", artist);
                bundle.putParcelableArrayList(String.valueOf(R.string.song_list), songList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }

    private ArrayList<Song> getArtistSongs(long artistId) {
        ArrayList<Song> songList = new ArrayList<>();
        for(Song song: SongsTab.sSongList){
            if(song.getArtistId() == artistId){
                songList.add(song);
            }
        }
        return songList;
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
