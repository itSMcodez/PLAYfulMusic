package com.itsmcodez.playful.fragments;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import com.itsmcodez.playful.databinding.FragmentAlbumsBinding;

public class AlbumsFragment extends Fragment {
    private FragmentAlbumsBinding binding;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Bind to views
        binding = FragmentAlbumsBinding.inflate(inflater, container, false);
        
        return binding.getRoot();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }
    
}
