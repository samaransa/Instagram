package com.example.instagram.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.instagram.Adapters.LatestMessageAdapter;
import com.example.instagram.Adapters.OnlineAdapter;
import com.example.instagram.Models.Chatting;
import com.example.instagram.Models.Online;
import com.example.instagram.Models.Users;
import com.example.instagram.databinding.ActivityLatestMessageBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LatestMessageActivity extends AppCompatActivity {
    ActivityLatestMessageBinding binding;
    ArrayList<Online> onlineStatusList = new ArrayList<>();
    ArrayList<Chatting> latestMsgList = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;
    LatestMessageAdapter adapter;
    Map<String, Chatting> map = new HashMap<>();
    FirebaseUser currentUser;
    String currentUserId;
    String name, profilePicture, userId, username;

    String tag = "LatestMessageActivity";
    Chatting chatting;
    Map<String , Chatting> latestMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLatestMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        currentUserId = auth.getUid();
//        listenForLatestMessage();
//        Intent intent = getIntent();
        name = getIntent().getStringExtra("name");
        profilePicture = getIntent().getStringExtra("profilePicture");
        username = getIntent().getStringExtra("username");
        userId = getIntent().getStringExtra("userId");


        chileEvenList();
//        listenForLatestMessage();
        intentMethod();
        onlineStatusWork();
        fetchingDataFromFirebase();
//

    }

    private void intentMethod() {
        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LatestMessageActivity.this, MessageActivity.class);
                startActivity(intent);
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }

        });
    }

    public void listenForLatestMessage() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.messageRv.setLayoutManager(layoutManager);
        adapter = new LatestMessageAdapter(this, latestMsgList);
        binding.messageRv.setNestedScrollingEnabled(false);
        binding.messageRv.setAdapter(adapter);


        database.getReference().child("latest-message").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                latestMsgList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chatting chatting = dataSnapshot.getValue(Chatting.class);
                    chatting.setFromId(dataSnapshot.getKey());
                    latestMsgList.add(chatting);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onlineStatusWork() {
        LinearLayoutManager layoutManage = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.onlineStatusRv.setLayoutManager(layoutManage);
        binding.onlineStatusRv.setNestedScrollingEnabled(false);
        binding.horizontalSv.setHorizontalScrollBarEnabled(false);
        OnlineAdapter onlineAdapter = new OnlineAdapter(this, onlineStatusList);
        binding.onlineStatusRv.setAdapter(onlineAdapter);
        database.getReference().child("UserState").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                onlineStatusList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Online online = dataSnapshot.getValue(Online.class);
                    online.setUserId(dataSnapshot.getKey());
                    if (!dataSnapshot.getKey().equals(auth.getUid())) {
                        onlineStatusList.add(online);

                    }
                }
                onlineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void fetchingDataFromFirebase() {
        database.getReference().child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Users users = snapshot.getValue(Users.class);
                    binding.userName.setText(users.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser != null) {
            updateUserStatus(currentUserId, "online");
            onlineState(username, profilePicture);
        } else {
            updateUserStatus("", "offline");
            onlineState(null, null);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (currentUser != null) {
            updateUserStatus("", "offline");
            onlineState(null, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentUser != null) {
            updateUserStatus("", "offline");
            onlineState(null, null);
        }
    }

    private void updateUserStatus(String userId, String state) {
        String saveCurrentTime, saveCurrentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());


        Users users = new Users();
        users.setUserId(userId);
        users.setState(state);
        users.setDate(saveCurrentDate);
        users.setTime(saveCurrentTime);


       /*
        Users users= new Users(currentUserId, saveCurrentDate ,"online", saveCurrentTime);
        */

        /*
        HashMap<String, Object> onlineStateMap = new HashMap<>();
        onlineStateMap.put("time", saveCurrentTime);
        onlineStateMap.put("date", saveCurrentDate);
        onlineStateMap.put("state", state);
        database.getReference().child("Users").child(currentUserId).child("userState")
                .updateChildren(onlineStateMap);

         */

        database.getReference().child("Users").child(currentUserId).child("userState").setValue(users)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {


                    }
                });


    }


    private void onlineState(String username, String profilePicture) {


        Online online = new Online();
        online.setUsername(username);
        online.setProfilePicture(profilePicture);
        database.getReference().child("UserState").child(currentUserId).setValue(online).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void chileEvenList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.messageRv.setLayoutManager(layoutManager);
        adapter = new LatestMessageAdapter(this, latestMsgList);
        binding.messageRv.setNestedScrollingEnabled(false);
        binding.messageRv.setAdapter(adapter);
        database.getReference().child("latest-message").child(auth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Chatting chatting = snapshot.getValue(Chatting.class);
                chatting.setFromId(snapshot.getKey());
                latestMsgList.add(0 , chatting);
                map.put(snapshot.getKey(), chatting);
                refreshRecyclerView();




            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                latestMsgList.clear();
                chatting = snapshot.getValue(Chatting.class);
                chatting.setFromId(snapshot.getKey());
                latestMsgList.add(0 , chatting);
                map.put(chatting.getFromId(), chatting);
                refreshRecyclerView();





            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void refreshRecyclerView(){
        latestMsgList.clear();
        for (Chatting chatting1 : map.values()){
            latestMsgList.add(0 , chatting1);
        }
        adapter.notifyDataSetChanged();
    }



    private void refresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                latestMsgList.clear();
              for (Chatting chatting1 : map.values()){
                  latestMsgList.add(chatting1);
                  adapter.notifyDataSetChanged();
              }

              binding.swipeRefreshLayout.setRefreshing(false);

            }
        });
    }






}