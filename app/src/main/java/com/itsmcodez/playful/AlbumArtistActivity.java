package com.itsmcodez.playful;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.itsmcodez.playful.adapters.SongsAdapter;
import com.itsmcodez.playful.databinding.ActivityAlbumArtistBinding;
import com.itsmcodez.playful.models.SongsModel;
import com.itsmcodez.playful.services.MusicService;
import com.itsmcodez.playful.utils.ColorUtils;
import com.itsmcodez.playful.utils.MusicUtils;
import com.itsmcodez.playful.viewmodels.SongsViewModel;
import java.util.ArrayList;
import java.util.Locale;

public class AlbumArtistActivity extends AppCompatActivity implements ServiceConnection {
    private ActivityAlbumArtistBinding binding;
    private SongsViewModel songsViewModel;
    private SongsAdapter songsAdapter;
    private MusicService musicService;

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

        // Play all button onClick
        binding.playAllBt.setOnClickListener(
                view -> {
                    if (!musicService.isStarted) {
                        startService(new Intent(AlbumArtistActivity.this, MusicService.class));
                    }

                    if (musicService.getPlayer().isPlaying()) {
                        musicService.getPlayer().stop();
                    }
                
                    musicService.getPlayer().setShuffleModeEnabled(false);
                    musicService.getPlayer().setMediaItems(MusicUtils.getMediaItems(), 0, 0);
                
                    startActivity(new Intent(AlbumArtistActivity.this, PlayerActivity.class));
                });
        
        binding.shuffleAllBt.setOnClickListener(view -> {
                    if (!musicService.isStarted) {
                        startService(new Intent(AlbumArtistActivity.this, MusicService.class));
                    }

                    if (musicService.getPlayer().isPlaying()) {
                        musicService.getPlayer().stop();
                    }
                
                    musicService.getPlayer().setShuffleModeEnabled(true);
                    musicService.getPlayer().setMediaItems(MusicUtils.getMediaItems(), 0, 0);
                
                    startActivity(new Intent(AlbumArtistActivity.this, PlayerActivity.class));
        });
        
                

        // ViewModel
        songsViewModel = new ViewModelProvider(AlbumArtistActivity.this).get(SongsViewModel.class);

        // Receive data
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String albumId = intent.getStringExtra("albumId");
            
            // Extract colors from album artwork 
            int color = Color.parseColor("#ff64b5f6");
            BitmapDrawable bitmapDrawable;
            Bitmap albumBitmap;
            Drawable drawable = MusicUtils.getAlbumArtworkDrawable(AlbumArtistActivity.this, albumId);
            ImageView albumImage = new ImageView(AlbumArtistActivity.this);
            albumImage.setImageDrawable(drawable);
            if(albumImage.getDrawable() == null){
                albumImage.setImageDrawable(getDrawable(R.drawable.ic_music));
            }
            if( !( albumImage.getDrawable() instanceof VectorDrawable ) ) {
                bitmapDrawable = (BitmapDrawable) albumImage.getDrawable();
                albumBitmap = bitmapDrawable.getBitmap();
                color = ColorUtils.getColor(ColorUtils.generatePalette(albumBitmap), 0xff64b5f6);
            }
            
            // Design
            ColorStateList colorIn = ColorStateList.valueOf(color);
            binding.collapsingToolbar.setExpandedTitleColor(color);
            binding.playAllBt.setIconTint(colorIn);
            binding.playAllBt.setIconTintMode(PorterDuff.Mode.SRC_IN);
            binding.playAllBt.setStrokeColor(colorIn);
            binding.playAllBt.setTextColor(color);
            binding.shuffleAllBt.setBackgroundColor(color);
            
            // Album artwork
            Drawable albumArtwork = MusicUtils.getAlbumArtworkDrawable(AlbumArtistActivity.this, albumId);
            if(albumArtwork == null) {
                albumArtwork = getDrawable(R.drawable.ic_music);
                albumArtwork.setColorFilter(0xff64b5f6, PorterDuff.Mode.SRC_IN);
            }
            binding.collapsingToolbar.setBackground(albumArtwork);
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
                                    var sum = 0L;
                                    var duration = 0L;
                        
                                    // Filter
                                    ArrayList<SongsModel> filteredSongs = new ArrayList<>();
                        
                                    if(intent.getStringExtra("displaySongs").equals("fromAlbum")){
                            
                                        for(SongsModel song : allSongs){
                                            if(song.getAlbum().equals(title)){
                                                filteredSongs.add(song);
                                                duration = sum + Long.parseLong(song.getDuration());
                                                sum = sum + Long.parseLong(song.getDuration());
                                            }
                                        }
                                    }
                                    else if(intent.getStringExtra("displaySongs").equals("fromArtist")){
                                        
                                        for(SongsModel song : allSongs){
                                            if(song.getArtist().equals(title)){
                                                filteredSongs.add(song);
                                                duration = sum + Long.parseLong(song.getDuration());
                                                sum = sum + Long.parseLong(song.getDuration());
                                            }
                                        }
                                    }
                        
                                    // Set media items
                                    MusicUtils.setMediaItems(getMediaItems(filteredSongs));
                        
                                    songsAdapter = new SongsAdapter(AlbumArtistActivity.this, getLayoutInflater(), filteredSongs);
                                    binding.recyclerView.setAdapter(songsAdapter);
                                    
                                    if(binding.recyclerView.getAdapter().getItemCount() == 1){
                                        binding.info.setText(binding.recyclerView.getAdapter().getItemCount() + " Song • " + MusicUtils.getFormattedTime(duration) );
                                    }
                                    else{
                                        binding.info.setText(binding.recyclerView.getAdapter().getItemCount() + " Songs • " + MusicUtils.getFormattedTime(duration) );
                                    }
                        
                                    songsAdapter.setOnClickEvents(
                                        new SongsAdapter.OnClickEvents() {
                                            @Override
                                            public void onItemClick(
                                                    View view, SongsModel song, int position) {
                                    
                                                        // Set media items
                                                        MusicUtils.setMediaItems(getMediaItems(filteredSongs));
                                
                                                        if(!musicService.isStarted){
                                                            startService(new Intent(AlbumArtistActivity.this, MusicService.class));
                                                        }
                                
                                                        if(musicService.getPlayer().isPlaying()){
                                                            musicService.getPlayer().stop();
                                                        }
                                    
                                                        musicService.getPlayer().setMediaItems(MusicUtils.getMediaItems(), position, 0);
                                    
                                                        startActivity(new Intent(AlbumArtistActivity.this, PlayerActivity.class));
                                                    }

                                            @Override
                                            public boolean onItemLongClick(
                                                    View view, SongsModel song, int position) {
                                                        return false;
                                                    }
                                        });
                                }
                            });
        }
    }
    
    @Override
    protected void onResume() {
        bindService(new Intent(AlbumArtistActivity.this, MusicService.class), this, BIND_AUTO_CREATE);
        super.onResume();
    }
    
    @Override
    protected void onPause() {
        unbindService(this);
        super.onPause();
    }
    
    public ArrayList<MediaItem> getMediaItems(ArrayList<SongsModel> songs) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        for (SongsModel song : songs) {
            
            MediaMetadata mediaMetadata = new MediaMetadata.Builder()
                                            .setTitle(song.getTitle())
                                            .setAlbumTitle(song.getAlbum())
                                            .setArtworkUri(song.getAlbumArtwork())
                                            .setArtist(song.getArtist())
                                            .build();
            
            MediaItem mediaItem =
                    new MediaItem.Builder()
                            .setUri(song.getPath())
                            .setMediaMetadata(mediaMetadata)
                            .build();

            mediaItems.add(mediaItem);
        }

        return mediaItems;
    }
    
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MusicBinder binder = (MusicService.MusicBinder) iBinder;
        musicService = binder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
    }
}
