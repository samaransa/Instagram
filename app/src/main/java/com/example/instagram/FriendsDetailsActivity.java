package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.instagram.Fragments.ProfileFragment;
import com.example.instagram.Models.Followers;
import com.example.instagram.Models.Users;
import com.example.instagram.Models.UsersStories;
import com.example.instagram.databinding.ActivityFreindsDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class FriendsDetailsActivity extends AppCompatActivity {
    ActivityFreindsDetailsBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String userId;
    String tag = "FriendsDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFreindsDetailsBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        String userName = getIntent().getStringExtra("username");
        String name = getIntent().getStringExtra("name");
        String bio = getIntent().getStringExtra("bio");
        String profilePicture = getIntent().getStringExtra("profilePicture");
        userId = getIntent().getStringExtra("userId");
        int followersCount = getIntent().getIntExtra("followersCount", 0);
        int followingsCount = getIntent().getIntExtra("followingCount", 0);
        binding.name.setText(name);  // taking Name.
        binding.userName.setText(userName); // taking username;
        binding.bio.setText(bio); // taking bio
        Picasso.get().load(profilePicture).into(binding.profileImage); // loading profile image.

        takingDataFromFireBaseData(); //  calling method .


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




        database.getReference().child("Users")
                .child(userId)
                .child("followers")
                .child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            binding.followBtn.setBackground(ContextCompat.getDrawable(FriendsDetailsActivity.this, R.drawable.follow_active_btn_bg));
                            binding.followBtn.setText("Following");
                            binding.followBtn.setTextColor(FriendsDetailsActivity.this.getResources().getColor(R.color.black));
                            binding.followBtn.setEnabled(false);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





        binding.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Followers follow = new Followers();
                follow.setFollowedById(FirebaseAuth.getInstance().getUid());
                follow.setFollowedAt(new Date().getTime());
                database.getReference().child("Users")
                        .child(userId)
                        .child("followers")
                        .child(auth.getUid())
                        .setValue(follow)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("Users")
                                        .child(userId)
                                        .child("followersCount")
                                        .setValue(followersCount  + 1)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(FriendsDetailsActivity.this, "You Followed " + name, Toast.LENGTH_SHORT).show();
                                                Log.d(tag, "You Followed" + name);
                                                binding.followBtn.setBackground(ContextCompat.getDrawable(FriendsDetailsActivity.this, R.drawable.follow_active_btn_bg));
                                                binding.followBtn.setText("Following");
                                                binding.followBtn.setTextColor(FriendsDetailsActivity.this.getResources().getColor(R.color.black));
                                                database.getReference().child("Users")
                                                        .child(auth.getUid())
                                                        .child("followings")
                                                        .child(userId)
                                                        .setValue(follow)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                                /*

                                                                This is not working

                                                                database.getReference().child("Users")
                                                                        .child(auth.getUid())
                                                                        .child("followingCount")
                                                                        .setValue(1 + followingsCount )
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {

                                                                            }
                                                                        });


                                                                 */

                                                            }
                                                        });
                                            }
                                        });
                            }
                        });

            }
        });

        setContentView(binding.getRoot());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    // taking data from firebase data like postCount , followerCount, and followingCount for users.
    public void takingDataFromFireBaseData(){

        // for getting Post Count
        database.getReference().child("usersPostedImages").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            binding.postCount.setText(snapshot.getChildrenCount()+"");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // for getting following Count.
        database.getReference().child("Users").child(userId)
                .child("followings").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            binding.followingCount.setText(snapshot.getChildrenCount() + "");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        // Getting Followers Count.
        database.getReference().child("Users")
                .child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Users users = snapshot.getValue(Users.class);
                            binding.followerCount.setText(users.getFollowersCount()+"");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}