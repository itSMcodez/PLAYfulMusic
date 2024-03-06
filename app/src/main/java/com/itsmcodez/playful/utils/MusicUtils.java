package com.itsmcodez.playful.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadata;
import android.net.Uri;
import android.content.ContentUris;
import android.util.Log;
import android.widget.ImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itsmcodez.playful.models.PlaylistsModel;
import com.itsmcodez.playful.models.SongsModel;
import java.util.ArrayList;
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
    
    public static ArrayList<PlaylistsModel> getAllPlaylists(Application application){
        
        ArrayList<PlaylistsModel> playlists = new ArrayList<>();
        final int MODE_PRIVATE = 0;
        SharedPreferences sharedPref = application.getSharedPreferences("playlists", MODE_PRIVATE);
        String allPlaylists = sharedPref.getString("all_playlists", "");
        
        if(!allPlaylists.equals("")){
            Gson gson = new Gson();
            TypeToken<ArrayList<PlaylistsModel>> playlistToken = new TypeToken<ArrayList<PlaylistsModel>>(){};
            playlists = gson.fromJson(allPlaylists, playlistToken.getType());
        }
        
        getRegistrar();
        return playlists;
    }
    
    public static void addPlaylist(Application application, String playlistTitle){
        
        ArrayList<PlaylistsModel> playlists = getAllPlaylists(application);
        playlists.add(new PlaylistsModel(playlistTitle, "0 Songs", "00:00", "0", null));
        final int MODE_PRIVATE = 0;
        SharedPreferences sharedPref = application.getSharedPreferences("playlists", MODE_PRIVATE);
        Gson gson = new Gson();
        String allPlaylists = gson.toJson(playlists);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString("all_playlists", allPlaylists);
        prefEditor.apply();
        
        getRegistrar();
    }
    
    public static void deletePlaylist(Application application, int position) {
        
    	ArrayList<PlaylistsModel> playlists = getAllPlaylists(application);
        playlists.remove(position);
        final int MODE_PRIVATE = 0;
        SharedPreferences sharedPref = application.getSharedPreferences("playlists", MODE_PRIVATE);
        Gson gson = new Gson();
        String allPlaylists = gson.toJson(playlists);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString("all_playlists", allPlaylists);
        prefEditor.apply();
        
        getRegistrar();
    }
    
    public static String getRegistrar(){
        Log.i(TAG, "Accessing registrar...");
        return MusicUtils.ARGS;
    }
}
