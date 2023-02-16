package com.example.instagram.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.Models.Story;
import com.example.instagram.Models.Users;
import com.example.instagram.Models.UsersStories;
import com.example.instagram.R;
import com.example.instagram.databinding.StorySampleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    Context context;
    ArrayList<Story> list;

    public StoryAdapter(Context context, ArrayList<Story> arrayList) {
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
        Story story = list.get(position);
        if (story.getStories().size()>0){
            UsersStories lastStories = story.getStories().get(story.getStories().size()-1);
            Picasso.get().load(lastStories.getImage()).into(holder.binding.profileImage);
//            holder.binding.statusCircle.setPortionsCount(story.getStories().size());

            FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(story.getStoryBy()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Users users = snapshot.getValue(Users.class);
                            Picasso.get().load(users.getProfilePicture()).into(holder.binding.profileImage);
                            holder.binding.profileName.setText(users.getUsername());
                            holder.binding.profileImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ArrayList<MyStory> myStories = new ArrayList<>();
                                    for (UsersStories stories : story.getStories()){
                                        myStories.add(new MyStory(
                                                stories.getImage()
                                        ));
                                    }

                                    new StoryView.Builder(((AppCompatActivity) context).getSupportFragmentManager())
                                            .setStoriesList(myStories) // Required
                                            .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                                            .setTitleText(users.getName()) // Default is Hidden
                                            .setSubtitleText("") // Default is Hidden
                                            .setTitleLogoUrl(users.getProfilePicture()) // Default is Hidden
                                            .setStoryClickListeners(new StoryClickListeners() {
                                                @Override
                                                public void onDescriptionClickListener(int position) {
                                                    //your action
                                                }

                                                @Override
                                                public void onTitleIconClickListener(int position) {
                                                    //your action
                                                }
                                            }) // Optional Listeners
                                            .build() // Must be called before calling show method
                                            .show();


                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

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
