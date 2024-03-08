package com.itsmcodez.playful.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.itsmcodez.playful.databinding.LayoutPlaylistItemViewBinding;
import com.itsmcodez.playful.models.PlaylistsModel;
import java.util.ArrayList;

public class PlaylistSelectionAdapter extends RecyclerView.Adapter<PlaylistSelectionAdapter.PlaylistViewHolder> {
    private LayoutPlaylistItemViewBinding binding;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<PlaylistsModel> playlists;
    private OnClickEvents onClickEvents;

    public PlaylistSelectionAdapter(Context context, LayoutInflater inflater, ArrayList<PlaylistsModel> playlists) {
        this.context = context;
        this.inflater = inflater;
        this.playlists = playlists;
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        public TextView playlistTitle;
        public LinearLayout itemView;

        public PlaylistViewHolder(LayoutPlaylistItemViewBinding binding) {
            super(binding.getRoot());
            this.playlistTitle = binding.playlistTitle;
            this.itemView = binding.itemView;
        }
    }

    @Override
    public PlaylistSelectionAdapter.PlaylistViewHolder onCreateViewHolder(
            ViewGroup parent, int position) {
        // Bind to views
        binding = LayoutPlaylistItemViewBinding.inflate(inflater, parent, false);
        return new PlaylistSelectionAdapter.PlaylistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(PlaylistSelectionAdapter.PlaylistViewHolder viewHolder, int position) {
        PlaylistsModel playlist = playlists.get(position);
        
        // Playlist title
        viewHolder.playlistTitle.setText(playlist.getTitle());
        
        viewHolder.itemView.setOnClickListener(view -> {
                if(onClickEvents != null){
                    onClickEvents.onItemClick(view, playlist, position);
                }
        });
    }

    @Override
    public int getItemCount() {
        int size;
        
        if(playlists != null && playlists.size() > 0){
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
