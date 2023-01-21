package com.example.instagram.NotificationFragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.instagram.FriendsDetailsActivity;
import com.example.instagram.Models.Followers;
import com.example.instagram.Models.Users;
import com.example.instagram.R;
import com.example.instagram.SplashActivity;
import com.example.instagram.databinding.FriendRequestSampleBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class SuggestionForYouAdapter extends RecyclerView.Adapter<SuggestionForYouAdapter.ViewHolder> {
    Context context;
    ArrayList<Users> list;
    private  int selectedItemPosition = 0;
    String tag = "SuggestionForYouAdapter";

    public SuggestionForYouAdapter(Context context, ArrayList<Users> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_request_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = list.get(position);
        Picasso.get().load(users.getProfilePicture()).into(holder.binding.profileImage);
        holder.binding.username.setText(users.getUsername());
        holder.binding.profileName.setText(users.getName());

        int followingCount = users.getFollowingCount();

        holder.binding.buttonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, followingCount + "", Toast.LENGTH_SHORT).show();
            }
        });


        // working for follow button

       FirebaseDatabase.getInstance().getReference()
               .child("Users")
               .child(users.getUserId())
               .child("followers").child(FirebaseAuth.getInstance().getUid()).child("followedById").addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists()){
                           holder.binding.buttonFollow.setBackground(ContextCompat.getDrawable(context, R.drawable.follow_active_btn_bg));
                           holder.binding.buttonFollow.setText("Following");
                           holder.binding.buttonFollow.setTextColor(context.getResources().getColor(R.color.black));
                           holder.binding.buttonFollow.setEnabled(false);
                       }


                       /*

                       else
                       {
                           holder.binding.buttonFollow.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   Followers follow = new Followers();
                                   follow.setFollowedById(FirebaseAuth.getInstance().getUid());
                                   follow.setFollowedAt(new Date().getTime());
                                   FirebaseDatabase.getInstance().getReference().child("Users")
                                           .child(users.getUserId())
                                           .child("followers")
                                           .child(FirebaseAuth.getInstance().getUid())
                                           .setValue(follow)
                                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void unused) {
                                                   FirebaseDatabase.getInstance().getReference().child("Users")
                                                           .child(users.getUserId())
                                                           .child("followersCount")
                                                           .setValue(users.getFollowersCount() + 1 )
                                                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                               @Override
                                                               public void onSuccess(Void unused) {
                                                                   Toast.makeText(context, "You Followed " + users.getName(), Toast.LENGTH_SHORT).show();
                                                                   Log.d(tag, "You Followed" + users.getName());
                                                                   FirebaseDatabase.getInstance().getReference().child("Users")
                                                                           .child(FirebaseAuth.getInstance().getUid())
                                                                           .child("followings")
                                                                           .child(users.getUserId())
                                                                           .setValue(follow)
                                                                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                               @Override
                                                                               public void onSuccess(Void unused) {
                                                                                   FirebaseDatabase.getInstance().getReference().child("Users")
                                                                                           .child(FirebaseAuth.getInstance().getUid())
                                                                                           .child("followingCount")
                                                                                           .setValue(followingCount + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                               @Override
                                                                                               public void onSuccess(Void unused) {
                                                                                                   holder.binding.buttonFollow.setBackground(ContextCompat.getDrawable(context, R.drawable.follow_active_btn_bg));
                                                                                                   holder.binding.buttonFollow.setText("Following");
                                                                                                   holder.binding.buttonFollow.setTextColor(context.getResources().getColor(R.color.black));
                                                                                                   holder.binding.buttonFollow.setEnabled(false);
                                                                                                   Log.d(tag, "following");

                                                                                               }
                                                                                           });

                                                                               }
                                                                           });
                                                               }
                                                           });
                                               }
                                           });

                               }
                           });

                       }

                        */






                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });


       holder.binding.cut.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(context, users.getUserId(), Toast.LENGTH_SHORT).show();
           }
       });
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
               intent.putExtra("followingCount", users.getFollowingCount());
               intent.putExtra("postCount", users.getPostCount());
               context.startActivity(intent);
//               Animatoo.INSTANCE.animateFade(context);

           }
       });






    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FriendRequestSampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = FriendRequestSampleBinding.bind(itemView);
        }
    }
}
