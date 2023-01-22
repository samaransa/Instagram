package com.example.instagram.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.instagram.Adapters.DiscoverAdapter;
import com.example.instagram.Adapters.ViewPagerAdapterForProfile;
import com.example.instagram.EditProfileActivity;
import com.example.instagram.Models.Posts;
import com.example.instagram.Models.Users;
import com.example.instagram.ProfileFragments.AddBottomSheetFragment;
import com.example.instagram.ProfileFragments.BottomSheetFragment;
import com.example.instagram.R;
import com.example.instagram.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    int[] images = {R.drawable.gritwo, R.drawable.con_main};
    FirebaseAuth auth;
    FirebaseDatabase database;
    ActivityResultLauncher<String> launcher;
    FirebaseStorage storage;
    ProgressDialog dialog;
    String tag = "ProfileFragment";
    Uri source;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");

        discoverAdapter();
        addViewPagerAdapter();
        bottomSheet();
        add();
        openGallery();
        gettingUserDataFromDatabase();
        forGettingFollowingCount();
        forGettingPostCount();


        binding.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               startActivity(new Intent(getContext(), EditProfileActivity.class));
                Animatoo.INSTANCE.animateSlideUp(getContext());
            }
        });

        return binding.getRoot();
    }

    // setting discover adapter ;
    public void discoverAdapter() {

        binding.discoverPeopleRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.discoverPeopleRv.setNestedScrollingEnabled(false);
//        binding.horizontalSv.setHorizontalScrollBarEnabled(false);

        DiscoverAdapter adapter = new DiscoverAdapter(getContext() , list);
        binding.discoverPeopleRv.setAdapter(adapter);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    if(!dataSnapshot.getKey().equals(auth.getUid())){
                        list.add(users);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }  //

    public void addViewPagerAdapter() {
        binding.viewPager.setAdapter(new ViewPagerAdapterForProfile((FragmentActivity) getContext()));
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (((tab, position) -> tab.setIcon(images[position])))).attach();
    }   //

    public void bottomSheet() {
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFragment sheetFragment = new BottomSheetFragment();
                sheetFragment.show(getChildFragmentManager(), sheetFragment.getTag());
            }
        });
    }  //


    public void add() {
        binding.addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBottomSheetFragment fragment = new AddBottomSheetFragment();
                fragment.show(getChildFragmentManager(), fragment.getTag());
            }
        });
    } //

    public void gettingUserDataFromDatabase() {
        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Users users = snapshot.getValue(Users.class);
                    Picasso.get().load(users.getProfilePicture()).placeholder(R.drawable.usee).into(binding.profileImage);
                    binding.userName.setText(users.getUsername());
                    binding.name.setText(users.getName());
                    binding.followerCount.setText(users.getFollowersCount()+ "");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }  //

    public void openGallery() {
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                launcher.launch("image/*");
            }
        });


        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        binding.profileImage.setImageURI(result);
                        source = result;
                        if (source!=null){
                            addingProfilePicture();

                        }else {
                            try {
                                addingProfilePicture();
                            }catch (Exception e){
                                Log.d(tag, "Result/resource is Empty");
                                dialog.dismiss();
                            }
                        }





                    }
                });


    } //

    public void refreshProfileFragment(){
       getChildFragmentManager()
                .beginTransaction()
                .detach(ProfileFragment.this)
                .attach(ProfileFragment.this)
                .commit();
        Toast.makeText(getContext(), "Reloaded", Toast.LENGTH_SHORT).show();
    } //


    public void addingProfilePicture(){
        final StorageReference reference = storage.getReference()
                .child("profile_picture").child(FirebaseAuth.getInstance().getUid());
        reference.putFile(source).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        database.getReference().child("Users").child(auth.getUid()).child("profilePicture")
                                .setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.dismiss();
                                        Toast.makeText(getContext(),
                                                "Profile Photo Updated", Toast.LENGTH_SHORT).show();


                                    }
                                });

                    }
                });

            }
        });

    }
    // for getting followIngCount;
    public void forGettingFollowingCount(){
        database.getReference().child("Users").child(auth.getUid())
                .child("followings").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            binding.followingCount.setText(snapshot.getChildrenCount() + "");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    // for getting postsCount;
    public void forGettingPostCount(){

        database.getReference().child("usersPostedImages").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.postCount.setText(snapshot.getChildrenCount()+"");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}