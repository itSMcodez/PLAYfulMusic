package com.itsmcodez.playful.repositories;
import android.app.Application;
import androidx.lifecycle.MutableLiveData;
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
    
    public static synchronized PlaylistsRepository getInstance(Application application) {
        PlaylistsRepository.application = application;
        
    	if(instance == null){
            instance = new PlaylistsRepository();
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
}
