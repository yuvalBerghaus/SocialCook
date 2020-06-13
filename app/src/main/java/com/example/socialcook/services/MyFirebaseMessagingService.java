package com.example.socialcook.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import static com.example.socialcook.afterlogin.adminFrag.NotificationApp.CHANNEL_1_ID;
import static com.example.socialcook.afterlogin.adminFrag.NotificationApp.CHANNEL_2_ID;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.socialcook.R;
import com.example.socialcook.ReceiveNotificationActivity;
import com.example.socialcook.afterlogin.adminFrag.NotificationApp;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private NotificationManagerCompat notificationManager;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(FireBase.getAuth().getCurrentUser() != null) {
            notificationManager = NotificationManagerCompat.from(this);
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            Map<String, String> extraData = remoteMessage.getData();

            String brandId = extraData.get("brandId");
            String category = extraData.get("category");

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, CHANNEL_2_ID)
                            .setContentTitle(title)
                            .setContentText(body)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setOnlyAlertOnce(true)
                            .setSmallIcon(R.drawable.ic_one);

            Intent intent;
            if (category.equals("shoes")) {
                intent = new Intent(this, ReceiveNotificationActivity.class);

            } else {
                intent = new Intent(this, ReceiveNotificationActivity.class);

            }
            intent.putExtra("brandId", brandId);
            intent.putExtra("category", category);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(pendingIntent);

            int id = (int) System.currentTimeMillis();
            notificationManager.notify(id, notificationBuilder.build());
        }
        else {
            FireBase.firebaseMessaging.unsubscribeFromTopic("news");
            FireBase.firebaseMessaging.unsubscribeFromTopic(FireBase.getAuth().getUid());
        }
    }
}