package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.instagram.Adapters.LatestMessageAdapter;
import com.example.instagram.Adapters.MessageAdapter;
import com.example.instagram.Adapters.OnlineAdapter;
import com.example.instagram.Models.Chatting;
import com.example.instagram.Models.Online;
import com.example.instagram.Models.Users;
import com.example.instagram.databinding.ActivityLatestMessageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LatestMessageActivity extends AppCompatActivity {
    ActivityLatestMessageBinding binding;
    ArrayList<Online> storyList = new ArrayList<>();
    ArrayList<Chatting>  latestMsgList = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;
    LatestMessageAdapter adapter;
    Map<String, Chatting> map = new HashMap<>();







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLatestMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
//        listenForLatestMessage();

//        listenForLatestMessage();
        intentMethod();
        childAdd();

    }
    private void intentMethod(){
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





    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public  void  childAdd(){

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
                latestMsgList.add(0, chatting);
                map.put(snapshot.getKey(), chatting);
//                refresh();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Chatting chatting = snapshot.getValue(Chatting.class);
                chatting.setFromId(snapshot.getKey());
                latestMsgList.add(0, chatting);
                map.put(snapshot.getKey(), chatting);
                refresh();
                adapter.notifyDataSetChanged();
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

    public void refresh(){
        latestMsgList.clear();
        for (Chatting chatting : map.values()){
            latestMsgList.add(0, chatting);
            adapter.notifyDataSetChanged();
        }
    }







}