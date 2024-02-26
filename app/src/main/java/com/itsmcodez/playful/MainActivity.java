package com.itsmcodez.playful;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itsmcodez.playful.databinding.ActivityMainBinding;
import com.itsmcodez.playful.fragments.AlbumsFragment;
import com.itsmcodez.playful.fragments.ArtistsFragment;
import com.itsmcodez.playful.fragments.PlaylistsFragment;
import com.itsmcodez.playful.fragments.SettingsFragment;
import com.itsmcodez.playful.fragments.SongsFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MenuItem sortMenuItem;

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
        replaceFragment(new SongsFragment());
        
        // BottomNavigation
        binding.bottomNavBar.setOnItemSelectedListener(
                new BottomNavigationView.OnItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                    
                        if(item.getItemId() == R.id.songs_menu_item){
                            replaceFragment(new SongsFragment());
                            if(!sortMenuItem.isVisible()){
                                sortMenuItem.setVisible(true);
                            }
                            if(binding.miniController.getVisibility() != View.VISIBLE){
                                binding.miniController.setVisibility(View.VISIBLE);
                            }
                        }
                    
                        if(item.getItemId() == R.id.albums_menu_item){
                            replaceFragment(new AlbumsFragment());
                            if(sortMenuItem.isVisible()){
                                sortMenuItem.setVisible(false);
                            }
                            if(binding.miniController.getVisibility() != View.VISIBLE){
                                binding.miniController.setVisibility(View.VISIBLE);
                            }
                        }
                    
                        if(item.getItemId() == R.id.artists_menu_item){
                            replaceFragment(new ArtistsFragment());
                            if(sortMenuItem.isVisible()){
                                sortMenuItem.setVisible(false);
                            }
                            if(binding.miniController.getVisibility() != View.VISIBLE){
                                binding.miniController.setVisibility(View.VISIBLE);
                            }
                        }
                    
                        if(item.getItemId() == R.id.playlists_menu_item){
                            replaceFragment(new PlaylistsFragment());
                            if(sortMenuItem.isVisible()){
                                sortMenuItem.setVisible(false);
                            }
                            if(binding.miniController.getVisibility() != View.VISIBLE){
                                binding.miniController.setVisibility(View.VISIBLE);
                            }
                        }
                    
                        if(item.getItemId() == R.id.settings_menu_item){
                            replaceFragment(new SettingsFragment());
                            if(sortMenuItem.isVisible()){
                                sortMenuItem.setVisible(false);
                            }
                            if(binding.miniController.getVisibility() != View.GONE){
                                binding.miniController.setVisibility(View.GONE);
                            }
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_menu, menu);
        
        // assign sort_menu_item to sortMenuItem
        sortMenuItem = menu.findItem(R.id.sort_menu_item);
        
        return super.onCreateOptionsMenu(menu);
    }
    
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, fragment);
        fragmentTransaction.commit();
    }
}
