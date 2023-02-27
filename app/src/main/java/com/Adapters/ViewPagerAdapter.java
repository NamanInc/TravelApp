package com.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.Fragments.HomeFragment;
import com.Fragments.NotificationFragemt;
import com.Fragments.Posts;
import com.Fragments.SearchFragment;
import com.Fragments.Trending;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new Trending();

            case 2:
                return new NotificationFragemt();

            case 3:
                return new Posts();

            case 4:
                return new SearchFragment();
            default:
                return new HomeFragment();
        }

    }

    @Override
    public int getCount() {
        return 5;
    }
}
