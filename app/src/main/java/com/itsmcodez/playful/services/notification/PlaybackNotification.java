package com.itsmcodez.playful.services.notification;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.core.app.NotificationCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaStyleNotificationHelper;
import com.itsmcodez.playful.BaseApplication;
import com.itsmcodez.playful.R;
import com.itsmcodez.playful.services.MusicService;
import com.itsmcodez.playful.utils.MusicUtils;

public class PlaybackNotification {
    public static int _ID = 1;
    private MediaStyleNotificationHelper.MediaStyle mediaStyle;
    private NotificationCompat.Builder mediaStyleNotification;
    
    private void setMediaStyleNotification(Context context, MediaSession mediaSession, boolean ongoing){
        
        // notification style
        mediaStyle = new MediaStyleNotificationHelper.MediaStyle(mediaSession);
        mediaStyle.setShowActionsInCompactView(0,1,2);
        mediaStyle.setShowCancelButton(true);
        
        // Album artwork 
        ImageView image = new ImageView(context);
        image.setImageURI(mediaSession.getPlayer().getCurrentMediaItem().mediaMetadata.artworkUri);
        if(image.getDrawable() == null){
            image.setImageDrawable(context.getDrawable(R.drawable.ic_music_note_png));
        }
        Drawable drawable = image.getDrawable();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
        Bitmap albumBitmap = bitmapDrawable.getBitmap();
        
        // NotificationActionsReceiver PendingIntent
        PendingIntent actionPrevReceiver = PendingIntent.getBroadcast(context, 0, new Intent(context, MusicService.NotificationActionsReceiver.class).setAction(MusicUtils.ACTION_PREV), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent actionPlayPauseReceiver = PendingIntent.getBroadcast(context, 0, new Intent(context, MusicService.NotificationActionsReceiver.class).setAction(MusicUtils.ACTION_PLAY_PAUSE), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent actionNextReceiver = PendingIntent.getBroadcast(context, 0, new Intent(context, MusicService.NotificationActionsReceiver.class).setAction(MusicUtils.ACTION_NEXT), PendingIntent.FLAG_UPDATE_CURRENT);
        
        int playPause = 0;
        NotificationCompat.Action skipPrevAction = new NotificationCompat.Action.Builder(R.drawable.ic_skip_previous_outline, MusicUtils.ACTION_PREV, actionPrevReceiver).build();
        if(mediaSession.getPlayer().isPlaying()) {
        	playPause = R.drawable.ic_pause_outline;
        }
        else {
            playPause = R.drawable.ic_play_outline;
        }
        NotificationCompat.Action playPauseAction = new NotificationCompat.Action.Builder(playPause, MusicUtils.ACTION_PLAY_PAUSE, actionPlayPauseReceiver).build();
        NotificationCompat.Action skipNextAction = new NotificationCompat.Action.Builder(R.drawable.ic_skip_next_outline, MusicUtils.ACTION_NEXT, actionNextReceiver).build();
        
        mediaStyleNotification = new NotificationCompat.Builder(context, BaseApplication.CHANNEL_ID)
        .setContentTitle(mediaSession.getPlayer().getCurrentMediaItem().mediaMetadata.title)
        .setContentText(mediaSession.getPlayer().getCurrentMediaItem().mediaMetadata.artist)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setLargeIcon(albumBitmap)
        .setSmallIcon(R.drawable.ic_music_note_outline)
        .setAutoCancel(ongoing)
        .setOngoing(ongoing)
        .setCategory(NotificationCompat.CATEGORY_SERVICE)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .addAction(skipPrevAction)
        .addAction(playPauseAction)
        .addAction(skipNextAction)
        .setStyle(mediaStyle);
    }
    
    public Notification getMediaStyleNotification(Context context, MediaSession mediaSession, boolean ongoing){
        setMediaStyleNotification(context, mediaSession, ongoing);
        setContentIntent(mediaSession.getSessionActivity());
        return mediaStyleNotification.build();
    }
    
    private void setContentIntent(PendingIntent intent) {
    	mediaStyleNotification.setContentIntent(intent);
    }
    
}
