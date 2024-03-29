package com.example.instagram.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.Activities.MessageDetailsActivity;
import com.example.instagram.Models.Users;
import com.example.instagram.R;
import com.example.instagram.databinding.MessagesSampleBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    Context context;
    ArrayList<Users> list;

    public MessageAdapter(Context context, ArrayList<Users> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.messages_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
          Users users = list.get(position);
        Picasso.get().load(users.getProfilePicture()).placeholder(R.drawable.usee).into(holder.binding.profileImage);
        holder.binding.profileLName.setText(users.getName());

        holder.binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Opening Camera.......", Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(FirebaseAuth.getInstance().getUid() + users.getUserId())
                .orderByChild("timestamp")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                holder.binding.lastMessage.setText(dataSnapshot.child("message").getValue().toString());
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageDetailsActivity.class);
                intent.putExtra("name", users.getName());
                intent.putExtra("profilePicture", users.getProfilePicture());
                intent.putExtra("userId", users.getUserId());
                intent.putExtra("token", users.getToken());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MessagesSampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = MessagesSampleBinding.bind(itemView);
        }
    }
}
