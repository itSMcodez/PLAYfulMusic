package com.itsmcodez.playful.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsmcodez.playful.R;
import com.itsmcodez.playful.adapters.PlaylistsAdapter;
import com.itsmcodez.playful.databinding.FragmentPlaylistsBinding;
import com.itsmcodez.playful.databinding.LayoutAddPlaylistBinding;
import com.itsmcodez.playful.models.PlaylistsModel;
import com.itsmcodez.playful.viewmodels.PlaylistsViewModel;
import java.util.ArrayList;

public class PlaylistsFragment extends Fragment {
    private FragmentPlaylistsBinding binding;
    private static final String TAG = "PlaylistsFragment";
    private PlaylistsAdapter playlistsAdapter;
    private static PlaylistsViewModel playlistsViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlistsViewModel = new ViewModelProvider(this).get(PlaylistsViewModel.class);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Bind to views
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false);

        // Add a new playlist
        binding.fabAdd.setOnClickListener(
                view -> {
                    showAddPlaylistDialog(container.getContext());
                });

        // RecyclerView
        binding.recyclerView.setLayoutManager(new GridLayoutManager(container.getContext(), 2));

        // Observe LiveData (playlistsViewModel)
        playlistsViewModel
                .getAllPlaylists()
                .observe(
                        getViewLifecycleOwner(),
                        new Observer<ArrayList<PlaylistsModel>>() {
                            @Override
                            public void onChanged(ArrayList<PlaylistsModel> allPlaylists) {
                                
                                playlistsAdapter = new PlaylistsAdapter(container.getContext(), inflater, allPlaylists);
                                binding.recyclerView.setAdapter(playlistsAdapter);
                                playlistsAdapter.notifyDataSetChanged();
                                
                            }
                        });

        return binding.getRoot();
    }

    private void showAddPlaylistDialog(final Context context) {

        LayoutAddPlaylistBinding layoutAddPlaylistBinding =
                LayoutAddPlaylistBinding.inflate(getLayoutInflater());

        AlertDialog addPlaylistDialog =
                new MaterialAlertDialogBuilder(context)
                        .setView(layoutAddPlaylistBinding.getRoot())
                        .setTitle(R.string.fragment_playlists_dialog_title)
                        .setIcon(R.drawable.ic_playlist_plus)
                        .setPositiveButton(
                                R.string.fragment_playlists_dialog_positive_bt_text,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        if (layoutAddPlaylistBinding
                                                .textInputEditText
                                                .getText()
                                                .toString()
                                                .trim()
                                                .isEmpty()) {
                                            Toast.makeText(
                                                            context,
                                                            R.string
                                                                    .fragment_playlists_dialog_positive_bt_clicked_rational,
                                                            Toast.LENGTH_LONG)
                                                    .show();
                                        } else {
                                            String title =
                                                    layoutAddPlaylistBinding
                                                            .textInputEditText
                                                            .getText()
                                                            .toString();
                                            playlistsViewModel.addPlaylist(title);
                                            Log.d(TAG, "Added " + title);
                                        }
                                    }
                                })
                        .setNegativeButton(
                                R.string.fragment_playlists_dialog_negative_bt_text,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.cancel();
                                    }
                                })
                        .create();
        addPlaylistDialog.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(context, R.string.cancel, Toast.LENGTH_SHORT).show();
                    }
                });
        addPlaylistDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }

    public static PlaylistsViewModel getViewModel() {
        return playlistsViewModel;
    }
}
