package com.itsmcodez.playful.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsmcodez.playful.R;
import com.itsmcodez.playful.databinding.LayoutAddPlaylistBinding;
import com.itsmcodez.playful.databinding.PlaylistItemViewBinding;
import com.itsmcodez.playful.fragments.PlaylistsFragment;
import com.itsmcodez.playful.models.PlaylistsModel;
import java.util.ArrayList;

public class PlaylistsAdapter extends RecyclerView.Adapter<PlaylistsAdapter.PlaylistsViewHolder> {
    private PlaylistItemViewBinding binding;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<PlaylistsModel> playlists;
    private OnClickEvents onClickEvents;

    public PlaylistsAdapter(
            Context context, LayoutInflater inflater, ArrayList<PlaylistsModel> playlists) {
        this.context = context;
        this.inflater = inflater;
        this.playlists = playlists;
    }

    public static class PlaylistsViewHolder extends RecyclerView.ViewHolder {
        public PlaylistItemViewBinding binding;
        public TextView playlistTitle, playlistInfo;
        public ImageView albumArtwork, itemMenu;
        public CardView itemView;

        public PlaylistsViewHolder(PlaylistItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.playlistTitle = binding.playlistTitle;
            this.playlistInfo = binding.playlistInfo;
            this.itemMenu = binding.itemMenu;
            this.albumArtwork = binding.albumArtwork;
            this.itemView = binding.itemView;
        }
    }

    @Override
    public PlaylistsAdapter.PlaylistsViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        // Bind to views
        binding = PlaylistItemViewBinding.inflate(inflater, parent, false);
        return new PlaylistsAdapter.PlaylistsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PlaylistsAdapter.PlaylistsViewHolder viewHolder, int position) {

        // get playlist at position
        PlaylistsModel playlist = playlists.get(position);

        // Metadata
        viewHolder.playlistTitle.setText(playlist.getTitle());
        if (playlist.getSongsCount().equals("1")) {
            viewHolder.playlistInfo.setText(
                    playlist.getSongsCount() + " Song" + " • " + playlist.getSongsDuration());
        } else if (playlist.getSongsCount().equals("0 Songs")) {
            viewHolder.playlistInfo.setText("0 Songs" + " • " + playlist.getSongsDuration());
        } else {
            viewHolder.playlistInfo.setText(
                    playlist.getSongsCount() + " Songs" + " • " + playlist.getSongsDuration());
        }

        // artwork
        if (playlist.getSongs() != null && playlist.getSongs().size() != 0) {
            var albumId = playlist.getSongs().get(playlist.getSongs().size() - 1).getAlbumId();
            Uri albumPath = Uri.parse("content://media/external/audio/albumart");
            long _albumId = Long.parseLong(albumId);
            Uri albumArtwork = ContentUris.withAppendedId(albumPath, _albumId);
            viewHolder.albumArtwork.setImageURI(albumArtwork);
            if (viewHolder.albumArtwork.getDrawable() == null) {
                viewHolder.albumArtwork.setImageDrawable(
                        context.getDrawable(R.drawable.ic_playlist_music_outline));
            }
        } else {
            viewHolder.albumArtwork.setImageDrawable(
                    context.getDrawable(R.drawable.ic_playlist_music_outline));
        }

        viewHolder.itemView.setOnClickListener(
                view -> {
                    if (onClickEvents != null) {
                        onClickEvents.onItemClick(view, playlist, position);
                    }
                });

        viewHolder.itemMenu.setOnClickListener(
                view -> {
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.inflate(R.menu.fragment_playlists_item_view_menu);
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(
                            new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {

                                    if (item.getItemId() == R.id.delete_menu_item) {

                                        if (playlist.getTitle().equals("Favourites")) {
                                            Toast.makeText(
                                                            context.getApplicationContext(),
                                                            "Cannot delete " + playlist.getTitle(),
                                                            Toast.LENGTH_LONG)
                                                    .show();
                                        } else {
                                            AlertDialog confirmDialog =
                                                    new MaterialAlertDialogBuilder(context)
                                                            .setTitle(R.string.confirm_deletion)
                                                            .setMessage(
                                                                    "Are you sure you want to delete "
                                                                            + "\""
                                                                            + playlist.getTitle()
                                                                            + "\"? "
                                                                            + "This will not affect or delete any of the songs in the playlist")
                                                            .setPositiveButton(
                                                                    R.string.confirm,
                                                                    new DialogInterface
                                                                            .OnClickListener() {

                                                                        @Override
                                                                        public void onClick(
                                                                                DialogInterface
                                                                                        dialog,
                                                                                int which) {

                                                                            Toast.makeText(
                                                                                            context
                                                                                                    .getApplicationContext(),
                                                                                            "Deleted "
                                                                                                    + playlist
                                                                                                            .getTitle(),
                                                                                            Toast
                                                                                                    .LENGTH_LONG)
                                                                                    .show();

                                                                            PlaylistsFragment
                                                                                    .getViewModel()
                                                                                    .deletePlaylist(
                                                                                            position);
                                                                        }
                                                                    })
                                                            .setNegativeButton(
                                                                    R.string.cancel,
                                                                    new DialogInterface
                                                                            .OnClickListener() {
                                                                        @Override
                                                                        public void onClick(
                                                                                DialogInterface
                                                                                        dialog,
                                                                                int which) {
                                                                            dialog.cancel();
                                                                        }
                                                                    })
                                                            .create();
                                            confirmDialog.show();
                                        }
                                    }

                                    if (item.getItemId() == R.id.rename_menu_item) {

                                        if (playlist.getTitle().equals("Favourites")) {
                                            Toast.makeText(
                                                            context.getApplicationContext(),
                                                            "Cannot rename " + playlist.getTitle(),
                                                            Toast.LENGTH_LONG)
                                                    .show();
                                        } else {

                                            LayoutAddPlaylistBinding textInputLayout =
                                                    LayoutAddPlaylistBinding.inflate(inflater);

                                            AlertDialog renameDialog =
                                                    new MaterialAlertDialogBuilder(context)
                                                            .setTitle(R.string.rename)
                                                            .setMessage(
                                                                    "Are you sure you want to rename "
                                                                            + "\""
                                                                            + playlist.getTitle()
                                                                            + "\"? "
                                                                            + "Type the new name for the playlist below")
                                                            .setView(textInputLayout.getRoot())
                                                            .setPositiveButton(
                                                                    R.string.rename,
                                                                    new DialogInterface
                                                                            .OnClickListener() {

                                                                        @Override
                                                                        public void onClick(
                                                                                DialogInterface
                                                                                        dialog,
                                                                                int which) {

                                                                            Toast.makeText(
                                                                                            context
                                                                                                    .getApplicationContext(),
                                                                                            "Renamed "
                                                                                                    + playlist
                                                                                                            .getTitle(),
                                                                                            Toast
                                                                                                    .LENGTH_LONG)
                                                                                    .show();

                                                                            if (textInputLayout
                                                                                    .textInputEditText
                                                                                    .getText()
                                                                                    .toString()
                                                                                    .trim()
                                                                                    .isEmpty()) {
                                                                                Toast.makeText(
                                                                                                context,
                                                                                                R
                                                                                                        .string
                                                                                                        .fragment_playlists_dialog_positive_bt_clicked_rational,
                                                                                                Toast
                                                                                                        .LENGTH_LONG)
                                                                                        .show();
                                                                            } else {
                                                                                String newName = textInputLayout
                                                                                                .textInputEditText
                                                                                                .getText()
                                                                                                .toString();
                                                    
                                                                                PlaylistsFragment
                                                                                    .getViewModel()
                                                                                    .renamePlaylist(
                                                                                            newName,
                                                                                            position);
                                                                            }
                                                                        }
                                                                    })
                                                            .setNegativeButton(
                                                                    R.string.cancel,
                                                                    new DialogInterface
                                                                            .OnClickListener() {
                                                                        @Override
                                                                        public void onClick(
                                                                                DialogInterface
                                                                                        dialog,
                                                                                int which) {
                                                                            dialog.cancel();
                                                                        }
                                                                    })
                                                            .create();
                                            renameDialog.show();
                                        }
                                    }

                                    return true;
                                }
                            });
                });
    }

    @Override
    public int getItemCount() {
        int size;

        if (playlists != null && playlists.size() > 0) {
            size = playlists.size();
        } else size = 0;

        return size;
    }

    public void setOnClickEvents(OnClickEvents onClickEvents) {
        this.onClickEvents = onClickEvents;
    }

    public interface OnClickEvents {
        void onItemClick(View view, PlaylistsModel playlist, int position);

        boolean onItemLongClick(View view, PlaylistsModel playlist, int position);
    }
}
