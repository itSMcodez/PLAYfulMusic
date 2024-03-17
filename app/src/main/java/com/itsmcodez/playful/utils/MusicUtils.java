package com.itsmcodez.playful.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadata;
import android.net.Uri;
import android.content.ContentUris;
import android.os.PowerManager;
import android.util.Log;
import android.widget.ImageView;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itsmcodez.playful.models.PlaylistSongsModel;
import com.itsmcodez.playful.models.PlaylistsModel;
import com.itsmcodez.playful.models.SongsModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class MusicUtils {
    public static final String TAG = "MusicUtils";
    public static final String ARGS = "com.itsmcodez.playful." + TAG;
    public static final String ACTION_PREV = "ACTION_PREV";
    public static final String ACTION_PLAY_PAUSE = "ACTION_PLAY_PAUSE";
    public static final String ACTION_NEXT = "ACTION_NEXT";
    public static final int BIND_AUTO_CREATE = 1;
    public static final int PLAYER_WAKELOCK = PowerManager.PARTIAL_WAKE_LOCK;
    public static final AudioAttributes PLAYER_ATTRIBUTES = new AudioAttributes.Builder()
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build();
    private static List<MediaItem> mediaItems;
    
    static{
        AppUtils.addRegistrar(MusicUtils.getRegistrar());
        mediaItems = new ArrayList<>();
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
        long _albumId = Long.parseLong(albumId);
        Uri albumArtwork = ContentUris.withAppendedId(albumPath, _albumId);
        ImageView album = new ImageView(context);
        album.setImageURI(albumArtwork);
        Drawable albumDrawable = album.getDrawable();
        getRegistrar();
        return albumDrawable;
    }
    
    public static void setMediaItems(List<MediaItem> mediaItems){
        MusicUtils.mediaItems = mediaItems;
    }
    
    public static List<MediaItem> getMediaItems(){
        return MusicUtils.mediaItems;
    }
    
    public static void addMediaItem(MediaItem mediaItem, int position){
        mediaItems.add(position, mediaItem);
    }
    
    public static void addMediaItem(MediaItem mediaItem){
        mediaItems.add(mediaItem);
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
    
    public static ArrayList<PlaylistsModel> getAllPlaylists(Context context){
        ArrayList<PlaylistsModel> playlists = new ArrayList<>();
        final int MODE_PRIVATE = 0;
        SharedPreferences sharedPref = context.getSharedPreferences("playlists", MODE_PRIVATE);
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
        playlists.add(new PlaylistsModel(playlistTitle, "0 Songs", "00:00", "0"));
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
    
    public static void renamePlaylist(Application application, String newName, int position) {
        
    	ArrayList<PlaylistsModel> playlists = getAllPlaylists(application);
        playlists.get(position).setTitle(newName);
        final int MODE_PRIVATE = 0;
        SharedPreferences sharedPref = application.getSharedPreferences("playlists", MODE_PRIVATE);
        Gson gson = new Gson();
        String allPlaylists = gson.toJson(playlists);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString("all_playlists", allPlaylists);
        prefEditor.apply();
        
        getRegistrar();
    }
    
    public static void addSongToPlaylist(Application application, PlaylistSongsModel song, int position){
        
        ArrayList<PlaylistsModel> playlists = getAllPlaylists(application);
        ArrayList<PlaylistSongsModel> playlistSongs = playlists.get(position).getSongs();
        playlistSongs.add(song);
        playlists.get(position).setSongs(playlistSongs);
        playlists.get(position).setSongsCount(String.valueOf(playlistSongs.size()));
        
        // Sum up durations
        var sum = 0L;
        var duration = 0L;
        for(PlaylistSongsModel _song : playlistSongs){
            duration = sum + Long.parseLong(_song.getDuration());
            sum = sum + Long.parseLong(_song.getDuration());
        }
        playlists.get(position).setSongsDuration(MusicUtils.getFormattedTime(duration));
        
        final int MODE_PRIVATE = 0;
        SharedPreferences sharedPref = application.getSharedPreferences("playlists", MODE_PRIVATE);
        Gson gson = new Gson();
        String allPlaylists = gson.toJson(playlists);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString("all_playlists", allPlaylists);
        prefEditor.apply();
        
        getRegistrar();
    }
    
    public static void addSongToPlaylist(Context context, PlaylistSongsModel song, int position){
        
        ArrayList<PlaylistsModel> playlists = getAllPlaylists(context);
        ArrayList<PlaylistSongsModel> playlistSongs = playlists.get(position).getSongs();
        playlistSongs.add(song);
        playlists.get(position).setSongs(playlistSongs);
        playlists.get(position).setSongsCount(String.valueOf(playlistSongs.size()));
        
        // Sum up durations
        var sum = 0L;
        var duration = 0L;
        for(PlaylistSongsModel _song : playlistSongs){
            duration = sum + Long.parseLong(_song.getDuration());
            sum = sum + Long.parseLong(_song.getDuration());
        }
        playlists.get(position).setSongsDuration(MusicUtils.getFormattedTime(duration));
        
        final int MODE_PRIVATE = 0;
        SharedPreferences sharedPref = context.getSharedPreferences("playlists", MODE_PRIVATE);
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
