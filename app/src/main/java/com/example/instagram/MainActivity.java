package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.instagram.Activities.LoginActivity;
import com.example.instagram.Activities.SignActivity;
import com.example.instagram.Fragments.HomeFragment;
import com.example.instagram.Fragments.NotificationFragment;
import com.example.instagram.Fragments.ProfileFragment;
import com.example.instagram.Fragments.ReelsFragment;
import com.example.instagram.Fragments.SearchFragment;
import com.example.instagram.Models.Online;
import com.example.instagram.Models.Users;
import com.example.instagram.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iammert.library.readablebottombar.ReadableBottomBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    String currentUserId;
    String  name , userName, profilePicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        currentUserId = auth.getUid();

        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.replace(R.id.frameLayout, new HomeFragment());

        transaction1.commit();


        binding.bottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i) {
                    case 0:
                        transaction.replace(R.id.frameLayout, new HomeFragment());


                        break;
                    case 1:
                        transaction.replace(R.id.frameLayout, new SearchFragment());
                        break;

                    case 2:
                        transaction.replace(R.id.frameLayout, new ReelsFragment());
                        break;

                    case 3:
                        transaction.replace(R.id.frameLayout, new NotificationFragment());
                        break;

                    case 4:
                        transaction.replace(R.id.frameLayout, new ProfileFragment());

                        break;


                }
//                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}