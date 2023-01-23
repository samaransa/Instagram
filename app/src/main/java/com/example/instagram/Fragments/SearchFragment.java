package com.example.instagram.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.instagram.ProfileFragments.BottomSheetFragment;
import com.example.instagram.R;
import com.example.instagram.databinding.FragmentSearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;


public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    LottieAnimationView view;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentSearchBinding.inflate(inflater, container, false);
        String Tag = "First Log";
        view = binding.animationView;
        binding.summit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Work in progress", Toast.LENGTH_SHORT).show();
                Log.d("Test", "Work in Progress");
                Log.d(Tag, "My First Log Message ");
                Log.e("error", "this is error");
                // Java Method.
                view.setAnimation(R.raw.dollar_coin);
                view.playAnimation();
                view.loop(true);

            }
        });






        return binding.getRoot();
    }



}