package com.example.instagram.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.MessageDetailsActivity;
import com.example.instagram.Models.Message;
import com.example.instagram.Models.Users;
import com.example.instagram.R;
import com.example.instagram.databinding.MessagesSampleBinding;
import com.squareup.picasso.Picasso;

import java.net.UnknownServiceException;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageDetailsActivity.class);
                intent.putExtra("name", users.getName());
                intent.putExtra("profilePicture", users.getProfilePicture());
                intent.putExtra("userId", users.getUserId());
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
