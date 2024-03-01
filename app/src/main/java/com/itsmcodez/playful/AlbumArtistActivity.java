package com.itsmcodez.playful;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.itsmcodez.playful.adapters.SongsAdapter;
import com.itsmcodez.playful.databinding.ActivityAlbumArtistBinding;
import com.itsmcodez.playful.models.SongsModel;
import com.itsmcodez.playful.utils.MusicUtils;
import com.itsmcodez.playful.viewmodels.SongsViewModel;
import java.util.ArrayList;
import java.util.Locale;

public class AlbumArtistActivity extends AppCompatActivity {
    private ActivityAlbumArtistBinding binding;
    private SongsViewModel songsViewModel;
    private SongsAdapter songsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind to views
        binding = ActivityAlbumArtistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ActionBar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // RecyclerView
        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        // ViewModel
        songsViewModel = new ViewModelProvider(AlbumArtistActivity.this).get(SongsViewModel.class);

        // Receive data
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String albumId = intent.getStringExtra("albumId");
            
            // Album artwork
            binding.collapsingToolbar.setBackground(MusicUtils.getAlbumArtworkDrawable(AlbumArtistActivity.this, albumId));
            binding.collapsingToolbar.setTitle(title);
            
            // Metadata
            binding.title.setText(title);

            // Observe LiveData (songsViewModel)
            songsViewModel
                    .getAllSongs()
                    .observe(
                            this,
                            new Observer<ArrayList<SongsModel>>() {
                                @Override
                                public void onChanged(ArrayList<SongsModel> allSongs) {
                                    
                                    // allSongs duration
                                    var sum = 0;
                                    var duration = 0;
                        
                                    // Filter
                                    ArrayList<SongsModel> filteredSongs = new ArrayList<>();
                        
                                    if(intent.getStringExtra("displaySongs").equals("fromAlbum")){
                            
                                        for(SongsModel song : allSongs){
                                            if(song.getAlbum().equals(title)){
                                                filteredSongs.add(song);
                                                duration = sum + Integer.parseInt(song.getDuration());
                                                sum = sum + Integer.parseInt(song.getDuration());
                                            }
                                        }
                                    }
                                    else if(intent.getStringExtra("displaySongs").equals("fromArtist")){
                                        
                                        for(SongsModel song : allSongs){
                                            if(song.getArtist().equals(title)){
                                                filteredSongs.add(song);
                                                duration = sum + Integer.parseInt(song.getDuration());
                                                sum = sum + Integer.parseInt(song.getDuration());
                                            }
                                        }
                                    }
                                    
                        
                                    songsAdapter = new SongsAdapter(AlbumArtistActivity.this, getLayoutInflater(), filteredSongs);
                                    binding.recyclerView.setAdapter(songsAdapter);
                                    
                                    if(binding.recyclerView.getAdapter().getItemCount() == 1){
                                        binding.info.setText(binding.recyclerView.getAdapter().getItemCount() + " Song • " + MusicUtils.getFormattedTime(duration) );
                                    }
                                    else{
                                        binding.info.setText(binding.recyclerView.getAdapter().getItemCount() + " Songs • " + MusicUtils.getFormattedTime(duration) );
                                    }
                                }
                            });
        }
    }
}
