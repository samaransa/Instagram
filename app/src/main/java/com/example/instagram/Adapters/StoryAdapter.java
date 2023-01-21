package com.example.instagram.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.Models.Stories;
import com.example.instagram.R;
import com.example.instagram.databinding.StorySampleBinding;

import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    Context context;
    ArrayList<Stories> list;

    public StoryAdapter(Context context, ArrayList<Stories> arrayList) {
        this.context = context;
        this.list = arrayList;
    }

    @NonNull
    @Override
    public StoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapter.ViewHolder holder, int position) {
        holder.binding.profileImage.setImageResource(list.get(position).getProfileImage());
        holder.binding.profileName.setText(list.get(position).getProfileName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        StorySampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = StorySampleBinding.bind(itemView);

        }
    }
}
