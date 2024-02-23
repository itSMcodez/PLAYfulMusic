package com.itsmcodez.playful;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.itsmcodez.playful.databinding.ActivityPermissionsBinding;

public class PermissionsActivity extends AppCompatActivity {
    private ActivityPermissionsBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind to views
        binding = ActivityPermissionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
