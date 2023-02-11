package com.example.instagram.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.instagram.Adapters.CommentAdapter;
import com.example.instagram.Models.Comment;
import com.example.instagram.Models.Users;
import com.example.instagram.databinding.ActivityCommentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {
    ActivityCommentBinding binding;
    Intent intent;
    String postId;
    String postedBy;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<Comment> list = new ArrayList<>();
    String tag = "CommentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        intent = getIntent();
        postId = intent.getStringExtra("postId");
        postedBy = intent.getStringExtra("postedBy");
        workOnComment();
        CommentAdapter();



        database.getReference().child("Users")
                .child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Users users = snapshot.getValue(Users.class);
                            Picasso.get().load(users.getProfilePicture()).into(binding.profileImage);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




    }

    public void workOnComment(){

            binding.sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String bodyMessage = binding.edTypeMessage.getText().toString();
                    if (!bodyMessage.isEmpty()){
                        Comment comment = new Comment();
                        comment.setCommentBody(bodyMessage);
                        comment.setCommentedAt(new Date().getTime());
                        comment.setCommentedBy(FirebaseAuth.getInstance().getUid());
                        database.getReference().child("posts")
                                .child(postId)
                                .child("comments")
                                .push()
                                .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        database.getReference().child("posts")
                                                .child(postId)
                                                .child("commentCount")
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        int commentCount = 0;
                                                        if (snapshot.exists()){
                                                            commentCount = snapshot.getValue(Integer.class);
                                                        }
                                                        database.getReference()
                                                                .child("posts")
                                                                .child(postId)
                                                                .child("commentCount")
                                                                .setValue(commentCount + 1)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        binding.edTypeMessage.setText("");
                                                                        Log.d(tag, "Commented Successfully");
                                                                    }
                                                                });

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                    }
                                });
                    }else {
                        Log.d(tag,"Comment box is Empty");
                    }

                }
            });




    }

    public void CommentAdapter(){
        CommentAdapter adapter = new CommentAdapter(this, list);
        binding.commentMsgRv.setLayoutManager(new LinearLayoutManager(this));
        binding.commentMsgRv.setAdapter(adapter);

        database.getReference().child("posts")
                .child(postId)
                .child("comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Comment comment = dataSnapshot.getValue(Comment.class);
                            list.add(comment);
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


}