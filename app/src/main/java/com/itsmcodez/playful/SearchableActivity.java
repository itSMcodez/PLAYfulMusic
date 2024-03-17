package com.itsmcodez.playful;

import android.content.Intent;
import android.app.SearchManager;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import android.content.ComponentName;
import android.os.IBinder;
import androidx.media3.common.MediaMetadata;
import com.itsmcodez.playful.services.MusicService;
import com.itsmcodez.playful.utils.MusicUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.itsmcodez.playful.adapters.SongsAdapter;
import com.itsmcodez.playful.databinding.ActivitySearchableBinding;
import com.itsmcodez.playful.models.SongsModel;
import com.itsmcodez.playful.providers.CustomSuggestionsProvider;
import com.itsmcodez.playful.viewmodels.SongsViewModel;
import java.util.ArrayList;

public class SearchableActivity extends AppCompatActivity implements ServiceConnection {
    private ActivitySearchableBinding binding;
    private MusicService musicService;
    private SongsViewModel songsViewModel;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind to views
        binding = ActivitySearchableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // initialize songsViewModel
        songsViewModel = new ViewModelProvider(this).get(SongsViewModel.class);

        // ActionBar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(SearchableActivity.this));

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
            SearchRecentSuggestions searchRecentSuggestions =
                    new SearchRecentSuggestions(
                            SearchableActivity.this,
                            CustomSuggestionsProvider.AUTHORITY,
                            CustomSuggestionsProvider.MODE);
            searchRecentSuggestions.saveRecentQuery(query, null);
        }
    }

    private void search(String query) {

        // Observe LiveData
        songsViewModel
                .getAllSongs()
                .observe(
                        SearchableActivity.this,
                        new Observer<ArrayList<SongsModel>>() {
                            @Override
                            public void onChanged(ArrayList<SongsModel> allSongs) {
                                ArrayList<SongsModel> filteredSongs = new ArrayList<>();

                                for (SongsModel song : allSongs) {
                                    if (song.getTitle().toLowerCase().contains(query.toLowerCase())
                                            || song.getArtist()
                                                    .toLowerCase()
                                                    .contains(query.toLowerCase())) {
                                        filteredSongs.add(song);
                                    }
                                }
                    
                                SongsAdapter songsAdapter =
                                        new SongsAdapter(
                                                SearchableActivity.this,
                                                getLayoutInflater(),
                                                filteredSongs);
                                binding.recyclerView.setAdapter(songsAdapter);

                                if (binding.recyclerView.getAdapter().getItemCount() == 0) {
                                    binding.recyclerView.setVisibility(View.GONE);
                                    binding.noSongsText.setVisibility(View.VISIBLE);
                                }

                                getSupportActionBar()
                                        .setTitle(
                                                "Found "
                                                        + filteredSongs.size()
                                                        + " songs for "
                                                        + "\""
                                                        + query
                                                        + "\"");

                                songsAdapter.setOnClickEvents(
                                        new SongsAdapter.OnClickEvents() {
                                            @Override
                                            public void onItemClick(
                                                    View view, SongsModel song, int position) {
                                
                                                // Set media items
                                                MusicUtils.setMediaItems(getMediaItems(filteredSongs));

                                                if (!musicService.isStarted) {
                                                    startService(
                                                            new Intent(
                                                                    SearchableActivity.this,
                                                                    MusicService.class));
                                                }

                                                if (musicService.getPlayer().isPlaying()) {
                                                    musicService.getPlayer().stop();
                                                }
                                
                                                musicService.getPlayer().setMediaItems(MusicUtils.getMediaItems(), position, 0);
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

    @Override
    protected void onResume() {
        bindService(
                new Intent(SearchableActivity.this, MusicService.class),
                this,
                BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
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
