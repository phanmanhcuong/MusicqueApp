package com.example.cuongphan.musiqueapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by CuongPhan on 6/28/2017.
 */

public class AlbumAdapter extends BaseAdapter{
    private ArrayList<Album> mAlbumList;
    private Context context;

    public AlbumAdapter(Context context, ArrayList<Album> mAlbumList) {
        this.mAlbumList = mAlbumList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mAlbumList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.album_view, null);
        Album currentAlbum = mAlbumList.get(position);

        ImageView imgAlbumArt = (ImageView)convertView.findViewById(R.id.img_album_art);
        if(currentAlbum.getmAlbumArt() == null){
            //imgAlbumArt.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.album_background));
        } else{
            imgAlbumArt.setImageDrawable(Drawable.createFromPath(currentAlbum.getmAlbumArt()));
        }

        TextView tvAlbumName = (TextView)convertView.findViewById(R.id.tv_album_name);
        tvAlbumName.setText(currentAlbum.getmAlbumName());

        TextView tvArtist = (TextView)convertView.findViewById(R.id.tv_artist);
        tvArtist.setText(currentAlbum.getmArtist());

        return convertView;
    }
}
