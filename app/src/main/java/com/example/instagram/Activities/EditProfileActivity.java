package com.example.instagram.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.instagram.BottomSheets.EditProfileBottomSheetFragment;
import com.example.instagram.Fragments.HomeFragment;
import com.example.instagram.Fragments.ProfileFragment;
import com.example.instagram.Models.Users;
import com.example.instagram.ProfileFragments.BottomSheetFragment;
import com.example.instagram.R;
import com.example.instagram.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.nio.file.attribute.UserDefinedFileAttributeView;

public class EditProfileActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    ActivityResultLauncher<String> launcher;
    ActivityEditProfileBinding binding;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();



        // calling update Method
        update();
        onClick();


        bottomSheet();
        // Fetching profile image from database
        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Users users = snapshot.getValue(Users.class);
                    Picasso.get().load(users.getProfilePicture()).placeholder(R.drawable.usee).into(binding.profileImage);
                    binding.edName.setText(users.getName());
                    binding.edUserName.setText(users.getUsername());
                    binding.edBio.setText(users.getBio());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void onClick(){
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            onBackPressed();

            }
        });
    }


    public  void update(){
        binding.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.done.setVisibility(View.INVISIBLE);
                Users users =  new Users();
                String name = binding.edName.getText().toString();
                String username = binding.edUserName.getText().toString();
                String bio =   binding.edBio.getText().toString();
                users.setName(name);
                users.setUsername(username);
                users.setBio(bio);
                database.getReference().child("Users").child(auth.getUid()).child("name").setValue(name)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                binding.progressBar.setVisibility(View.INVISIBLE);
                                binding.done.setVisibility(View.VISIBLE);
                            }
                        });

                database.getReference().child("Users").child(auth.getUid()).child("username").setValue(username)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                binding.progressBar.setVisibility(View.INVISIBLE);
                                binding.done.setVisibility(View.VISIBLE);
                            }
                        });

                database.getReference().child("Users").child(auth.getUid()).child("bio").setValue(bio)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                binding.progressBar.setVisibility(View.INVISIBLE);
                                binding.done.setVisibility(View.VISIBLE);
                            }
                        });
            }
        });
    }

    public void bottomSheet() {
        binding.txtEditPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileBottomSheetFragment sheetFragment = new EditProfileBottomSheetFragment();
                sheetFragment.show(getSupportFragmentManager(), sheetFragment.getTag());
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileBottomSheetFragment sheetFragment = new EditProfileBottomSheetFragment();
                sheetFragment.show(getSupportFragmentManager(), sheetFragment.getTag());

            }
        });

        binding.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileBottomSheetFragment sheetFragment = new EditProfileBottomSheetFragment();
                sheetFragment.show(getSupportFragmentManager(), sheetFragment.getTag());

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Not working properly i see this later in work.
    public void refreshImage(){
        final Handler refreshHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do updates for imageview
                refreshHandler.postDelayed(this, 1000);
            }
        };
        refreshHandler.postDelayed(runnable, 1000);
    }



}