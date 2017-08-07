package com.example.cuongphan.musiqueapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

/**
 * Created by CuongPhan on 7/24/2017.
 */

public class PlayingSongNotification extends Service{
    private static final String STARTFOREGROUND_ACTION = "startforeground";
    private static final String NOTIFICATION_CLICKED_ACTION = "playing_music_control";
    private static final String PLAY_ACTION = "play_song";
    private static final String PAUSE_ACTION = "pause_song";
    private static final String PREV_ACTION = "previous_song";
    private static final String NEXT_ACTION = "next_song";
    private static final int  NOTIFICATION_ID_FOREGROUND_SERVICE = 100;
    private String songName;
    private String artist;
    private static RemoteViews remoteBigViews;
    private static RemoteViews remoteSmallViews;
    private static Notification notification;
    private static NotificationManager notificationManager;
    private static PlayingMusicControl playingMusicControl;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        playingMusicControl = new PlayingMusicControl();
        songName = intent.getStringExtra(String.valueOf(R.string.song_name));
        artist = intent.getStringExtra(String.valueOf(R.string.artist));
        switch (intent.getAction()){
            case STARTFOREGROUND_ACTION:
                showNotification();
                break;
            case PREV_ACTION:
                playingMusicControl.notificationActionHandle(PREV_ACTION);
                break;
            case NEXT_ACTION:
                playingMusicControl.notificationActionHandle(NEXT_ACTION);
                break;
            case PAUSE_ACTION:
                playingMusicControl.notificationActionHandle(PAUSE_ACTION);
                break;
            case PLAY_ACTION:
                playingMusicControl.notificationActionHandle(PLAY_ACTION);
                break;
            default:
                break;
        }
        return START_STICKY;
    }

    private void showNotification() {
        remoteBigViews = new RemoteViews(getPackageName(), R.layout.playing_song_notification_bigview);
        remoteSmallViews = new RemoteViews(getPackageName(), R.layout.playing_song_notification_smallview);

        Intent notificationClickedIntent = new Intent(this, PlayingMusicControl.class);
        notificationClickedIntent.setAction(NOTIFICATION_CLICKED_ACTION);
        notificationClickedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notificationClickedPendingIntent = PendingIntent.getActivity(this, 0, notificationClickedIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent previousSongClickedIntent = new Intent(this, PlayingSongNotification.class);
        previousSongClickedIntent.setAction(PREV_ACTION);
        PendingIntent previousSongClickedPendingItent = PendingIntent.getService(this, 0, previousSongClickedIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextSongClickedIntent = new Intent(this, PlayingSongNotification.class);
        nextSongClickedIntent.setAction(NEXT_ACTION);
        PendingIntent nextSongClickedPendingIntent = PendingIntent.getService(this, 0, nextSongClickedIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playPauseSongClickedIntent = new Intent(this, PlayingSongNotification.class);
        playPauseSongClickedIntent.setAction(PAUSE_ACTION);
        PendingIntent playPauseSongClickedPendingIntent = PendingIntent.getService(this, 0, playPauseSongClickedIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteBigViews.setOnClickPendingIntent(R.id.imgbtn_previous_song, previousSongClickedPendingItent);
        remoteBigViews.setOnClickPendingIntent(R.id.imgbtn_play_pause, playPauseSongClickedPendingIntent);
        remoteBigViews.setOnClickPendingIntent(R.id.imgbtn_next_song, nextSongClickedPendingIntent);

        remoteBigViews.setTextViewText(R.id.tv_songname, songName);
        remoteSmallViews.setTextViewText(R.id.tv_songname, songName);
        remoteBigViews.setTextViewText(R.id.tv_artist, artist);
        remoteSmallViews.setTextViewText(R.id.tv_artist, artist);

        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.playing_music_notification_smallview).setCustomContentView(remoteSmallViews).setCustomBigContentView(remoteBigViews).setContentIntent(notificationClickedPendingIntent).build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        startForeground(NOTIFICATION_ID_FOREGROUND_SERVICE, notification);
    }

    public void updateNotification(String title, String artist){
        remoteBigViews.setTextViewText(R.id.tv_songname, title);
        remoteSmallViews.setTextViewText(R.id.tv_songname, title);
        remoteBigViews.setTextViewText(R.id.tv_artist, artist);
        remoteSmallViews.setTextViewText(R.id.tv_artist, artist);

        notificationManager.notify(NOTIFICATION_ID_FOREGROUND_SERVICE, notification);
    }

    public void updatePausePlaySong(int i) {
        if(i == 0){
            remoteBigViews.setImageViewResource(R.id.imgbtn_play_pause, R.drawable.ic_media_play);
            remoteSmallViews.setImageViewResource(R.id.imgbtn_play_pause, R.drawable.ic_media_play);
            notificationManager.notify(NOTIFICATION_ID_FOREGROUND_SERVICE, notification);
        }
        else{
            remoteBigViews.setImageViewResource(R.id.imgbtn_play_pause, R.drawable.ic_media_pause);
            remoteSmallViews.setImageViewResource(R.id.imgbtn_play_pause, R.drawable.ic_media_pause);
            notificationManager.notify(NOTIFICATION_ID_FOREGROUND_SERVICE, notification);
        }
    }
}
