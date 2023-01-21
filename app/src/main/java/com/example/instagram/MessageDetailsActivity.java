package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.Toast;

import com.example.instagram.databinding.ActivityMessageDetailsBinding;
import com.squareup.picasso.Picasso;

public class MessageDetailsActivity extends AppCompatActivity {

    ActivityMessageDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageDetailsBinding.inflate(getLayoutInflater());

        String name = getIntent().getStringExtra("name");
        String profilePicture = getIntent().getStringExtra("profilePicture");

        binding.name.setText(name);
        Picasso.get().load(profilePicture).into(binding.profileImage);



        binding.edTypeMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String message = binding.edTypeMessage.getText().toString();
                if(!message.isEmpty()){
                    binding.sendBtn.setVisibility(View.VISIBLE);
                    binding.microPhone.setVisibility(View.INVISIBLE);
                    binding.openGalley.setVisibility(View.INVISIBLE);
                    binding.sticker.setVisibility(View.INVISIBLE);
                    binding.edTypeMessage.setMaxWidth(300);
                    binding.inIcon.setImageResource(R.drawable.search_three);
                }else {
                    binding.sendBtn.setVisibility(View.INVISIBLE);
                    binding.microPhone.setVisibility(View.VISIBLE);
                    binding.openGalley.setVisibility(View.VISIBLE);
                    binding.sticker.setVisibility(View.VISIBLE);
                    binding.inIcon.setImageResource(R.drawable.camera_full);
                }

            }



            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        binding.microPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MessageDetailsActivity.this, "Hello sir", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MessageDetailsActivity.this, LoginActivity.class);
//                startActivity(intent);



            }
        });

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MessageDetailsActivity.this, "Sending.....", Toast.LENGTH_SHORT).show();

            }
        });


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setContentView(binding.getRoot());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}