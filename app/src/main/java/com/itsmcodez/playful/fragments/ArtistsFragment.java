package com.itsmcodez.playful.fragments;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.itsmcodez.playful.AlbumArtistActivity;
import com.itsmcodez.playful.adapters.ArtistsAdapter;
import com.itsmcodez.playful.databinding.FragmentArtistsBinding;
import com.itsmcodez.playful.models.ArtistsModel;
import com.itsmcodez.playful.viewmodels.ArtistsViewModel;
import java.util.ArrayList;

public class ArtistsFragment extends Fragment {
    private FragmentArtistsBinding binding;
    private ArtistsAdapter artistsAdapter;
    private ArtistsViewModel artistsViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artistsViewModel = new ViewModelProvider(this).get(ArtistsViewModel.class);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Bind to views
        binding = FragmentArtistsBinding.inflate(inflater, container, false);

        // RecyclerView
        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(container.getContext(), RecyclerView.VERTICAL, false));

        // Observe LiveData (artistsViewModel)
        artistsViewModel
                .getAllArtists()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<ArrayList<ArtistsModel>>() {
                            @Override
                            public void onChanged(ArrayList<ArtistsModel> allArtists) {

                                artistsAdapter =
                                        new ArtistsAdapter(
                                                container.getContext(), inflater, allArtists);
                                binding.recyclerView.setAdapter(artistsAdapter);

                                if (binding.recyclerView.getAdapter().getItemCount() == 0) {
                                    binding.recyclerView.setVisibility(View.GONE);
                                    binding.noSongsText.setVisibility(View.VISIBLE);
                                }

                                artistsAdapter.setOnClickEvents(
                                        new ArtistsAdapter.OnClickEvents() {
                                            @Override
                                            public void onItemClick(
                                                    View view, ArtistsModel artist, int position) {
                                                        startActivity(new Intent(container.getContext(), AlbumArtistActivity.class)
                                            .putExtra("displaySongs", "fromArtist").putExtra("albumId", artist.getAlbumId()).putExtra("title", artist.getArtist()));
                                                    }

                                            @Override
                                            public boolean onItemLongClick(
                                                    View view, ArtistsModel artist, int position) {
                                                        return false;
                                                    }
                                        });
                            }
                        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }
    
    public void recyclerViewScrollToTop() {
    	if((binding.recyclerView.getAdapter() != null) && (binding.recyclerView.getAdapter().getItemCount() != 0)){
            binding.recyclerView.scrollToPosition(0);
        }
    }
}
