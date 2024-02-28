package com.itsmcodez.playful.viewmodels;
import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import com.itsmcodez.playful.models.SongsModel;
import com.itsmcodez.playful.repositories.SongsRepository;

public class SongsViewModel extends AndroidViewModel {
    private SongsRepository songsRepository;
    private MutableLiveData<ArrayList<SongsModel>> allSongs;
    
    public SongsViewModel(Application application){
        super(application);
        songsRepository = SongsRepository.getInstance(application);
        allSongs = songsRepository.getAllSongs();
    }
    
    public MutableLiveData<ArrayList<SongsModel>> getAllSongs(){
        return this.allSongs;
    }
}
