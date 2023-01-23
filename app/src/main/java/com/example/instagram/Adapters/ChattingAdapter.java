package com.example.instagram.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.Models.Chatting;
import com.example.instagram.R;
import com.example.instagram.databinding.ReceaveMessageSampleBinding;
import com.example.instagram.databinding.SendMessageSampleBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChattingAdapter extends RecyclerView.Adapter {
    ArrayList<Chatting> list;
    Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;
    String recId;

    public ChattingAdapter(ArrayList<Chatting> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.send_message_sample, parent, false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receave_message_sample, parent, false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }else {
            return RECEIVER_VIEW_TYPE;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chatting chatting= list.get(position);

        if (holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder)holder).binding.senderMessage.setText(chatting.getMessage());
        }else {
            ((ReceiverViewHolder)holder).binding.receivedMessage.setText(chatting.getMessage());

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ReceaveMessageSampleBinding binding;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ReceaveMessageSampleBinding.bind(itemView);

        }
    }

    public class SenderViewHolder extends  RecyclerView.ViewHolder {
        SendMessageSampleBinding binding;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SendMessageSampleBinding.bind(itemView);
        }
    }
}
