package com.example.instagram.ProfileFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram.Adapters.ProfileFragmentPhotoUploadedAdapter;
import com.example.instagram.EditProfileActivity;
import com.example.instagram.Models.Discover;
import com.example.instagram.Models.Posts;
import com.example.instagram.databinding.FragmentGridBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class GridFragment extends Fragment {
   FragmentGridBinding binding;
   ArrayList<Discover> list = new ArrayList<>();
   FirebaseDatabase database;
   FirebaseAuth auth;
   ArrayList<Posts> postlist = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentGridBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        uploadedPicture();
        binding.horizontalScrollView.setHorizontalScrollBarEnabled(false);
        onClick();



        return binding.getRoot();
    }

    public void uploadedPicture(){
        binding.uploadedPhotoRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.uploadedPhotoRV.setNestedScrollingEnabled(false);
        ProfileFragmentPhotoUploadedAdapter adapter = new ProfileFragmentPhotoUploadedAdapter(getContext(), postlist);
        binding.uploadedPhotoRV.setAdapter(adapter);
        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postlist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Posts post = dataSnapshot.getValue(Posts.class);
                    post.setPostId(dataSnapshot.getKey());
                    if (post.getPostedBy().equals(FirebaseAuth.getInstance().getUid())){
                        postlist.add(post);
                    }
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void onClick(){
        binding.btnEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.btnAddBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }





}