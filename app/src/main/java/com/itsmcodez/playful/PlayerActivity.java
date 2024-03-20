package com.itsmcodez.playful;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.media3.exoplayer.ExoPlayer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.itsmcodez.playful.databinding.ActivityPlayerBinding;
import com.itsmcodez.playful.databinding.LayoutNowPlayingQueueBinding;
import com.itsmcodez.playful.services.MusicService;
import com.itsmcodez.playful.utils.MusicUtils;

public class PlayerActivity extends AppCompatActivity implements ServiceConnection {
    private ActivityPlayerBinding binding;
    private MusicService musicService;
    private boolean isBindingNull = false;
    private boolean isPlayerPlaying = false;
    private Handler progressHandler;
    private int repeatMode = 0;
    private boolean inShuffleMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind to views
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        
        // Action Bar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        // Tooltips
        TooltipCompat.setTooltipText(binding.repeat, "Repeat");
        TooltipCompat.setTooltipText(binding.skipNext, "Next track");
        TooltipCompat.setTooltipText(binding.skipPrev, "Previous track");
        TooltipCompat.setTooltipText(binding.playPause, "Play/Pause track");
        TooltipCompat.setTooltipText(binding.shuffle, "Shuffle");
        
        // Player repeat mode and shuffle
        repeatMode = ExoPlayer.REPEAT_MODE_OFF;
        inShuffleMode = false;
        
        // UI progress handler
        progressHandler = new Handler(getMainLooper());
        
        // Now plating queue BottomSheet
        //initNowPlayingQueue();
    }

    private void initNowPlayingQueue() {
        
        LayoutNowPlayingQueueBinding binding = LayoutNowPlayingQueueBinding.inflate(getLayoutInflater());
        BottomSheetBehavior nowPlayingQueueLayout = BottomSheetBehavior.from(binding.getRoot());
        
        binding.header.setOnClickListener(view -> {
                if(nowPlayingQueueLayout.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    	nowPlayingQueueLayout.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                }
                else if(nowPlayingQueueLayout.getState() == BottomSheetBehavior.STATE_HALF_EXPANDED) {
                    nowPlayingQueueLayout.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else {
                    nowPlayingQueueLayout.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
        });
        
        binding.expansionStateImg.setOnClickListener(view -> {
                if(nowPlayingQueueLayout.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    	nowPlayingQueueLayout.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                }
                else if(nowPlayingQueueLayout.getState() == BottomSheetBehavior.STATE_HALF_EXPANDED) {
                    nowPlayingQueueLayout.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else {
                    nowPlayingQueueLayout.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
        });
        
        nowPlayingQueueLayout.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback(){
                
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    
                }
                
                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    binding.expansionStateImg.setRotation(slideOffset * 180);
                }
                
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isBindingNull) {
            isBindingNull = true;
        }
        this.binding = null;
        
        if (!isPlayerPlaying) stopService(new Intent(PlayerActivity.this, MusicService.class));
        
    }
    
    @Override
    protected void onResume() {
        bindService(
                new Intent(PlayerActivity.this, MusicService.class),
                this,
                BIND_AUTO_CREATE);
        isBindingNull = false;
        super.onResume();
    }
    
    @Override
    protected void onPause() {
        isBindingNull = true;
        if(musicService.getPlayer().isPlaying()) {
        	isPlayerPlaying = true;
        }
        else {
            isPlayerPlaying = false;
        }
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
                binding.songCurrentDuration.setText(MusicUtils.getFormattedTime(musicService.getPlayer().getContentPosition())); 
                binding.seekBar.setMax((int)musicService.getPlayer().getContentDuration());
                binding.seekBar.setProgress((int) musicService.getPlayer().getContentPosition());
                
                // Album artwork 
                binding.albumArtwork.setImageURI(musicService.getPlayer().getCurrentMediaItem().mediaMetadata.artworkUri);
                if(binding.albumArtwork.getDrawable() == null) {
                	binding.albumArtwork.setImageResource(R.drawable.ic_music_note);
                }
                binding.albumCover.setImageDrawable(binding.albumArtwork.getDrawable()); 
                
                // Properties
                if(musicService.getPlayer().getRepeatMode() == ExoPlayer.REPEAT_MODE_OFF) {
                	binding.repeat.setImageResource(R.drawable.ic_repeat_off);
                    repeatMode = ExoPlayer.REPEAT_MODE_OFF;
                } else if(musicService.getPlayer().getRepeatMode() == ExoPlayer.REPEAT_MODE_ONE) {
                	binding.repeat.setImageResource(R.drawable.ic_repeat_once);
                    repeatMode = ExoPlayer.REPEAT_MODE_ONE;
                }
                else {
                    binding.repeat.setImageResource(R.drawable.ic_repeat);
                    repeatMode = ExoPlayer.REPEAT_MODE_ALL;
                }
            
                if(musicService.getPlayer().getShuffleModeEnabled()) {
                	binding.shuffle.setImageResource(R.drawable.ic_shuffle_on);
                    inShuffleMode = true;
                } else {
                	binding.shuffle.setImageResource(R.drawable.ic_shuffle_off);
                    inShuffleMode = false;
                }
            
                if(musicService.getPlayer().isPlaying()) {
                	binding.playPause.setImageResource(R.drawable.ic_pause_circle_outline);
                    isPlayerPlaying = true;
                } else {
                    binding.playPause.setImageResource(R.drawable.ic_play_circle_outline);
                    isPlayerPlaying = false;
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
        
        binding.repeat.setOnClickListener(view -> {
                if(repeatMode == 0) {
                	musicService.getPlayer().setRepeatMode(ExoPlayer.REPEAT_MODE_ONE);
                    binding.repeat.setImageResource(R.drawable.ic_repeat_once);
                    repeatMode = ExoPlayer.REPEAT_MODE_ONE;
                    Toast.makeText(PlayerActivity.this, "REPEAT_ONCE", Toast.LENGTH_SHORT).show();
                }
                else if(repeatMode == 1) {
                	musicService.getPlayer().setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
                    binding.repeat.setImageResource(R.drawable.ic_repeat);
                    repeatMode = ExoPlayer.REPEAT_MODE_ALL;
                    Toast.makeText(PlayerActivity.this, "REPEAT_ALL", Toast.LENGTH_SHORT).show();
                }
                else if(repeatMode == 2) {
                    musicService.getPlayer().setRepeatMode(ExoPlayer.REPEAT_MODE_OFF);
                	binding.repeat.setImageResource(R.drawable.ic_repeat_off);
                    repeatMode = ExoPlayer.REPEAT_MODE_OFF;
                    Toast.makeText(PlayerActivity.this, "REPEAT_OFF", Toast.LENGTH_SHORT).show();
                }
        });
        
        binding.shuffle.setOnClickListener(view -> {
                if(!inShuffleMode) {
                	musicService.getPlayer().setShuffleModeEnabled(true);
                    binding.shuffle.setImageResource(R.drawable.ic_shuffle_on);
                    inShuffleMode = true;
                    Toast.makeText(PlayerActivity.this, "SHUFFLE_ON", Toast.LENGTH_SHORT).show();
                } else {
                    musicService.getPlayer().setShuffleModeEnabled(false);
                    binding.shuffle.setImageResource(R.drawable.ic_shuffle_off);
                    inShuffleMode = false;
                    Toast.makeText(PlayerActivity.this, "SHUFFLE_OFF", Toast.LENGTH_SHORT).show();
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
                
                    // Properties
                    if(musicService.getPlayer().getRepeatMode() == ExoPlayer.REPEAT_MODE_OFF) {
                    	binding.repeat.setImageResource(R.drawable.ic_repeat_off);
                        repeatMode = ExoPlayer.REPEAT_MODE_OFF;
                    } else if(musicService.getPlayer().getRepeatMode() == ExoPlayer.REPEAT_MODE_ONE) {
                    	binding.repeat.setImageResource(R.drawable.ic_repeat_once);
                        repeatMode = ExoPlayer.REPEAT_MODE_ONE;
                    }
                    else {
                        binding.repeat.setImageResource(R.drawable.ic_repeat);
                        repeatMode = ExoPlayer.REPEAT_MODE_ALL;
                    }
                    
                    if(musicService.getPlayer().isPlaying()) {
                        binding.playPause.setImageResource(R.drawable.ic_pause_circle_outline);
                        isPlayerPlaying = true;
                    } else {
                        binding.playPause.setImageResource(R.drawable.ic_play_circle_outline);
                        isPlayerPlaying = false;
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
        runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    if(!isBindingNull) {
                        assert musicService != null;
                    	if(musicService.getPlayer().isPlaying()) {
                            binding.songCurrentDuration.setText(MusicUtils.getFormattedTime(musicService.getPlayer().getContentPosition())); 
                            binding.seekBar.setProgress((int) musicService.getPlayer().getContentPosition());
                    	}
                    }
                    
                    if(!isBindingNull) {
                    	progressHandler.postDelayed(this, 1000);
                    }
                }
                
        });
    }
}
