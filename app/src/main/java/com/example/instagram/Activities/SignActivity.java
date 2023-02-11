package com.example.instagram.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.instagram.databinding.ActivitySignBinding;

public class SignActivity extends AppCompatActivity {

    ActivitySignBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding.signUpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

        binding.switchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        Intent intent = getIntent();
    }
}