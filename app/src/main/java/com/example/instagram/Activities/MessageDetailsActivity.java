package com.example.instagram.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.instagram.Adapters.ChattingAdapter;
import com.example.instagram.Models.Chatting;
import com.example.instagram.Models.Online;
import com.example.instagram.Models.Users;
import com.example.instagram.R;
import com.example.instagram.Services.FcmNotificationsSender;
import com.example.instagram.databinding.ActivityFreindsDetailsBinding;
import com.example.instagram.databinding.ActivityMessageDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MessageDetailsActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    String senderId;
    String receiverId;
    ActivityMessageDetailsBinding binding;
    String tag = "MessageDetailsActivity";
    String token , name, userName;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageDetailsBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = auth.getCurrentUser();


        senderId = auth.getUid();
        receiverId = getIntent().getStringExtra("userId");
        name = getIntent().getStringExtra("name");
        String profilePicture = getIntent().getStringExtra("profilePicture");
        token = getIntent().getStringExtra("token");

        binding.name.setText(name);
        Picasso.get().load(profilePicture).into(binding.profileImage);
        allMessagingTask();

        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Users users = snapshot.getValue(Users.class);
                    userName = users.getName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        binding.edTypeMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String message = binding.edTypeMessage.getText().toString();
                if(!message.isEmpty()){
                    binding.sendBtn.setVisibility(View.VISIBLE);
                    binding.microPhone.setVisibility(View.INVISIBLE);
                    binding.openGalley.setVisibility(View.INVISIBLE);
                    binding.sticker.setVisibility(View.INVISIBLE);
                    binding.edTypeMessage.setMaxWidth(300);
                    binding.inIcon.setImageResource(R.drawable.search_three);
                    updateTypingStatus("typing...");
                }else {
                    binding.sendBtn.setVisibility(View.INVISIBLE);
                    binding.microPhone.setVisibility(View.VISIBLE);
                    binding.openGalley.setVisibility(View.VISIBLE);
                    binding.sticker.setVisibility(View.VISIBLE);
                    binding.inIcon.setImageResource(R.drawable.camera_full);
                    updateTypingStatus("");
                }

            }



            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        binding.microPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MessageDetailsActivity.this, "Hello sir", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MessageDetailsActivity.this, LoginActivity.class);
//                startActivity(intent);



            }
        });



        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setContentView(binding.getRoot());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void allMessagingTask(){
        final ArrayList<Chatting> messageModals = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.messagesRecyclerView.setLayoutManager(layoutManager);

        final ChattingAdapter chattingAdapter = new ChattingAdapter(messageModals, this);
        binding.messagesRecyclerView.setAdapter(chattingAdapter);
        final  String senderRoom = senderId+receiverId;
        final String receiverRoom = receiverId+senderId;


        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.edTypeMessage.getText().toString();
                final Chatting modal = new Chatting(senderId, message);
                modal.setTimestamp(new Date().getTime());
                database.getReference().child("chats").child(senderRoom).push()
                        .setValue(modal)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("chats")
                                        .child(receiverRoom).push()
                                        .setValue(modal)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                Log.d(tag, "Message Successfully gone");
                                                binding.edTypeMessage.setText("");

                                                if (!token.equals("")){
                                                    FcmNotificationsSender sender = new FcmNotificationsSender(token,
                                                            userName, modal.getMessage(), getApplicationContext(), MessageDetailsActivity.this);
                                                    sender.SendNotifications();
                                                    Log.d(tag, "Notification send successfully.");

                                                }else {
                                                    Log.d(tag, "Token is empty.");
                                                }

                                                Chatting chattingClass = new Chatting(senderId, message, receiverId);
                                                chattingClass.setTimestamp(modal.getTimestamp());



                                                database.getReference().child("latest-message")
                                                        .child(senderId).child(receiverId)
                                                        .setValue(chattingClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                database.getReference().child("latest-message")
                                                                        .child(receiverId).child(senderId)
                                                                        .setValue(chattingClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {

                                                                            }
                                                                        });

                                                            }
                                                        });



                                            }
                                        });

                            }
                        });

            }
        });

        database.getReference().child("chats")
                .child(senderRoom).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModals.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Chatting modal = dataSnapshot.getValue(Chatting.class);
                            modal.setMessageId(dataSnapshot.getKey());
                            messageModals.add(modal);
                        }
                        chattingAdapter.notifyDataSetChanged();

                        binding.messagesRecyclerView.smoothScrollToPosition(binding.messagesRecyclerView.getAdapter().getItemCount()); // it will automatically scroll to the view position.



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void  updateTypingStatus(String value){
        Online online = new Online();
        online.setTypingStatus(value);
        database.getReference().child("Users").child(auth.getUid())
                .child("userState").child("typingStatus").setValue(online).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });


        database.getReference().child("Users").child(receiverId).child("userState")
                .child("typingStatus").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Online online1 = snapshot.getValue(Online.class);
                            binding.typeStatus.setText(online1.getTypingStatus());
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