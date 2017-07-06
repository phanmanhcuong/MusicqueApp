package com.example.cuongphan.musiqueapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by CuongPhan on 6/21/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int tabNumber;
    public PagerAdapter(FragmentManager fm, int tabNumber) {
        super(fm);
        this.tabNumber = tabNumber;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                SongsTab songsTab = new SongsTab();
                return songsTab;

            case 1:
                AlbumsTab albumsTab = new AlbumsTab();
                return albumsTab;

            case 2:
                ArtistsTab artistsTab = new ArtistsTab();
                return artistsTab;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabNumber;
    }
}
