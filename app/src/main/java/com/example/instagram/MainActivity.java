package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.instagram.Fragments.HomeFragment;
import com.example.instagram.Fragments.NotificationFragment;
import com.example.instagram.Fragments.ProfileFragment;
import com.example.instagram.Fragments.ReelsFragment;
import com.example.instagram.Fragments.SearchFragment;
import com.example.instagram.databinding.ActivityMainBinding;
import com.iammert.library.readablebottombar.ReadableBottomBar;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.replace(R.id.frameLayout, new HomeFragment());

        transaction1.commit();


        binding.bottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i) {
                    case 0:
                        transaction.replace(R.id.frameLayout, new HomeFragment());


                        break;
                    case 1:
                        transaction.replace(R.id.frameLayout, new SearchFragment());
                        break;

                    case 2:
                        transaction.replace(R.id.frameLayout, new ReelsFragment());
                        break;

                    case 3:
                        transaction.replace(R.id.frameLayout, new NotificationFragment());
                        break;

                    case 4:
                        transaction.replace(R.id.frameLayout, new ProfileFragment());

                        break;


                }
//                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }


}