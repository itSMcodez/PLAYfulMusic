package com.itsmcodez.playful.repositories;
import android.app.Application;
import android.net.Uri;
import android.database.Cursor;
import android.content.ContentUris;
import com.itsmcodez.playful.models.AlbumsModel;
import android.provider.MediaStore;
import androidx.lifecycle.MutableLiveData;
import com.itsmcodez.playful.models.ArtistsModel;
import java.util.ArrayList;

public class ArtistsRepository {
    private static ArtistsRepository instance;
    private static Application application;
    private ArrayList<ArtistsModel> artists;
    private MutableLiveData<ArrayList<ArtistsModel>> allArtists;
    
    public ArtistsRepository(){
        
        ArrayList<ArtistsModel> base = new ArrayList<>();
        
        Uri artistsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        
        String[] projection = {MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM_ID};
        
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.DATE_ADDED;
        
        Cursor cursor = application.getContentResolver().query(artistsUri, projection, selection, null, sortOrder);
        
        if(cursor.moveToFirst()){
            
            do{
                
                String artist = cursor.getString(0);
                String albumId = cursor.getString(1);
                
                Uri albumPath = Uri.parse("content://media/external/audio/albumart");
                Uri albumArtwork = ContentUris.withAppendedId(albumPath, Integer.parseInt(albumId));
                
                base.add(new ArtistsModel(artist, albumId, albumArtwork));
                
            } while(cursor.moveToNext());
            cursor.close();
            
            // assign allAlbums to albums
            artists = removeDuplicates(base);
            allArtists = new MutableLiveData<>(artists);
        }
    }
    
    public static ArrayList<ArtistsModel> removeDuplicates(ArrayList<ArtistsModel> artists) {
        ArrayList<ArtistsModel> filteredList = new ArrayList<>();
        
        // filter
        ArrayList<String> duplicate = new ArrayList<>();
        for(ArtistsModel artist : artists){
            
            if(duplicate.size() == 0){
                duplicate.add(artist.getArtist());
            }
            else{
                if(duplicate.contains(artist.getArtist())){
                    duplicate.add(artist.getArtist());
                }
                else{
                    filteredList.add(artist);
                    duplicate.add(artist.getArtist());
                }
            }
            
        }

        return filteredList;
    } 
    
    public static synchronized ArtistsRepository getInstance(Application application){
        ArtistsRepository.application = application;
        
        if(instance == null){
            instance = new ArtistsRepository();
        }
        
        return instance;
    }
    
    public synchronized ArrayList<ArtistsModel> getArtists(){
        return this.artists;
    }
    
    public synchronized MutableLiveData<ArrayList<ArtistsModel>> getAllArtists(){
        return this.allArtists;
    }
}
