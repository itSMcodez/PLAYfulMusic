package com.itsmcodez.playful.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.itsmcodez.playful.R;
import com.itsmcodez.playful.databinding.AlbumItemViewBinding;
import com.itsmcodez.playful.models.AlbumsModel;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumsViewHolder> {
    private AlbumItemViewBinding binding;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<AlbumsModel> albums;

    public AlbumsAdapter(Context context, LayoutInflater inflater, ArrayList<AlbumsModel> albums) {
        this.context = context;
        this.inflater = inflater;
        this.albums = albums;
    }

    public static class AlbumsViewHolder extends RecyclerView.ViewHolder {
        public AlbumItemViewBinding binding;
        public TextView albumTitle;
        public CircleImageView albumArtwork;
        public CardView itemView;

        public AlbumsViewHolder(AlbumItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.albumTitle = binding.albumTitle;
            this.albumArtwork = binding.albumArtwork;
            this.itemView = binding.itemView;
        }
    }

    @Override
    public AlbumsAdapter.AlbumsViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        // Bind to views
        binding = AlbumItemViewBinding.inflate(inflater, parent, false);
        return new AlbumsAdapter.AlbumsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AlbumsAdapter.AlbumsViewHolder viewHolder, int position) {
        
        // get album at position 
        AlbumsModel album = albums.get(position);
        
        // Metadata 
        viewHolder.albumTitle.setSelected(true);
        viewHolder.albumTitle.setText(album.getAlbum());
        
        // artwork
        viewHolder.albumArtwork.setImageURI(album.getAlbumArtwork());
        if(viewHolder.albumArtwork.getDrawable() == null){
            viewHolder.albumArtwork.setImageDrawable(context.getDrawable(R.drawable.ic_album_outline));
        }
        
        viewHolder.itemView.setOnClickListener(view -> {
            
        });
    }

    @Override
    public int getItemCount() {
        int size;
        
        if(albums != null && albums.size() > 0){
            size = albums.size();
        }
        else size = 0;
        
        return size;
    }
}