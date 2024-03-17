package com.itsmcodez.playful.fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.content.ServiceConnection;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.itsmcodez.playful.PlayerActivity;
import com.itsmcodez.playful.adapters.SongsAdapter;
import com.itsmcodez.playful.databinding.FragmentSongsBinding;
import com.itsmcodez.playful.models.SongsModel;
import com.itsmcodez.playful.services.MusicService;
import com.itsmcodez.playful.utils.MusicUtils;
import com.itsmcodez.playful.viewmodels.SongsViewModel;
import java.util.ArrayList;

public class SongsFragment extends Fragment implements ServiceConnection {
    private FragmentSongsBinding binding;
    private SongsAdapter songsAdapter;
    private SongsViewModel songsViewModel;
    private MusicService musicService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songsViewModel = new ViewModelProvider(this).get(SongsViewModel.class);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Bind to views
        binding = FragmentSongsBinding.inflate(inflater, container, false);

        // bind to MusicService
        getActivity().bindService(new Intent(container.getContext(), MusicService.class), this, MusicUtils.BIND_AUTO_CREATE);

        // RecyclerView
        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(container.getContext(), RecyclerView.VERTICAL, false));

        // Observe LiveData (songsViewModel)
        songsViewModel
                .getAllSongs()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<ArrayList<SongsModel>>() {
                            @Override
                            public void onChanged(ArrayList<SongsModel> allSongs) {

                                songsAdapter =
                                        new SongsAdapter(
                                                container.getContext(), inflater, allSongs);
                                binding.recyclerView.setAdapter(songsAdapter);

                                if (binding.recyclerView.getAdapter().getItemCount() == 0) {
                                    binding.recyclerView.setVisibility(View.GONE);
                                    binding.noSongsText.setVisibility(View.VISIBLE);
                                }

                                songsAdapter.setOnClickEvents(
                                        new SongsAdapter.OnClickEvents() {
                                            @Override
                                            public void onItemClick(
                                                    View view, SongsModel song, int position) {
                                
                                                        // Set media items
                                                        MusicUtils.setMediaItems(getMediaItems(allSongs));
                                
                                                        if(!musicService.isStarted){
                                                            getActivity().startService(new Intent(container.getContext(), MusicService.class));
                                                        }
                                
                                                        if(musicService.getPlayer().isPlaying()){
                                                            musicService.getPlayer().stop();
                                                        }
                                
                                                        musicService.getPlayer().setMediaItems(MusicUtils.getMediaItems(), position, 0);
                                
                                                        startActivity(new Intent(container.getContext(), PlayerActivity.class));
                                                        
                                                    }

                                            @Override
                                            public boolean onItemLongClick(
                                                    View view, SongsModel song, int position) {
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
        getActivity().unbindService(this);
    }

    public void recyclerViewScrollToTop() {
        if ((binding.recyclerView.getAdapter() != null)
                && (binding.recyclerView.getAdapter().getItemCount() != 0)) {
            binding.recyclerView.scrollToPosition(0);
        }
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
