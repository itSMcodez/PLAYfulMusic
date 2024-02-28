package com.itsmcodez.playful.viewmodels;
import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.itsmcodez.playful.models.ArtistsModel;
import com.itsmcodez.playful.repositories.ArtistsRepository;
import java.util.ArrayList;

public class ArtistsViewModel extends AndroidViewModel {
    private ArtistsRepository artistsRepository;
    private MutableLiveData<ArrayList<ArtistsModel>> allArtists;
    
    public ArtistsViewModel(Application application){
        super(application);
        artistsRepository = ArtistsRepository.getInstance(application);
        allArtists = artistsRepository.getAllArtists();
    }
    
    public MutableLiveData<ArrayList<ArtistsModel>> getAllArtists(){
        return this.allArtists;
    }
}
