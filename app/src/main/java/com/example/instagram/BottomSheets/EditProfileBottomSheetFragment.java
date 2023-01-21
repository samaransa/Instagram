package com.example.instagram.BottomSheets;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.instagram.Adapters.ViewPagerAdapterForEditProfilePicture;
import com.example.instagram.Adapters.ViewPagerAdapterForProfile;
import com.example.instagram.ProfileFragments.BottomSheetFragment;
import com.example.instagram.R;
import com.example.instagram.databinding.FragmentEditPofileBottomSheetBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class EditProfileBottomSheetFragment extends BottomSheetFragment {

      FragmentEditPofileBottomSheetBinding binding;
    int [] images = {R.drawable.profile_picture, R.drawable.avatar_old_man};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = FragmentEditPofileBottomSheetBinding.inflate(inflater, container, false);
         binding.viewpager.setAdapter(new ViewPagerAdapterForEditProfilePicture((FragmentActivity) getContext()));
         new TabLayoutMediator(binding.tabLayout2, binding.viewpager, (((tab, position) -> tab.setIcon(images[position])))).attach();
         return binding.getRoot();
    }



}