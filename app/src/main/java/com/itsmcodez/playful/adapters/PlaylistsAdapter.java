package com.itsmcodez.playful.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.itsmcodez.playful.R;
import com.itsmcodez.playful.databinding.PlaylistItemViewBinding;
import com.itsmcodez.playful.models.PlaylistsModel;
import java.util.ArrayList;

public class PlaylistsAdapter extends RecyclerView.Adapter<PlaylistsAdapter.PlaylistsViewHolder> {
    private PlaylistItemViewBinding binding;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<PlaylistsModel> playlists;
    private OnClickEvents onClickEvents;

    public PlaylistsAdapter(Context context, LayoutInflater inflater, ArrayList<PlaylistsModel> playlists) {
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
        viewHolder.playlistInfo.setText(playlist.getSongsCount() + " • " + playlist.getSongsDuration());
        
        // artwork
        viewHolder.albumArtwork.setImageURI(playlist.getAlbumArtwork());
        if(viewHolder.albumArtwork.getDrawable() == null){
            viewHolder.albumArtwork.setImageDrawable(context.getDrawable(R.drawable.ic_playlist_music_outline));
        }
        
        viewHolder.itemView.setOnClickListener(view -> {
                if(onClickEvents != null){
                    onClickEvents.onItemClick(view, playlist, position);
                }
        });
        
        viewHolder.itemMenu.setOnClickListener(view -> {
                
        });
    }

    @Override
    public int getItemCount() {
        int size;
        
        if(playlists != null && playlists.size() > 0){
            size = playlists.size();
        }
        else size = 0;
        
        return size;
    }
    
    public void setOnClickEvents(OnClickEvents onClickEvents){
        this.onClickEvents = onClickEvents;
    }
    
    public interface OnClickEvents{
        void onItemClick(View view, PlaylistsModel playlist, int position);
        boolean onItemLongClick(View view, PlaylistsModel playlist, int position);
    }
}