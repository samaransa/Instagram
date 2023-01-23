package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.Toast;

import com.example.instagram.Adapters.ChattingAdapter;
import com.example.instagram.Models.Chatting;
import com.example.instagram.databinding.ActivityMessageDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class MessageDetailsActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    String senderId;
    String receiverId;
    ActivityMessageDetailsBinding binding;
    String tag = "MessageDetailsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageDetailsBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        senderId = auth.getUid();
        receiverId = getIntent().getStringExtra("userId");
        String name = getIntent().getStringExtra("name");
        String profilePicture = getIntent().getStringExtra("profilePicture");

        binding.name.setText(name);
        Picasso.get().load(profilePicture).into(binding.profileImage);
        allMessagingTask();



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
                }else {
                    binding.sendBtn.setVisibility(View.INVISIBLE);
                    binding.microPhone.setVisibility(View.VISIBLE);
                    binding.openGalley.setVisibility(View.VISIBLE);
                    binding.sticker.setVisibility(View.VISIBLE);
                    binding.inIcon.setImageResource(R.drawable.camera_full);
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
        binding.messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                        messageModals.clear();;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Chatting modal = dataSnapshot.getValue(Chatting.class);
                            modal.setMessageId(dataSnapshot.getKey());
                            messageModals.add(modal);
                        }
                        chattingAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}