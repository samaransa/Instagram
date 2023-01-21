package com.example.instagram.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.Models.Online;
import com.example.instagram.R;
import com.example.instagram.databinding.OnlineFriendsSampleBinding;

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
        holder.binding.profileImage.setImageResource(list.get(position).getProfileImage());
        holder.binding.profileName.setText(list.get(position).getName());

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
