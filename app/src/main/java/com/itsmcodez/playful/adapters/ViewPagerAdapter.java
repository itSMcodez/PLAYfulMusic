package com.itsmcodez.playful.adapters;

import android.app.Application;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.itsmcodez.playful.fragments.AlbumsFragment;
import com.itsmcodez.playful.fragments.ArtistsFragment;
import com.itsmcodez.playful.fragments.PlaylistsFragment;
import com.itsmcodez.playful.fragments.SongsFragment;
import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<ViewPagerAdapter.FragmentModel> fragmentation;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<ViewPagerAdapter.FragmentModel> fragmentation){
        super(fm);
        this.fragmentation = fragmentation;
    }
    
    @Override
    public int getCount() {
        return fragmentation.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentation.get(position).getFragment();
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        
        return fragmentation.get(position).getFragmentTitle();
    }
    
    /*public void addFragment(String fragmentTitle, Fragment fragment){
        fragmentTitles.add(fragmentTitle);
        fragments.add(fragment);
    }*/
    
    public static class FragmentModel {
        private String fragmentTitle;
        private Fragment fragment;
        
        public FragmentModel(String fragmentTitle, Fragment fragment){
            this.fragmentTitle = fragmentTitle;
            this.fragment = fragment;
        }
        
        public String getFragmentTitle(){
            return this.fragmentTitle;
        }
        
        public Fragment getFragment(){
            return this.fragment;
        }
    }
    
    public static class FragmentationViewModel extends AndroidViewModel {
        private MutableLiveData<ArrayList<ViewPagerAdapter.FragmentModel>> musicItemFragments;
        private FragmentationRepo fragmentationRepo;
        
        public FragmentationViewModel(Application application){
            super(application);
            fragmentationRepo = FragmentationRepo.getInstance();
            musicItemFragments = fragmentationRepo.getMusicItemFragments();
        }
        
        public MutableLiveData<ArrayList<ViewPagerAdapter.FragmentModel>> getMusicItemFragments(){
            return musicItemFragments;
        }
    }
    
    public static class FragmentationRepo{
        private static FragmentationRepo instance;
        private MutableLiveData<ArrayList<ViewPagerAdapter.FragmentModel>> musicItemFragments;
        private ArrayList<ViewPagerAdapter.FragmentModel> musicItemFragmentList;
        
        public FragmentationRepo(){
            
            // set musicItemFragments
            ViewPagerAdapter.FragmentModel songsFragment = new ViewPagerAdapter.FragmentModel("Songs", new SongsFragment());
            ViewPagerAdapter.FragmentModel albumsFragment = new ViewPagerAdapter.FragmentModel("Albums", new AlbumsFragment());
            ViewPagerAdapter.FragmentModel artistsFragment = new ViewPagerAdapter.FragmentModel("Artists", new ArtistsFragment());
            ViewPagerAdapter.FragmentModel playlistsFragment = new ViewPagerAdapter.FragmentModel("Playlists", new PlaylistsFragment());
            musicItemFragmentList = new ArrayList<>();
            musicItemFragmentList.add(songsFragment);
            musicItemFragmentList.add(albumsFragment);
            musicItemFragmentList.add(artistsFragment);
            musicItemFragmentList.add(playlistsFragment);
            musicItemFragments = new MutableLiveData<>(musicItemFragmentList);
        }
        
        public static FragmentationRepo getInstance(){
            if(instance == null){
                instance = new FragmentationRepo();
            }
            return instance;
        }
        
        public MutableLiveData<ArrayList<ViewPagerAdapter.FragmentModel>> getMusicItemFragments(){
            return musicItemFragments;
        }
    }
    
}
