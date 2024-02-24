package com.itsmcodez.playful;

import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itsmcodez.playful.databinding.ActivityMainBinding;
import com.itsmcodez.playful.fragments.MusicFragment;
import com.itsmcodez.playful.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate and get instance of binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // set content view to binding's root
        setContentView(binding.getRoot());

        // ActionBar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(R.string.activity_main_action_bar_title);
        getSupportActionBar().setSubtitle(R.string.activity_main_action_bar_subtitle);

        // Default screen
        replaceFragment(new MusicFragment());
        
        // BottomNavigation
        binding.bottomNavBar.setOnItemSelectedListener(
                new BottomNavigationView.OnItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                    
                        if(item.getItemId() == R.id.music_menu_item){
                            replaceFragment(new MusicFragment());
                        }
                    
                        if(item.getItemId() == R.id.settings_menu_item){
                            replaceFragment(new SettingsFragment());
                        }
                    
                        return true;
                    }
                });
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
    
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, fragment);
        fragmentTransaction.commit();
    }
}
