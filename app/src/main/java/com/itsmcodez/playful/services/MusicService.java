package com.itsmcodez.playful.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.Player.Events;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.util.EventLogger;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSession.ControllerInfo;
import com.itsmcodez.playful.MainActivity;
import com.itsmcodez.playful.PlayerActivity;
import com.itsmcodez.playful.callbacks.StateHandleCallback;
import com.itsmcodez.playful.services.notification.PlaybackNotification;
import com.itsmcodez.playful.utils.MusicUtils;

public class MusicService extends Service {
    private MediaSession mediaSession;
    private ExoPlayer exoplayer;
    public static final String TAG = "MusicService";
    public String SESSION_ID = "com.itsmcodez.playful.services.MusicService.mediaSession";
    public PendingIntent SESSION_ACTIVITY;
    public boolean isStarted;
    public boolean isBound;
    private StateHandleCallback stateHandler;
    private PlaybackNotification playbackNotification;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    @Override
    public void onCreate() {
        super.onCreate();

        isStarted = false;
        isBound = false;

        playbackNotification = new PlaybackNotification();

        // Initialize SESSION_ACTIVITY
        SESSION_ACTIVITY =
                PendingIntent.getActivity(
                        getBaseContext(), 0, new Intent(getBaseContext(), PlayerActivity.class), 0);
        
        // powerManager and wakeLock
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wakeLock.acquire();

        // Initialize Player
        exoplayer =
                new ExoPlayer.Builder(getBaseContext())
                        .setAudioAttributes(MusicUtils.PLAYER_ATTRIBUTES, true)
                        .setHandleAudioBecomingNoisy(true)
                        .setWakeMode(PowerManager.PARTIAL_WAKE_LOCK)
                        .build();
        exoplayer.addAnalyticsListener(new EventLogger());

        // Initialize MediaSession
        mediaSession =
                new MediaSession.Builder(getBaseContext(), exoplayer)
                        .setSessionActivity(SESSION_ACTIVITY)
                        // .setCallback(new MediaSession.Callback() {})
                        .setId(SESSION_ID)
                        .build();

        // Assign listeners to player
        getPlayer()
                .addListener(
                        new Player.Listener() {

                            @Override
                            public void onMediaItemTransition(MediaItem mediaItem, int reason) {
                                Player.Listener.super.onMediaItemTransition(mediaItem, reason);

                                if (!getPlayer().isPlaying()) {
                                    getPlayer().prepare();
                                    getPlayer().play();
                                }

                                if (stateHandler != null) {
                                    stateHandler.onStateChanged();
                                }
                    
                                showNotification(true);
                            }

                            @Override
                            public void onPlaybackStateChanged(int playbackState) {
                                Player.Listener.super.onPlaybackStateChanged(playbackState);

                                if (playbackState == ExoPlayer.STATE_READY) {

                                    if (stateHandler != null) {
                                        stateHandler.onStateChanged();
                                    }

                                    showNotification(true);

                                } else {

                                    if (stateHandler != null) {
                                        stateHandler.onStateChanged();
                                    }

                                    showNotification(true);
                                }
                            }

                            @Override
                            public void onIsPlayingChanged(boolean isPlaying) {
                                Player.Listener.super.onIsPlayingChanged(isPlaying);

                                if (!getPlayer().isPlaying()) {
                                    if (stateHandler != null) {
                                        stateHandler.onStateChanged();
                                    }
                                    showNotification(false);
                                } else {
                                    if (stateHandler != null) {
                                        stateHandler.onStateChanged();
                                    }
                                    showNotification(true);
                                }
                            }

                            @Override
                            public void onPlayerError(PlaybackException exception) {
                                Player.Listener.super.onPlayerError(exception);

                                if (exception.errorCode
                                        == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND) {
                                    Toast.makeText(
                                                    getBaseContext(),
                                                    "Err: file not found!",
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                    if (getPlayer().getMediaItemCount() != 0) {
                                        if (getPlayer().hasNextMediaItem()) {
                                            getPlayer().seekToNextMediaItem();
                                        } else {
                                            getPlayer().seekTo(0, 0);
                                        }
                                    }
                                }

                                if (exception.errorCode
                                        == PlaybackException
                                                .ERROR_CODE_DECODING_FORMAT_UNSUPPORTED) {
                                    Toast.makeText(
                                                    getBaseContext(),
                                                    "Err: cannot play audio file!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
        
        // NotificationActionsReceiver
        NotificationActionsReceiver.setSession(mediaSession);
    }

    @Override
    public int onStartCommand(Intent intent, int arg1, int arg2) {
        isStarted = true;
        wakeLock.acquire();
        return START_STICKY;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    private IBinder iBinder = new MusicBinder();

    @Override
    public IBinder onBind(Intent intent) {
        isBound = true;
        return this.iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isBound = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {

        if (!getPlayer().isPlaying()) {
            getPlayer().release();
            mediaSession.release();
            mediaSession = null;
            isStarted = false;
            wakeLock.release();
            stopSelf(PlaybackNotification._ID);
        }

        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
    }
    
    public void setStateHandler(StateHandleCallback stateHandler) {
        this.stateHandler = stateHandler;
    }

    public Player getPlayer() {
        return mediaSession.getPlayer();
    }

    private void showNotification(boolean ongoing) {
        startForeground(
                PlaybackNotification._ID,
                playbackNotification.getMediaStyleNotification(
                        getBaseContext(), mediaSession, ongoing));
    }
    
    public static class NotificationActionsReceiver extends BroadcastReceiver {
        private static MediaSession mediaSession;
        
        @Override
        public void onReceive(Context context, Intent intent) {
            
            if(intent.getAction() != null) {
                String action = intent.getAction();
                
                // on ACTION_PREV triggered 
                if(action.equals(MusicUtils.ACTION_PREV)) {
                	if(mediaSession.getPlayer().isPlaying()) {
                        mediaSession.getPlayer().stop();
                    }
                    if(mediaSession.getPlayer().getMediaItemCount() != 0) {
                        if(mediaSession.getPlayer().hasPreviousMediaItem()) {
                            mediaSession.getPlayer().seekToPreviousMediaItem();
                        } 
                        else if(mediaSession.getPlayer().getMediaItemCount() == 1){
                            mediaSession.getPlayer().seekTo(0, 0); /* Replay same song again if media item is 1*/
                            mediaSession.getPlayer().prepare();    
                            mediaSession.getPlayer().play();     
                        }
                        else {
                            mediaSession.getPlayer().seekTo(mediaSession.getPlayer().getMediaItemCount() - 1, 0);
                        }
                    }
                }
                
                // on ACTION_PLAY_PAUSE triggered 
                if(action.equals(MusicUtils.ACTION_PLAY_PAUSE)) {
                	// Return if there are no songs
                    if(mediaSession.getPlayer().getMediaItemCount() == 0) {
                    	return;
                    }
                    if(mediaSession.getPlayer().isPlaying()) {
                        mediaSession.getPlayer().stop();
                    } else {
                        mediaSession.getPlayer().prepare();
                        mediaSession.getPlayer().play();
                    }
                }
                
                // on ACTION_NEXT triggered
                if(action.equals(MusicUtils.ACTION_NEXT)) {
                	if(mediaSession.getPlayer().isPlaying()) {
                        mediaSession.getPlayer().stop();
                    }
                    if(mediaSession.getPlayer().getMediaItemCount() != 0) {
                    	if(mediaSession.getPlayer().hasNextMediaItem()) {
                            mediaSession.getPlayer().seekToNextMediaItem();
                        } 
                        else if(mediaSession.getPlayer().getMediaItemCount() == 1){
                            mediaSession.getPlayer().seekTo(0, 0); /* Replay same song again if media item is 1*/
                            mediaSession.getPlayer().prepare();    
                            mediaSession.getPlayer().play();     
                        }
                        else {
                            mediaSession.getPlayer().seekTo(0, 0);
                        }
                    }
                }
            }
        }
        
        public static void setSession(MediaSession mediaSession) {
            NotificationActionsReceiver.mediaSession = mediaSession;
        }
    }
}
