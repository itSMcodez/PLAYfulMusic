package com.itsmcodez.playful;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.exoplayer.ExoPlayer;
import com.itsmcodez.playful.databinding.ActivityPlayerBinding;
import com.itsmcodez.playful.services.MusicService;
import com.itsmcodez.playful.utils.MusicUtils;

public class PlayerActivity extends AppCompatActivity implements ServiceConnection {
    private ActivityPlayerBinding binding;
    private MusicService musicService;
    private boolean isPlayerPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind to views
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        
        // Action Bar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
    
    @Override
    protected void onResume() {
        bindService(
                new Intent(PlayerActivity.this, MusicService.class),
                this,
                BIND_AUTO_CREATE);
        super.onResume();
    }
    
    @Override
    protected void onPause() {
        musicService.setStateHandler(null);
        unbindService(this);
        super.onPause();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MusicBinder binder = (MusicService.MusicBinder) iBinder;
        musicService = binder.getService();
        
        // Check if player is not null and set metadata to current media item if true
        if(musicService.getPlayer().getMediaItemCount() != 0 && musicService.getPlayer().getCurrentMediaItem() != null){
                // Metadata
                binding.songTitle.setText(musicService.getPlayer().getCurrentMediaItem().mediaMetadata.title);
                binding.songArtist.setText(musicService.getPlayer().getCurrentMediaItem().mediaMetadata.artist);
                binding.songDuration.setText(MusicUtils.getFormattedTime(musicService.getPlayer().getContentDuration()));
                binding.seekBar.setMax((int)musicService.getPlayer().getContentDuration());
                
                // Album artwork 
                binding.albumArtwork.setImageURI(musicService.getPlayer().getCurrentMediaItem().mediaMetadata.artworkUri);
                if(binding.albumArtwork.getDrawable() == null) {
                	binding.albumArtwork.setImageResource(R.drawable.ic_music_note);
                }
                binding.albumCover.setImageDrawable(binding.albumArtwork.getDrawable()); 
                
                if(musicService.getPlayer().isPlaying()) {
                	binding.playPause.setImageResource(R.drawable.ic_pause_circle_outline);
                } else {
                    binding.playPause.setImageResource(R.drawable.ic_play_circle_outline);
                }
            
                updateUIProgress();
        }
        
        binding.playPause.setOnClickListener(view -> {
                
                        // Return if there are no songs
                        if(musicService.getPlayer().getMediaItemCount() == 0) {
                        	return;
                        }
                
                        if(musicService.getPlayer().isPlaying()) {
                            musicService.getPlayer().stop();
                        } else {
                            musicService.getPlayer().prepare();
                            musicService.getPlayer().play();
                        }
                });
                
        binding.skipNext.setOnClickListener(view -> {
                        
                        if(musicService.getPlayer().isPlaying()) {
                            musicService.getPlayer().stop();
                        }
                    
                        if(musicService.getPlayer().getMediaItemCount() != 0) {
                        	if(musicService.getPlayer().hasNextMediaItem()) {
                                musicService.getPlayer().seekToNextMediaItem();
                            } 
                            else if(musicService.getPlayer().getMediaItemCount() == 1){
                                musicService.getPlayer().seekTo(0, 0); /* Replay same song again if media item is 1*/
                                musicService.getPlayer().prepare();    
                                musicService.getPlayer().play();     
                            }
                            else {
                                musicService.getPlayer().seekTo(0, 0);
                            }
                        }
                });
        
        binding.skipPrev.setOnClickListener(view -> {
                        
                        if(musicService.getPlayer().isPlaying()) {
                            musicService.getPlayer().stop();
                        }
                    
                        if(musicService.getPlayer().getMediaItemCount() != 0) {
                        	if(musicService.getPlayer().hasPreviousMediaItem()) {
                                musicService.getPlayer().seekToPreviousMediaItem();
                            } 
                            else if(musicService.getPlayer().getMediaItemCount() == 1){
                                musicService.getPlayer().seekTo(0, 0); /* Replay same song again if media item is 1*/
                                musicService.getPlayer().prepare();    
                                musicService.getPlayer().play();     
                            }
                            else {
                                musicService.getPlayer().seekTo(musicService.getPlayer().getMediaItemCount() - 1, 0);
                            }
                        }
                });
        
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    
                }
                
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    
                }
                
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser) {
                    	musicService.getPlayer().seekTo(progress);
                    }
                }
                
        });
        
        musicService.setStateHandler(() -> {
                
                if(musicService.getPlayer().getMediaItemCount() != 0 && musicService.getPlayer().getCurrentMediaItem() != null){
                    // Metadata
                    binding.songTitle.setText(musicService.getPlayer().getCurrentMediaItem().mediaMetadata.title);
                    binding.songArtist.setText(musicService.getPlayer().getCurrentMediaItem().mediaMetadata.artist);
                    if(musicService.getPlayer().getPlaybackState() == ExoPlayer.STATE_READY){
                        binding.songDuration.setText(MusicUtils.getFormattedTime(musicService.getPlayer().getContentDuration()));  
                        binding.seekBar.setMax((int)musicService.getPlayer().getContentDuration());
                    }
                
                    // Album artwork 
                    binding.albumArtwork.setImageURI(musicService.getPlayer().getCurrentMediaItem().mediaMetadata.artworkUri);
                    if(binding.albumArtwork.getDrawable() == null) {
                        binding.albumArtwork.setImageResource(R.drawable.ic_music_note);
                    }
                    binding.albumCover.setImageDrawable(binding.albumArtwork.getDrawable());    
                
                    if(musicService.getPlayer().isPlaying()) {
                        binding.playPause.setImageResource(R.drawable.ic_pause_circle_outline);
                    } else {
                        binding.playPause.setImageResource(R.drawable.ic_play_circle_outline);
                    }
                    
                    updateUIProgress();
                }
        });
        
        // Set Content View
        setContentView(binding.getRoot());
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService.setStateHandler(null);
        musicService = null;
    }
    
    public void updateUIProgress() {
        if(musicService != null) {
        	
            new Handler().postDelayed(() -> {
                
                if(musicService.getPlayer().isPlaying()) {
                	binding.songCurrentDuration.setText(MusicUtils.getFormattedTime(musicService.getPlayer().getContentPosition())); 
                    binding.seekBar.setProgress((int) musicService.getPlayer().getContentPosition());
                }
                
            updateUIProgress();
            }, 1000);
        }
    }
}
