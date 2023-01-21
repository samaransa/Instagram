package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.instagram.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    boolean passwordVisible;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        passwordToggle(); // calling passwordToggleMethod;


        binding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
//        getSupportActionBar().hide();

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }


    // login method

    public void login(){
        String email = binding.edEmailAddress.getText().toString();
        String password = binding.edPassword.getText().toString();
        if (TextUtils.isEmpty(email)){
            binding.edEmailAddress.setError("Please enter email");
            return;
        }
        if (TextUtils.isEmpty(password)){
            binding.edPassword.setError("Please enter password");
            return;
        }
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            binding.progressBar.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                    }else {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                            Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
//                            binding.edPassword.setError("Invalid password");
                        }else if (task.getException() instanceof FirebaseAuthInvalidUserException){
                            Toast.makeText(LoginActivity.this, "Email not exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });

        }

    }

    @SuppressLint("ClickableViewAccessibility")
    public void passwordToggle() {
        binding.edPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if (event.getAction()== MotionEvent.ACTION_UP){
                    if(event.getRawX()>=binding.edPassword.getRight()-binding.edPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = binding.edPassword.getSelectionEnd();
                        if (passwordVisible){
                            // set drawable image here.
                            binding.edPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0);
                            // for hide password
                            binding.edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible =false;
                        }else {
                            binding.edPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_on, 0);
                            // for hide password
                            binding.edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible =true;
                        }

                        binding.edPassword.setSelection(selection);
                        return true;
                    }
                }

                return false;
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (user!=null){
            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
            startActivity(intent);
        }
    }
}