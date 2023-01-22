package com.example.instagram.Fragments;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.instagram.Adapters.PostAdapter;
import com.example.instagram.Adapters.StoryAdapter;
import com.example.instagram.CropperActivity;
import com.example.instagram.MessageActivity;
import com.example.instagram.Models.Posts;
import com.example.instagram.Models.Story;
import com.example.instagram.Models.Users;
import com.example.instagram.Models.UsersStories;
import com.example.instagram.R;
import com.example.instagram.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {
    ArrayList<Story> list = new ArrayList<>();
    ArrayList<Posts> arrayList = new ArrayList<>();
    FragmentHomeBinding binding;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    ActivityResultLauncher<String> cropImage;
    String tag = "homeFragment";
    ActivityResultLauncher<String> galleryLauncher;
    Uri source;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentHomeBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        storyWork();  // calling method
        postWork();
        intentGoForNextClass();
        fetchingDataFromFirebase();

        cropImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Intent intent = new Intent(getContext(), CropperActivity.class);

            if (result!=null){
                intent.putExtra("sendImageData", result.toString());
                startActivityForResult(intent, 100);

            }else {
                try {
                    intent.putExtra("sendImageData", result.toString());
                    startActivityForResult(intent, 100);

                }catch (Exception e){
                    Log.d(tag, "Add some resources please");
                    Log.e(tag, e.toString());


                }
            }


        });








        binding.addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePermission();
            }
        });

        return binding.getRoot();
    }


    // All work for story
    public void storyWork(){

        binding.addStoryRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.addStoryRv.setNestedScrollingEnabled(false);
        binding.horizontalSv.setHorizontalScrollBarEnabled(false);
        StoryAdapter adapter = new StoryAdapter(getContext(), list);
        binding.addStoryRv.setAdapter(adapter);
        database.getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Story story  = new Story();
                        story.setStoryBy(dataSnapshot.getKey());
                        story.setStoryAt(dataSnapshot.child("postedBy").getValue(Long.class));

                        ArrayList<UsersStories> stories = new ArrayList<>();
                        for (DataSnapshot snapshot1 : dataSnapshot.child("userStories").getChildren()){
                            UsersStories usersStories =  snapshot1.getValue(UsersStories.class);
                            stories.add(usersStories);

                        }
                        story.setStories(stories);
                        list.add(story);

                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.addYourStoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryLauncher.launch("image/*");
            }
        });


        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.profileImage.setImageURI(result);
                source = result;
                if (source!=null){
                    uploadStory();

                }else {
                    try {
                        uploadStory();
                    }catch (Exception e){
                        Log.d(tag, "Result/resource is Empty");
                        binding.progressBar.setVisibility(View.INVISIBLE);
                    }
                }




            }
        });


    }
    // All work for post image;
    public void postWork(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setNestedScrollingEnabled(false);
        PostAdapter adapter = new PostAdapter(getContext(), arrayList);
        binding.recyclerView.setAdapter(adapter);
        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Posts post = dataSnapshot.getValue(Posts.class);
                    post.setPostId(dataSnapshot.getKey());
                    arrayList.add(post);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void  intentGoForNextClass(){
        binding.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MessageActivity.class));
            }
        });
    }

    public void fetchingDataFromFirebase(){
        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Users users = snapshot.getValue(Users.class);
                    Picasso.get().load(users.getProfilePicture()).into(binding.profileImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void ImagePermission(){
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        cropImage.launch("image/*");

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100 && resultCode == 101){
            String result = data.getStringExtra("CROP");
            Uri uri = data.getData();
            if (result!=null){
                uri = Uri.parse(result);
            }

            binding.profileImage.setImageURI(uri);
        }
    }

    public  void uploadStory(){
        final StorageReference reference
                = storage.getReference()
                .child("stories")
                .child(auth.getUid())
                .child(new Date().getTime() + "");
        reference.putFile(source).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Story story = new Story();
                        story.setStoryAt(new Date().getTime());
                        database.getReference()
                                .child("stories")
                                .child(auth.getUid())
                                .child("postedBy")
                                .setValue(story.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        UsersStories stories = new UsersStories(uri.toString(), story.getStoryAt());
                                        database.getReference()
                                                .child("stories")
                                                .child(auth.getUid())
                                                .child("userStories")
                                                .push()
                                                .setValue(stories)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        binding.progressBar.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(getContext(), "Story Uploaded", Toast.LENGTH_SHORT).show();

                                                    }
                                                });

                                    }
                                });

                    }
                });

            }
        });

    }




}