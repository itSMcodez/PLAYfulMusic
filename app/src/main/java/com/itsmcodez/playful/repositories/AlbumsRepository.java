package com.itsmcodez.playful.repositories;
import android.app.Application;
import android.net.Uri;
import android.database.Cursor;
import android.content.ContentUris;
import android.provider.MediaStore;
import com.itsmcodez.playful.models.SongsModel;
import androidx.lifecycle.MutableLiveData;
import com.itsmcodez.playful.models.AlbumsModel;
import java.util.ArrayList;

public class AlbumsRepository {
    private static AlbumsRepository instance;
    private static Application application; 
    private ArrayList<AlbumsModel> albums;
    private MutableLiveData<ArrayList<AlbumsModel>> allAlbums;
    
    public AlbumsRepository(){
        
        // initialize albums
        albums = new ArrayList<>();
        
        Uri albumsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        
        String[] projection = {MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ALBUM_ID};
        
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.DATE_ADDED;
        
        Cursor cursor = application.getContentResolver().query(albumsUri, projection, selection, null, sortOrder);
        
        if(cursor.moveToFirst()){
            
            do{
                
                String album = cursor.getString(0);
                String albumId = cursor.getString(1);
                
                Uri albumPath = Uri.parse("content://media/external/audio/albumart");
                Uri albumArtwork = ContentUris.withAppendedId(albumPath, Integer.parseInt(albumId));
                
                albums.add(new AlbumsModel(album, albumId, albumArtwork));
                
            } while(cursor.moveToNext());
            cursor.close();
            
            // assign allAlbums to albums
            allAlbums = new MutableLiveData<>(albums);
        }
    }
    
    public static synchronized AlbumsRepository getInstance(Application application){
        AlbumsRepository.application = application;
        
        if(instance == null){
            instance = new AlbumsRepository();
        }
        
        return instance;
    }
    
    public synchronized ArrayList<AlbumsModel> getAlbums(){
        return this.albums;
    }
    
    public synchronized MutableLiveData<ArrayList<AlbumsModel>> getAllAlbums(){
        return this.allAlbums;
    }
}
