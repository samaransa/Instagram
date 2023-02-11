package com.example.instagram.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.Activities.CommentActivity;
import com.example.instagram.Models.Posts;
import com.example.instagram.Models.Users;
import com.example.instagram.R;
import com.example.instagram.databinding.PostedImageSampleBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    Context context;
    ArrayList<Posts> list = new ArrayList<>();
    String tag = "postAdapter";

    public PostAdapter(Context context, ArrayList<Posts> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.posted_image_sample, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Posts post = list.get(position);
        Picasso.get().load(post.getPostImage()).into(holder.binding.postImage);
        holder.binding.likeCount.setText(post.getPostLike() + "");


        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPostedBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get().load(users.getProfilePicture()).into(holder.binding.profilePic);
                        holder.binding.userName.setText(users.getUsername());
                        holder.binding.copyUserName.setText(users.getUsername());
                        Log.d(tag, "Getting all user details");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        // Working for likes button;


        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(post.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            holder.binding.like.setImageResource(R.drawable.red_heart);
                            holder.binding.like.setEnabled(true);

                        }
                        else {
                            holder.binding.like.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    holder.binding.like.setImageResource(R.drawable.red_heart);
                                    FirebaseDatabase.getInstance().getReference().child("posts")
                                            .child(post.getPostId())
                                            .child("likes")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("posts")
                                                            .child(post.getPostId())
                                                            .child("postLike")
                                                            .setValue(post.getPostLike() + 1)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Log.d(tag, "You just liked " + post.getPostId());
                                                                }
                                                            });


                                                }
                                            });
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        holder.binding.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("postedBy", post.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PostedImageSampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = PostedImageSampleBinding.bind(itemView);
        }
    }
}
