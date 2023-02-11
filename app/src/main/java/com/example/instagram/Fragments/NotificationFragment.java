package com.example.instagram.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram.Models.Users;
import com.example.instagram.NotificationFragment.SuggestionForYouAdapter;
import com.example.instagram.databinding.FragmentNotificationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    FragmentNotificationBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseDatabase database;
    SuggestionForYouAdapter adapter;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding  =FragmentNotificationBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        friendSuggestionAdapter();


        return binding.getRoot();
    }

    public void friendSuggestionAdapter(){


        binding.friendsSuggestionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.friendsSuggestionRecyclerView.setNestedScrollingEnabled(false);
        adapter = new SuggestionForYouAdapter(getContext(), list);
        binding.friendsSuggestionRecyclerView.setAdapter(adapter);
//        setUserVisibleHint(true) ;



        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    if (!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())){
                        list.add(users);
                    }
                }
                adapter.notifyDataSetChanged();
//                swipeRefreshLayout(); // for the refreshing the layout manually.


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private  void  swipeRefreshLayout(){
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }






}