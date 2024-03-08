package com.itsmcodez.playful.viewmodels;
import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.itsmcodez.playful.models.PlaylistSongsModel;
import com.itsmcodez.playful.models.PlaylistsModel;
import com.itsmcodez.playful.repositories.PlaylistsRepository;
import com.itsmcodez.playful.utils.MusicUtils;
import java.util.ArrayList;

public class PlaylistsViewModel extends AndroidViewModel {
    private PlaylistsRepository playlistsRepository;
    private MutableLiveData<ArrayList<PlaylistsModel>> allPlaylists;
    private ArrayList<PlaylistsModel> playlists;
    private static Application application;
    public PlaylistsViewModel(Application application){
        super(application);
        PlaylistsViewModel.application = application;
        playlistsRepository = PlaylistsRepository.getInstance(application);
        allPlaylists = playlistsRepository.getAllPlaylists();
        playlists = playlistsRepository.getPlaylists();
    }
    
    public MutableLiveData<ArrayList<PlaylistsModel>> getAllPlaylists(){
        return this.allPlaylists;
    }
    
    public ArrayList<PlaylistsModel> getPlaylists(){
        return this.playlists;
    }
    
    public void addPlaylist(String playlistTitle){
        playlistsRepository.addPlaylist(playlistTitle);
        allPlaylists.setValue(MusicUtils.getAllPlaylists(application));
    }
    
    public void deletePlaylist(int position){
        playlistsRepository.deletePlaylist(position);
        allPlaylists.setValue(MusicUtils.getAllPlaylists(application));
    }
    
    public void addSongToPlaylist(PlaylistSongsModel song, int position){
        playlistsRepository.addSongToPlaylist(song, position);
        allPlaylists.setValue(MusicUtils.getAllPlaylists(application));
    }
}
