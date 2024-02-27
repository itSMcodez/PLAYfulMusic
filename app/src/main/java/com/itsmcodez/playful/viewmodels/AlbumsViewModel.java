package com.itsmcodez.playful.viewmodels;
import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.itsmcodez.playful.models.AlbumsModel;
import com.itsmcodez.playful.repositories.AlbumsRepository;
import java.util.ArrayList;

public class AlbumsViewModel extends AndroidViewModel {
    private AlbumsRepository albumsRepository;
    private MutableLiveData<ArrayList<AlbumsModel>> allAlbums;
    
    public AlbumsViewModel(Application application){
        super(application);
        albumsRepository = AlbumsRepository.getInstance(application);
        allAlbums = albumsRepository.getAllAlbums();
    }
    
    public MutableLiveData<ArrayList<AlbumsModel>> getAllAlbums(){
        return this.allAlbums;
    }
}
