package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.instagram.Adapters.MessageAdapter;
import com.example.instagram.Adapters.OnlineAdapter;
import com.example.instagram.Models.Online;
import com.example.instagram.Models.Users;
import com.example.instagram.databinding.ActivityMessageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    ActivityMessageBinding binding;
    ArrayList<Online> list = new ArrayList<>();
    ArrayList<Users> arrayList = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        whoIsOnlineAdapter();
        messagesAdapter();
    }

    public void whoIsOnlineAdapter(){

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        list.add(new Online("baby_naz", R.drawable.brunetee ));
        list.add(new Online("range_rox", R.drawable.chris ));
//        list.add(new Online("son_pari", R.drawable.modelgirl ));
        binding.activatedFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.activatedFriendsRecyclerView.setNestedScrollingEnabled(false);
        binding.horizontalSv.setHorizontalScrollBarEnabled(false);
        binding.activatedFriendsRecyclerView.setAdapter(new OnlineAdapter(this, list));

    }

    public void messagesAdapter(){



        binding.messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.messageRecyclerView.setNestedScrollingEnabled(false);
        MessageAdapter adapter = new MessageAdapter(this, arrayList);
        binding.messageRecyclerView.setAdapter(adapter);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    if (!dataSnapshot.getKey().equals(auth.getUid())){
                        arrayList.add(users);
                    }

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
}