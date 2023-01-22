package com.example.instagram.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.Models.Posts;
import com.example.instagram.R;
import com.example.instagram.databinding.ProfileUploadedPhotoSampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragmentPhotoUploadedAdapter extends RecyclerView.Adapter<ProfileFragmentPhotoUploadedAdapter.ViewHolder> {
    Context context;
    ArrayList<Posts> list;
    String tag = "profileFragmentPhotoUploadedAdapter";


    public ProfileFragmentPhotoUploadedAdapter(Context context, ArrayList<Posts> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProfileFragmentPhotoUploadedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_uploaded_photo_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileFragmentPhotoUploadedAdapter.ViewHolder holder, int position) {
        Posts post = list.get(position);
        Picasso.get().load(post.getPostImage()).into(holder.binding.postedImage);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ProfileUploadedPhotoSampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ProfileUploadedPhotoSampleBinding.bind(itemView);
        }
    }
}
