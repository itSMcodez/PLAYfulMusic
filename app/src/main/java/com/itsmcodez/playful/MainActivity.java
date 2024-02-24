
package com.itsmcodez.playful;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.itsmcodez.playful.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate and get instance of binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // set content view to binding's root
        setContentView(binding.getRoot());
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
    
}
