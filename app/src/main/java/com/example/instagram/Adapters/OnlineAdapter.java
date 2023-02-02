package com.example.instagram.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.Models.Online;
import com.example.instagram.Models.Users;
import com.example.instagram.R;
import com.example.instagram.databinding.OnlineFriendsSampleBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OnlineAdapter extends RecyclerView.Adapter<OnlineAdapter.ViewHolder> {

    Context context;
    ArrayList<Online> list;

    public OnlineAdapter(Context context, ArrayList<Online> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OnlineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.online_friends_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineAdapter.ViewHolder holder, int position) {
        Online online = list.get(position);
        Picasso.get().load(online.getProfilePicture()).into(holder.binding.profileImage);
        holder.binding.profileName.setText(online.getUsername());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        OnlineFriendsSampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = OnlineFriendsSampleBinding.bind(itemView);
        }
    }
}
