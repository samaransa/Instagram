package com.example.instagram.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.FriendsDetailsActivity;
import com.example.instagram.Models.Discover;
import com.example.instagram.Models.Users;
import com.example.instagram.R;
import com.example.instagram.databinding.DiscoverPeopleSampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.ViewHolder> {
    Context context;
    ArrayList<Users> list;

    public DiscoverAdapter(Context context, ArrayList<Users> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DiscoverAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.discover_people_sample, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverAdapter.ViewHolder holder, int position) {
        Users users = list.get(position);
        Picasso.get().load(users.getProfilePicture()).into(holder.binding.profileImage);
        holder.binding.profileName.setText(users.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FriendsDetailsActivity.class);
                intent.putExtra("userId", users.getUserId());
                intent.putExtra("username", users.getUsername());
                intent.putExtra("name", users.getName());
                intent.putExtra("bio", users.getBio());
                intent.putExtra("profilePicture", users.getProfilePicture());
                intent.putExtra("followersCount", users.getFollowersCount());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        DiscoverPeopleSampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = DiscoverPeopleSampleBinding.bind(itemView);

        }
    }
}
