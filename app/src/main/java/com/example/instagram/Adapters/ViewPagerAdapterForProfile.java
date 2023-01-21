package com.example.instagram.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.instagram.ProfileFragments.ContactFragment;
import com.example.instagram.ProfileFragments.GridFragment;
import com.example.instagram.R;

public class ViewPagerAdapterForProfile extends FragmentStateAdapter {
    int [] images = {R.drawable.grid, R.drawable.con_main};
    public ViewPagerAdapterForProfile(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new GridFragment();
            case 1: return new ContactFragment();
        }
        return new GridFragment();
    }

    @Override
    public int getItemCount() {
        return images.length;
    }
}
