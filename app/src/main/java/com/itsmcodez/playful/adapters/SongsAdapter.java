package com.itsmcodez.playful.adapters;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsmcodez.playful.BaseApplication;
import com.itsmcodez.playful.R;
import com.itsmcodez.playful.databinding.LayoutPlaylistsBinding;
import com.itsmcodez.playful.databinding.SongItemViewBinding;
import com.itsmcodez.playful.fragments.PlaylistsFragment;
import com.itsmcodez.playful.models.PlaylistsModel;
import com.itsmcodez.playful.models.PlaylistSongsModel;
import com.itsmcodez.playful.models.SongsModel;
import com.itsmcodez.playful.repositories.PlaylistsRepository;
import com.itsmcodez.playful.utils.MusicUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsViewHolder> {
    private SongItemViewBinding binding;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<SongsModel> songs;
    private OnClickEvents onClickEvents;

    public SongsAdapter(Context context, LayoutInflater inflater, ArrayList<SongsModel> songs) {
        this.context = context;
        this.inflater = inflater;
        this.songs = songs;
    }

    public static class SongsViewHolder extends RecyclerView.ViewHolder {
        public SongItemViewBinding binding;
        public TextView songTitle, songArtist;
        public CircleImageView albumArtwork;
        public ImageView itemMenu;
        public CardView itemView;

        public SongsViewHolder(SongItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.songTitle = binding.songTitle;
            this.songArtist = binding.songArtist;
            this.albumArtwork = binding.albumArtwork;
            this.itemMenu = binding.itemMenu;
            this.itemView = binding.itemView;
        }
    }

    @Override
    public SongsAdapter.SongsViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        // Bind to views
        binding = SongItemViewBinding.inflate(inflater, parent, false);
        return new SongsAdapter.SongsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SongsAdapter.SongsViewHolder viewHolder, int position) {

        // get song at position
        SongsModel song = songs.get(position);

        // Metadata
        viewHolder.songTitle.setText(song.getTitle());
        viewHolder.songArtist.setText(song.getArtist());

        // artwork
        viewHolder.albumArtwork.setImageURI(song.getAlbumArtwork());
        if (viewHolder.albumArtwork.getDrawable() == null) {
            viewHolder.albumArtwork.setImageDrawable(context.getDrawable(R.drawable.ic_music_note));
        }

        viewHolder.itemView.setOnClickListener(view -> {
                if(onClickEvents != null){
                    onClickEvents.onItemClick(view, song, position);
                }
        });

        viewHolder.itemMenu.setOnClickListener(
                view -> {
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.inflate(R.menu.song_item_view_menu);
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(
                            new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {

                                    if (item.getItemId() == R.id.add_to_playlist_menu_item) {

                                        LayoutPlaylistsBinding playlistsView =
                                                LayoutPlaylistsBinding.inflate(inflater);
                                        playlistsView.recyclerView.setLayoutManager(
                                                new LinearLayoutManager(context));
                                        ArrayList<PlaylistsModel> allPlaylists =
                                                MusicUtils.getAllPlaylists(context);
                                        PlaylistSelectionAdapter adapter =
                                                new PlaylistSelectionAdapter(
                                                        context, inflater, allPlaylists);
                                        playlistsView.recyclerView.setAdapter(adapter);

                                        AlertDialog selectPlaylistDialog =
                                                new MaterialAlertDialogBuilder(context)
                                                        .setView(playlistsView.getRoot())
                                                        .setTitle(
                                                                R.string
                                                                        .select_playlist_dialog_title)
                                                        .setNeutralButton(
                                                                R.string.cancel,
                                                                new DialogInterface
                                                                        .OnClickListener() {
                                                                    @Override
                                                                    public void onClick(
                                                                            DialogInterface dialog,
                                                                            int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                })
                                                        .create();
                                        selectPlaylistDialog.show();

                                        // Adapter on item click
                                        adapter.setOnClickEvents(
                                                new PlaylistSelectionAdapter.OnClickEvents() {
                                                    @Override
                                                    public void onItemClick(
                                                            View view,
                                                            PlaylistsModel playlist,
                                                            int position) {

                                                        for (PlaylistSongsModel _song :
                                                                playlist.getSongs()) {
                                                            if (_song.getTitle()
                                                                    .equals(song.getTitle())) {
                                                                Toast.makeText(
                                                                                context
                                                                                        .getApplicationContext(),
                                                                                "Song already added!",
                                                                                Toast.LENGTH_LONG)
                                                                        .show();
                                                                selectPlaylistDialog.dismiss();
                                                                return;
                                                            }
                                                        }
                                            
                                                        PlaylistSongsModel playlistSong =
                                                                        new PlaylistSongsModel(
                                                                                song.getPath(),
                                                                                song.getTitle(),
                                                                                song.getArtist(),
                                                                                song.getDuration(),
                                                                                song.getAlbum(),
                                                                                song.getAlbumId(),
                                                                                song.getSongId());

                                                                PlaylistsRepository repo =
                                                                        PlaylistsRepository
                                                                                .getInstance(
                                                                                        context);
                                                                repo.addSongToPlaylist(
                                                                        context,
                                                                        playlistSong,
                                                                        position);
                                            
                                                                Toast.makeText(context.getApplicationContext(), "1 song added to " + playlist.getTitle(), Toast.LENGTH_LONG).show();

                                                                // Dismiss dialog
                                                                selectPlaylistDialog.dismiss();
                                                    }

                                                    @Override
                                                    public boolean onItemLongClick(
                                                            View view,
                                                            PlaylistsModel playlist,
                                                            int position) {
                                                        return false;
                                                    }
                                                });
                                    }

                                    return true;
                                }
                            });
                });
    }

    @Override
    public int getItemCount() {
        int size;

        if (songs != null && songs.size() > 0) {
            size = songs.size();
        } else size = 0;

        return size;
    }
    
    public void setOnClickEvents(OnClickEvents onClickEvents){
        this.onClickEvents = onClickEvents;
    }
    
    public interface OnClickEvents{
        void onItemClick(View view, SongsModel song, int position);
        boolean onItemLongClick(View view, SongsModel song, int position);
    }
}
