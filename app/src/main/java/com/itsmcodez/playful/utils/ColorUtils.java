package com.itsmcodez.playful.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import androidx.palette.graphics.Palette;

public class ColorUtils {
    public static final String TAG = "ColorUtils";
    public static final String ARGS = "com.itsmcodez.playful." + TAG;
    private static Palette extractedColorsPalette;
    
    static{
        AppUtils.addRegistrar(ColorUtils.getRegistrar());
    }
    
    public static Palette extractColorsFromImage(String path){
        MediaMetadataRetriever imageRetriever = new MediaMetadataRetriever();
        imageRetriever.setDataSource(path);
        byte[] imgBytes = imageRetriever.getEmbeddedPicture();
        try{
            imageRetriever.release();
        }catch(Exception e){e.printStackTrace();}
        
        Bitmap albumBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
        
        ColorUtils.extractedColorsPalette = Palette.from(albumBitmap).generate();
        
        getRegistrar();
        
        return ColorUtils.extractedColorsPalette;
    }
    
    public static Palette extractColorsFromImage(Context context, String albumId){
        Drawable albumDrawable = MusicUtils.getAlbumArtworkDrawable(context, albumId);
        BitmapDrawable albumBitmapDrawable = (BitmapDrawable) albumDrawable;
        Bitmap albumBitmap = albumBitmapDrawable.getBitmap();
        
        ColorUtils.extractedColorsPalette = Palette.from(albumBitmap).generate();
        
        getRegistrar();
        
        return ColorUtils.extractedColorsPalette;
    }
    
    public static String getRegistrar(){
        Log.i(TAG, "Accessing registrar...");
        return ColorUtils.ARGS;
    }
}
