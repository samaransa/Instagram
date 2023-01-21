package com.example.instagram.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagram.MainActivity;
import com.example.instagram.Models.Users;
import com.example.instagram.R;

import com.example.instagram.databinding.FragmentEmailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.file.attribute.UserDefinedFileAttributeView;

public class EmailFragment extends Fragment {

    FragmentEmailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;




    public EmailFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEmailBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();






        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              createAccount();  // calling create account method
            }
        });

        checkBox();  // for by default
        binding.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox();   // calling check method
            }
        });

        return binding.getRoot();
    }







    // Method for create account;
    public void createAccount(){
        String email = binding.edEmail.getText().toString();
        String password = binding.edPassword.getText().toString();
        if (TextUtils.isEmpty(email)){
            binding.edEmail.setError("Please enter email");
            return;
        }
        if (TextUtils.isEmpty(password)){
            binding.edPassword.setError("Please enter password");
            return;
        }

        if (!email.contains("@gmail.com")){
            showError(binding.edEmail, "Email is invalid");
            return;
        }
        if (password.length()<6){
            showError(binding.edPassword, "Password must be 6 character");
        }

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && password.length()>6 && email.contains("@gmail.com")) {
            binding.progressBar.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.getException() instanceof  FirebaseAuthUserCollisionException){
                                Toast.makeText(getContext(), "This email is already exist", Toast.LENGTH_SHORT).show();
                                binding.progressBar.setVisibility(View.INVISIBLE);
                                binding.edPassword.setText("");
                                return;
                            }
                            if (task.isComplete()) {
                                binding.progressBar.setVisibility(View.INVISIBLE);
                                Users users = new Users(email, password);
                                String id = task.getResult().getUser().getUid();
                                database.getReference().child("Users").child(id).setValue(users);
                                Toast.makeText(getContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
//                                binding.edEmail.setText("");
//                                binding.edPassword.setText("");

                            } else {
                                binding.progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    // method for checkbox
    public void checkBox(){
        binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    // for show password
                    binding.edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }else {
                    // for hiding password
                    binding.edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private void showError(EditText editText, String s){
        editText.setError(s);
        editText.requestFocus();
    }




}