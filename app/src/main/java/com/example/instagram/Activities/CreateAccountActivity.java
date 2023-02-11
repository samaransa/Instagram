package com.example.instagram.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.instagram.Adapters.ViewPagerAdapter;
import com.example.instagram.databinding.ActivityCreateAccountBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class CreateAccountActivity extends AppCompatActivity {
    ActivityCreateAccountBinding binding;
    String [] titles = {"PHONE NUMBER", "EMAIL ADDRESS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // status bar gone
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (((tab, position) -> tab.setText(titles[position])))).attach();

        binding.txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}