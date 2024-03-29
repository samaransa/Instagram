package com.example.instagram.ProfileFragments;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram.Activities.LoginActivity;
import com.example.instagram.databinding.FragmentBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    FragmentBottomSheetBinding binding;

    FirebaseAuth auth;
    FirebaseDatabase database;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        binding.logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                deleteToken();
                startActivity(intent);
                auth.signOut();
                getActivity().finish();
            }
        });

        return binding.getRoot();
    }
    private void deleteToken(){
        database.getReference().child("Users").child(auth.getUid()).child("token").setValue("");
    }


}