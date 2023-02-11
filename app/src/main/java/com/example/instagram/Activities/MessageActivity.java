package com.example.instagram.Activities;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Struct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MessageActivity extends AppCompatActivity {
    ActivityMessageBinding binding;
    ArrayList<Users> arrayList = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        messagesAdapter();
        intentMethod();
    }


    public void messagesAdapter(){


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        layoutManager.setStackFromEnd(true);
//        binding.messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setNestedScrollingEnabled(false);
        MessageAdapter adapter = new MessageAdapter(this, arrayList);
        binding.recyclerView.setAdapter(adapter);

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


    public void intentMethod(){
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser !=null){
            updateUserStatus(auth.getUid(), "online");
        }else {
            updateUserStatus("", "offline");


        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (currentUser !=null){
            updateUserStatus("", "offline");

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentUser !=null){
            updateUserStatus("", "offline");

        }
    }

    private void updateUserStatus(String userId, String state){
        String saveCurrentTime, saveCurrentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime= new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());


        Users users = new Users();
        users.setUserId(userId);
        users.setState(state);
        users.setDate(saveCurrentDate);
        users.setTime(saveCurrentTime);



        database.getReference().child("Users").child(auth.getUid()).child("userState").setValue(users)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {



                    }
                });


    }




}