package com.itsmcodez.playful;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.itsmcodez.playful.databinding.ActivityAlbumArtistBinding;

public class AlbumArtistActivity extends AppCompatActivity {
    private ActivityAlbumArtistBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind to views
        binding = ActivityAlbumArtistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        
    }
}
