package com.example.instagram.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagram.Adapters.ViewPagerAdapterForProfile;
import com.example.instagram.BottomSheets.EditProfileBottomSheetFragment;
import com.example.instagram.R;
import com.example.instagram.databinding.FragmentReelsBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class ReelsFragment extends Fragment {
    FragmentReelsBinding binding;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentReelsBinding.inflate(inflater, container, false);
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProfileFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.constraintLayoutFirstReels, fragment).addToBackStack(null);
                fragmentTransaction.commit();
                binding.button.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "This fragment is not work in progress yet.", Toast.LENGTH_SHORT).show();
            }
        });





        /*

         // This code is not working on the edit  text;

        binding.ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams prams = binding.ed.getLayoutParams();
                prams.width = 100;
            }
        });

         */

        return binding.getRoot();
    }


}