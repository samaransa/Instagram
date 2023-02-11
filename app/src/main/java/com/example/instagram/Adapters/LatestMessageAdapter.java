package com.example.instagram.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.Models.Chatting;
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

public class LatestMessageAdapter extends RecyclerView.Adapter<LatestMessageAdapter.ViewHolder> {
    Context context;
    ArrayList<Chatting> list;
    Users users;

    public LatestMessageAdapter(Context context, ArrayList<Chatting> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LatestMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.messages_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LatestMessageAdapter.ViewHolder holder, int position) {
        Chatting chatting = list.get(position);
        holder.binding.lastMessage.setText(chatting.getMessage());
        String chatPartnerId;
        if (FirebaseAuth.getInstance().getUid().equals(chatting.getFromId())){
            chatPartnerId = chatting.getToId();

        }else {
            chatPartnerId = chatting.getFromId();
        }

        FirebaseDatabase.getInstance().getReference().child("Users").child(chatPartnerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    users = snapshot.getValue(Users.class);
                    Picasso.get().load(users.getProfilePicture()).into(holder.binding.profileImage);
                    holder.binding.profileLName.setText(users.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
