package com.example.instagram.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.instagram.Fragments.EmailFragment;
import com.example.instagram.Fragments.PhoneNumber;

public class ViewPagerAdapter extends FragmentStateAdapter {

    String [] titles = {"PHONE NUMBER", "EMAIL ADDRESS"};

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new PhoneNumber();
            case 1: return new EmailFragment();
        }
        return new PhoneNumber();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
