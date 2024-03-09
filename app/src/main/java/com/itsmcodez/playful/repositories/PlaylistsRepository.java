package com.itsmcodez.playful.repositories;
import android.app.Application;
import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import com.itsmcodez.playful.models.PlaylistSongsModel;
import com.itsmcodez.playful.models.PlaylistsModel;
import com.itsmcodez.playful.utils.MusicUtils;
import java.util.ArrayList;

public class PlaylistsRepository {
    private static PlaylistsRepository instance;
    private static Application application;
    private ArrayList<PlaylistsModel> playlists;
    private MutableLiveData<ArrayList<PlaylistsModel>> allPlaylists;
    
    public PlaylistsRepository(){
        playlists = MusicUtils.getAllPlaylists(application);
        allPlaylists = new MutableLiveData<>(playlists);
    }
    
    public PlaylistsRepository(Context context){
        playlists = MusicUtils.getAllPlaylists(context);
        allPlaylists = new MutableLiveData<>(playlists);
    }
    
    public static synchronized PlaylistsRepository getInstance(Application application) {
        PlaylistsRepository.application = application;
        
    	if(instance == null){
            instance = new PlaylistsRepository();
        }
        return instance;
    }
    
    public static synchronized PlaylistsRepository getInstance(Context context) {
        
    	if(instance == null){
            instance = new PlaylistsRepository(context);
        }
        return instance;
    }
    
    public synchronized ArrayList<PlaylistsModel> getPlaylists(){
        return this.playlists;
    }
    
    public synchronized MutableLiveData<ArrayList<PlaylistsModel>> getAllPlaylists(){
        return this.allPlaylists;
    }
    
    public synchronized void addPlaylist(String playlistTitle){
        MusicUtils.addPlaylist(application, playlistTitle);
        playlists = MusicUtils.getAllPlaylists(application);
        allPlaylists = new MutableLiveData<>(playlists);
    }
    
    public synchronized void deletePlaylist(int position){
        MusicUtils.deletePlaylist(application, position);
        playlists = MusicUtils.getAllPlaylists(application);
        allPlaylists = new MutableLiveData<>(playlists);
    }
    
    public synchronized void renamePlaylist(String newName, int position){
        MusicUtils.renamePlaylist(application, newName, position);
        playlists = MusicUtils.getAllPlaylists(application);
        allPlaylists = new MutableLiveData<>(playlists);
    }
    
    public synchronized void addSongToPlaylist(PlaylistSongsModel song, int position){
        MusicUtils.addSongToPlaylist(application, song, position);
        playlists = MusicUtils.getAllPlaylists(application);
        allPlaylists = new MutableLiveData<>(playlists);
    }
    
    public synchronized void addSongToPlaylist(Context context, PlaylistSongsModel song, int position){
        MusicUtils.addSongToPlaylist(context, song, position);
        playlists = MusicUtils.getAllPlaylists(context);
        allPlaylists = new MutableLiveData<>(playlists);
    }
}
