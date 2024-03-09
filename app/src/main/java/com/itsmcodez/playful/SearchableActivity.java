package com.itsmcodez.playful;

import android.content.Intent;
import android.app.SearchManager;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.itsmcodez.playful.adapters.SongsAdapter;
import com.itsmcodez.playful.databinding.ActivitySearchableBinding;
import com.itsmcodez.playful.models.SongsModel;
import com.itsmcodez.playful.providers.CustomSuggestionsProvider;
import com.itsmcodez.playful.viewmodels.SongsViewModel;
import java.util.ArrayList;

public class SearchableActivity extends AppCompatActivity {
    private ActivitySearchableBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind to views
        binding = ActivitySearchableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

    private void search(final String query) {
        SongsViewModel songsViewModel;

        songsViewModel = new ViewModelProvider(this).get(SongsViewModel.class);

        // Observe LiveData
        songsViewModel
                .getAllSongs()
                .observe(
                        SearchableActivity.this,
                        new Observer<ArrayList<SongsModel>>() {
                            @Override
                            public void onChanged(ArrayList<SongsModel> allSongs) {
                                ArrayList<SongsModel> filteredSongs = new ArrayList<>();
                    
                                for(SongsModel song : allSongs) {
                                	if(song.getTitle().toLowerCase().contains(query.toLowerCase()) || song.getArtist().toLowerCase().contains(query.toLowerCase())){
                                        filteredSongs.add(song);
                                    }
                                }
                                SongsAdapter songsAdapter = new SongsAdapter(SearchableActivity.this, getLayoutInflater(), filteredSongs);
                                binding.recyclerView.setAdapter(songsAdapter);
                    
                                if(binding.recyclerView.getAdapter().getItemCount() == 0){
                                    binding.recyclerView.setVisibility(View.GONE);
                                    binding.noSongsText.setVisibility(View.VISIBLE);
                                }
                    
                                getSupportActionBar().setTitle("Found " + filteredSongs.size() + " songs for " + "\"" + query + "\"");
                            }
                        });
    }
}
