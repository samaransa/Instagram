package com.example.instagram.ProfileFragments;

import static com.example.instagram.R.drawable.dialog_bg;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.Fragments.ProfileFragment;
import com.example.instagram.R;
import com.example.instagram.databinding.FragmentUpdateProfilePictureBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateProfilePictureFragment extends Fragment {
    FragmentUpdateProfilePictureBinding binding;
    FirebaseAuth auth ;
    FirebaseStorage storage;
    FirebaseDatabase database;
    ActivityResultLauncher<String> launcher;
    ProgressDialog dialog;
    Uri source;

    String tag = "updateProfilePictureFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUpdateProfilePictureBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        openGallery();

        dialogForRemoveProFilePicture();



        return binding.getRoot();
    }

    // working on fetching image from gallery for profile photo
    public void openGallery(){
        binding.newProfilePicture.setOnClickListener(new View.OnClickListener() {
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
//                binding.profileImage.setImageURI(result);
                source = result;
                if (source!=null){
                    addingProfilePicture();
                }else {
                    try {
                        addingProfilePicture();
                    }catch (Exception e){
                        Log.d(tag, "result is empty");
                        dialog.dismiss();
                    }
                }




            }
        });


    }





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

    // it reload the hole fragment
    public void reloadingFragment(){
        Fragment frg = new ProfileFragment();
        frg = getChildFragmentManager().findFragmentByTag("Your_Fragment_TAG");
        final FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();

    }


    // this method for the future use
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


    public void dialogForRemoveProFilePicture(){
        Dialog dialog = new Dialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.custom_dialog_box_for_remove_profile_picture);
        dialog.getWindow().setBackgroundDrawableResource(dialog_bg);
        binding.removeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView cancel = dialog.findViewById(R.id.cancel);
                TextView remove = dialog.findViewById(R.id.remove);
                ProgressBar progressBar = dialog.findViewById(R.id.progressBar2);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        database.getReference().child("Users").child(auth.getUid()).child("profilePicture")
                                .setValue("https://firebasestorage.googleapis.com/v0/b/instagram-64214.appspot.com/o/profile_picture%2Fiv0jiTC147f0giMDkA7k3p2x7BA3?alt=media&token=55cb7d88-4822-4c0b-bec6-51fe396e941f")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.dismiss();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }
                });
            }
        });
    }






}