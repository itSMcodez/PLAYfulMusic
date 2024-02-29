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
import com.itsmcodez.playful.databinding.ArtistItemViewBinding;
import com.itsmcodez.playful.models.ArtistsModel;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistsViewHolder> {
    private ArtistItemViewBinding binding;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ArtistsModel> artists;
    private OnClickEvents onClickEvents;

    public ArtistsAdapter(Context context, LayoutInflater inflater, ArrayList<ArtistsModel> artists) {
        this.context = context;
        this.inflater = inflater;
        this.artists = artists;
    }

    public static class ArtistsViewHolder extends RecyclerView.ViewHolder {
        public ArtistItemViewBinding binding;
        public TextView artistTitle;
        public ImageView badge;
        public CircleImageView albumArtwork;
        public CardView itemView;

        public ArtistsViewHolder(ArtistItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.artistTitle = binding.artistTitle;
            this.badge = binding.badge;
            this.albumArtwork = binding.albumArtwork;
            this.itemView = binding.itemView;
        }
    }

    @Override
    public ArtistsAdapter.ArtistsViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        // Bind to views
        binding = ArtistItemViewBinding.inflate(inflater, parent, false);
        return new ArtistsAdapter.ArtistsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ArtistsAdapter.ArtistsViewHolder viewHolder, int position) {
        
        // get album at position 
        ArtistsModel artist = artists.get(position);
        
        // Metadata 
        viewHolder.artistTitle.setText(artist.getArtist());
        
        // artwork
        viewHolder.badge.setColorFilter(context.getColor(R.color.colorPrimary));
        viewHolder.albumArtwork.setImageURI(artist.getAlbumArtwork());
        if(viewHolder.albumArtwork.getDrawable() == null){
            viewHolder.albumArtwork.setImageDrawable(context.getDrawable(R.drawable.ic_artist));
        }
        
        viewHolder.itemView.setOnClickListener(view -> {
            onClickEvents.onItemClick(view, artist, position);
        });
    }

    @Override
    public int getItemCount() {
        int size;
        
        if(artists != null && artists.size() > 0){
            size = artists.size();
        }
        else size = 0;
        
        return size;
    }
    
    public void setOnClickEvents(OnClickEvents onClickEvents){
        this.onClickEvents = onClickEvents;
    }
    
    public interface OnClickEvents{
        void onItemClick(View view, ArtistsModel artist, int position);
        boolean onItemLongClick(View view, ArtistsModel artist, int position);
    }
}