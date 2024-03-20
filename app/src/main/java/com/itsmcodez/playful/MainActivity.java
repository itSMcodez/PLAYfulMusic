package com.itsmcodez.playful;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.widget.TooltipCompat;
import com.itsmcodez.playful.utils.MusicUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itsmcodez.playful.databinding.ActivityMainBinding;
import com.itsmcodez.playful.fragments.AlbumsFragment;
import com.itsmcodez.playful.fragments.ArtistsFragment;
import com.itsmcodez.playful.fragments.PlaylistsFragment;
import com.itsmcodez.playful.fragments.SettingsFragment;
import com.itsmcodez.playful.fragments.SongsFragment;
import com.itsmcodez.playful.services.MusicService;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    private ActivityMainBinding binding;
    private MenuItem sortMenuItem;
    private MusicService musicService;
    private boolean isPlayerPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate and get instance of binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // set content view to binding's root
        setContentView(binding.getRoot());

        // ActionBar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(R.string.activity_main_action_bar_title);
        getSupportActionBar().setSubtitle(R.string.activity_main_action_bar_subtitle);
        
        // Tooltips
        TooltipCompat.setTooltipText(binding.miniController, "Mini controller");
        TooltipCompat.setTooltipText(binding.skipNext, "Next track");
        TooltipCompat.setTooltipText(binding.playPause, "Play/Pause");

        // Initialize fragments
        SongsFragment songsFragment = new SongsFragment();
        AlbumsFragment albumsFragment = new AlbumsFragment();
        ArtistsFragment artistsFragment = new ArtistsFragment();
        PlaylistsFragment playlistsFragment = new PlaylistsFragment();
        SettingsFragment settingsFragment = new SettingsFragment();
        
        // Default screen
        replaceFragment(songsFragment);
        
        // scroll to top functionality
        binding.fabScrollToTop.setOnClickListener(view -> {
                
                if(binding.bottomNavBar.getSelectedItemId() == R.id.songs_menu_item){
                    if(songsFragment.isVisible()){
                        songsFragment.recyclerViewScrollToTop();
                    }
                }
                
                if(binding.bottomNavBar.getSelectedItemId() == R.id.albums_menu_item){
                    if(albumsFragment.isVisible()){
                        albumsFragment.recyclerViewScrollToTop();
                    }
                }
                
                if(binding.bottomNavBar.getSelectedItemId() == R.id.artists_menu_item){
                    if(artistsFragment.isVisible()){
                        artistsFragment.recyclerViewScrollToTop();
                    }
                }
        });
        
        // Mini Controller
        binding.miniController.setOnClickListener(view -> {
                startActivity(new Intent(MainActivity.this, PlayerActivity.class));
        });
        
        // BottomNavigation
        binding.bottomNavBar.setOnItemSelectedListener(
                new BottomNavigationView.OnItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                    
                        if(item.getItemId() == R.id.songs_menu_item){
                            replaceFragment(songsFragment);
                            if(!sortMenuItem.isVisible()){
                                sortMenuItem.setVisible(true);
                            }
                            if(binding.miniController.getVisibility() != View.VISIBLE){
                                binding.miniController.setVisibility(View.VISIBLE);
                            }
                            if(binding.fabScrollToTop.getVisibility() != View.VISIBLE){
                                binding.fabScrollToTop.setVisibility(View.VISIBLE);
                            }
                        }
                    
                        if(item.getItemId() == R.id.albums_menu_item){
                            replaceFragment(albumsFragment);
                            if(sortMenuItem.isVisible()){
                                sortMenuItem.setVisible(false);
                            }
                            if(binding.miniController.getVisibility() != View.VISIBLE){
                                binding.miniController.setVisibility(View.VISIBLE);
                            }
                            if(binding.fabScrollToTop.getVisibility() != View.VISIBLE){
                                binding.fabScrollToTop.setVisibility(View.VISIBLE);
                            }
                        }
                    
                        if(item.getItemId() == R.id.artists_menu_item){
                            replaceFragment(artistsFragment);
                            if(sortMenuItem.isVisible()){
                                sortMenuItem.setVisible(false);
                            }
                            if(binding.miniController.getVisibility() != View.VISIBLE){
                                binding.miniController.setVisibility(View.VISIBLE);
                            }
                            if(binding.fabScrollToTop.getVisibility() != View.VISIBLE){
                                binding.fabScrollToTop.setVisibility(View.VISIBLE);
                            }
                        }
                    
                        if(item.getItemId() == R.id.playlists_menu_item){
                            replaceFragment(playlistsFragment);
                            if(sortMenuItem.isVisible()){
                                sortMenuItem.setVisible(false);
                            }
                            if(binding.miniController.getVisibility() != View.VISIBLE){
                                binding.miniController.setVisibility(View.VISIBLE);
                            }
                            if(binding.fabScrollToTop.getVisibility() == View.VISIBLE){
                                binding.fabScrollToTop.setVisibility(View.GONE);
                            }
                        }
                    
                        if(item.getItemId() == R.id.settings_menu_item){
                            replaceFragment(settingsFragment);
                            if(sortMenuItem.isVisible()){
                                sortMenuItem.setVisible(false);
                            }
                            if(binding.miniController.getVisibility() != View.GONE){
                                binding.miniController.setVisibility(View.GONE);
                            }
                            if(binding.fabScrollToTop.getVisibility() == View.VISIBLE){
                                binding.fabScrollToTop.setVisibility(View.GONE);
                            }
                        }
                    
                        return true;
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
        if(!isPlayerPlaying){
            stopService(new Intent(MainActivity.this, MusicService.class));
        }
    }
    
    @Override
    protected void onResume() {
        bindService(
                new Intent(MainActivity.this, MusicService.class),
                this,
                BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    protected void onPause() {
        
        if(musicService != null) {
        	if(musicService.getPlayer().isPlaying()){
                isPlayerPlaying = true;
            }
            else{
                isPlayerPlaying = false;
            }
            musicService.setStateHandler(null);
            unbindService(this);
        }
        super.onPause();
    }
    
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MusicBinder binder = (MusicService.MusicBinder) iBinder;
        musicService = binder.getService();
        
        // Check if player is not null and set metadata to current media item if true
        if(musicService.getPlayer().getMediaItemCount() != 0 && musicService.getPlayer().getCurrentMediaItem() != null){
                // Metadata
                binding.songTitle.setSelected(true);
                binding.songTitle.setText(musicService.getPlayer().getCurrentMediaItem().mediaMetadata.title);
                binding.songArtist.setText(musicService.getPlayer().getCurrentMediaItem().mediaMetadata.artist);
                
                // Album artwork 
                binding.albumArtwork.setImageURI(musicService.getPlayer().getCurrentMediaItem().mediaMetadata.artworkUri);
                if(binding.albumArtwork.getDrawable() == null) {
                	binding.albumArtwork.setImageResource(R.drawable.ic_music_note);
                }
                
                if(musicService.getPlayer().isPlaying()) {
                	binding.playPause.setImageResource(R.drawable.ic_pause_outline);
                    isPlayerPlaying = true;
                } else {
                    binding.playPause.setImageResource(R.drawable.ic_play_outline);
                    isPlayerPlaying = false;
                }
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
        
        musicService.setStateHandler(() -> {
                
                if(musicService.getPlayer().getMediaItemCount() != 0 && musicService.getPlayer().getCurrentMediaItem() != null){
                    // Metadata
                    binding.songTitle.setSelected(true);
                    binding.songTitle.setText(musicService.getPlayer().getCurrentMediaItem().mediaMetadata.title);
                    binding.songArtist.setText(musicService.getPlayer().getCurrentMediaItem().mediaMetadata.artist);
                
                    // Album artwork 
                    binding.albumArtwork.setImageURI(musicService.getPlayer().getCurrentMediaItem().mediaMetadata.artworkUri);
                    if(binding.albumArtwork.getDrawable() == null) {
                        binding.albumArtwork.setImageResource(R.drawable.ic_music_note);
                    }
                
                    if(musicService.getPlayer().isPlaying()) {
                        binding.playPause.setImageResource(R.drawable.ic_pause_outline);
                        isPlayerPlaying = true;    
                    } else {
                        binding.playPause.setImageResource(R.drawable.ic_play_outline);
                        isPlayerPlaying = false;    
                    }
                }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService.setStateHandler(null);
        musicService = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_menu, menu);
        
        // assign sort_menu_item to sortMenuItem
        sortMenuItem = menu.findItem(R.id.sort_menu_item);
        
        // SearchView
        MenuItem searchMenuItem = menu.findItem(R.id.search_menu_item);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryRefinementEnabled(true);
        
        return super.onCreateOptionsMenu(menu);
    }
    
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, fragment);
        fragmentTransaction.commit();
    }
}
