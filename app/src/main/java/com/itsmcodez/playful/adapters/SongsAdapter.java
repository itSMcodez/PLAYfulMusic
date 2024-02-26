package com.itsmcodez.playful.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.itsmcodez.playful.R;
import com.itsmcodez.playful.databinding.SongItemViewBinding;
import com.itsmcodez.playful.models.SongsModel;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsViewHolder> {
    private SongItemViewBinding binding;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<SongsModel> songs;

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
        if(viewHolder.albumArtwork.getDrawable() == null){
            viewHolder.albumArtwork.setImageDrawable(context.getDrawable(R.drawable.ic_music_note));
        }
        
        viewHolder.itemView.setOnClickListener(view -> {
            
        });
        
        viewHolder.itemMenu.setOnClickListener(view -> {
            
        });
    }

    @Override
    public int getItemCount() {
        int size;
        
        if(songs != null && songs.size() > 0){
            size = songs.size();
        }
        else size = 0;
        
        return size;
    }
}
