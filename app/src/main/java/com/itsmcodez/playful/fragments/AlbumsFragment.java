package com.itsmcodez.playful.fragments;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.itsmcodez.playful.AlbumArtistActivity;
import com.itsmcodez.playful.adapters.AlbumsAdapter;
import com.itsmcodez.playful.databinding.FragmentAlbumsBinding;
import com.itsmcodez.playful.models.AlbumsModel;
import com.itsmcodez.playful.viewmodels.AlbumsViewModel;
import java.util.ArrayList;

public class AlbumsFragment extends Fragment {
    private FragmentAlbumsBinding binding;
    private AlbumsAdapter albumsAdapter;
    private AlbumsViewModel albumsViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumsViewModel = new ViewModelProvider(this).get(AlbumsViewModel.class);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Bind to views
        binding = FragmentAlbumsBinding.inflate(inflater, container, false);

        // RecyclerView
        binding.recyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 2));

        // Observe LiveData (albumsViewModel)
        albumsViewModel
                .getAllAlbums()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<ArrayList<AlbumsModel>>() {
                            @Override
                            public void onChanged(ArrayList<AlbumsModel> allAlbums) {

                                albumsAdapter =
                                        new AlbumsAdapter(
                                                container.getContext(), inflater, allAlbums);
                                binding.recyclerView.setAdapter(albumsAdapter);

                                if (binding.recyclerView.getAdapter().getItemCount() == 0) {
                                    binding.recyclerView.setVisibility(View.GONE);
                                    binding.noSongsText.setVisibility(View.VISIBLE);
                                }

                                albumsAdapter.setOnClickEvents(
                                        new AlbumsAdapter.OnClickEvents() {
                                            @Override
                                            public void onItemClick(
                                                    View view, AlbumsModel album, int position) {
                                                        startActivity(new Intent(container.getContext(), AlbumArtistActivity.class)
                                            .putExtra("displaySongs", "fromAlbum").putExtra("albumId", album.getAlbumId()).putExtra("title", album.getAlbum()));
                                                    }

                                            @Override
                                            public boolean onItemLongClick(
                                                    View view, AlbumsModel album, int position) {
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
}
