package com.itsmcodez.playful.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadata;
import android.net.Uri;
import android.content.ContentUris;
import android.util.Log;
import android.widget.ImageView;
import com.itsmcodez.playful.models.SongsModel;
import java.util.Locale;

public final class MusicUtils {
    public static final String TAG = "MusicUtils";
    public static final String ARGS = "com.itsmcodez.playful." + TAG;
    
    static{
        AppUtils.addRegistrar(MusicUtils.getRegistrar());
    }
    
    public MusicUtils(){}
    
    public static String getFormattedTime(long duration){
        
        String songTime = "";
        var toMin = duration / 1000 / 60;
        var toSec = duration / 1000 % 60;
        
        if(toMin < 60){
            songTime = String.format(Locale.getDefault(), "%02d:%02d", toMin, toSec);
        }
        else{
            var toHr = toMin / 60;
            toMin %= 60;
            songTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", toHr, toMin, toSec);
        }
        getRegistrar();
        return songTime;
    }
    
    public static Drawable getAlbumArtworkDrawable(Context context, String albumId){
        Uri albumPath = Uri.parse("content://media/external/audio/albumart");
        Uri albumArtwork = ContentUris.withAppendedId(albumPath, Integer.parseInt(albumId));
        ImageView album = new ImageView(context);
        album.setImageURI(albumArtwork);
        Drawable albumDrawable = album.getDrawable();
        getRegistrar();
        return albumDrawable;
    }
    
    public static String getRegistrar(){
        Log.i(TAG, "Accessing registrar...");
        return MusicUtils.ARGS;
    }
}
