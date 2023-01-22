package com.example.instagram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.instagram.Models.Posts;
import com.example.instagram.databinding.ActivityCropperBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class CropperActivity extends AppCompatActivity {
    String sourceUri, destinationUri;
    Uri uri;
    ActivityCropperBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String tag = "cropperActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCropperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        Intent intent = getIntent();
        if (intent.getExtras()!=null){
            sourceUri = intent.getStringExtra("sendImageData");
            uri = Uri.parse(sourceUri);
        }
        destinationUri = new StringBuilder(UUID.randomUUID().toString()).toString();
        UCrop.Options options = new UCrop.Options();
        UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationUri)))
                .withOptions(options)
                .withMaxResultSize(2000, 2000)
                .start(CropperActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&  requestCode == UCrop.REQUEST_CROP){
            final  Uri resultUri = UCrop.getOutput(data);
            Intent intent = new Intent();
            intent.putExtra("CROP", resultUri+ "");
            setResult(101, intent);

            final StorageReference reference = storage.getReference().child("posts")
                    .child(FirebaseAuth.getInstance().getUid())
                    .child(new Date().getTime() + "");

            if (resultUri!=null){
                reference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Posts post = new Posts();
                                post.setPostImage(uri.toString());
                                post.setPostedBy(FirebaseAuth.getInstance().getUid());
                                post.setPostAt(new Date().getTime());

                                database.getReference().child("posts").push().setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(CropperActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                        Log.d(tag, "Uploaded Successfully");


                                    /*
                                        Users users = new Users();
                                        database.getReference().child("Users")
                                                .child(auth.getUid())
                                                .child("postCount")
                                                .setValue(users.getPostCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                    }
                                                });
                                                */
                                     


                                    }
                                });


                            }
                        });



                    }
                });
            }
            finish();
        }
        else {
            finish();
            // i can also use here onBackPressed method.
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}