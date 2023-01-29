package com.example.instagram.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.instagram.MessageDetailsActivity;
import com.example.instagram.Models.AllConstants;
import com.example.instagram.Models.Users;
import com.example.instagram.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FirebaseNotificationService extends FirebaseMessagingService{
    private Users users = new Users();

    // this is made by me so be care full.
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size()>0){
            Map<String, String> map = remoteMessage.getData();
            String title = map.get("title");
            String message = map.get("message");
            String hisId = map.get("hisId");
            String hisImage = map.get("hisImage");
            String chatId = map.get("chatId");

            if (Build.VERSION.SDK_INT>Build.VERSION_CODES.O){
                createOreoNotification(title, message, hisId, hisImage, chatId);
            }else {
                createNormalNotification(title, message, hisId, hisImage, chatId);
            }

        }
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        updateToken(token);
        super.onNewToken(token);
    }

    private void updateToken(String token){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(users.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        databaseReference.updateChildren(map);

    }

    private void createNormalNotification(String title, String message, String hisId, String hisImage, String chatId){
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, AllConstants.CHANNEL_ID);
        builder.setContentTitle(title).setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.icon)
                .setAutoCancel(true)
                .setColor(ResourcesCompat.getColor(getResources(), R.color.purple_200, null))
                .setSound(uri);

        Intent intent = new Intent(this, MessageDetailsActivity.class);
        intent.putExtra("hisId", hisId);
        intent.putExtra("chatId", chatId);
        intent.putExtra("hisImage", hisImage);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(new Random().nextInt(85-65), builder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createOreoNotification(String title, String message, String hisId, String hisImage, String chatId){
        NotificationChannel channel = new NotificationChannel(AllConstants.CHANNEL_ID, "Message", NotificationManager.IMPORTANCE_HIGH);
        channel.setShowBadge(true);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setDescription("Message Description");
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        Intent intent = new Intent(this, MessageDetailsActivity.class);
        intent.putExtra("hisId", hisId);
        intent.putExtra("chatId", chatId);
        intent.putExtra("hisImage", hisImage);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new Notification.Builder(this, AllConstants.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ResourcesCompat.getColor(getResources(), R.color.purple_200, null))
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        manager.notify(new Random().nextInt(85-65), notification);





    }


}
