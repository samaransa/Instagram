package com.example.instagram.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.instagram.ProfileFragments.ContactFragment;
import com.example.instagram.ProfileFragments.GridFragment;
import com.example.instagram.ProfileFragments.UpdateAvatarFragment;
import com.example.instagram.ProfileFragments.UpdateProfilePictureFragment;
import com.example.instagram.R;

public class ViewPagerAdapterForEditProfilePicture extends FragmentStateAdapter {
    int [] images = {R.drawable.profile_picture, R.drawable.avatar_old_man};
    public ViewPagerAdapterForEditProfilePicture(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new UpdateProfilePictureFragment();
            case 1: return new UpdateAvatarFragment();
        }
        return new UpdateProfilePictureFragment();
    }

    @Override
    public int getItemCount() {
        return images.length;
    }
}
