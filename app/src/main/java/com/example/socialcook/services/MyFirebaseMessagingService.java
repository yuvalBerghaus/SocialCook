package com.example.socialcook.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import static com.example.socialcook.afterlogin.adminFrag.NotificationApp.CHANNEL_2_ID;
import static com.example.socialcook.firebase.FireBase.refreshedToken;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.socialcook.R;
import com.example.socialcook.recievenotification.ReceiveNotificationActivity;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

//yes
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "checking remote message";
    private NotificationManagerCompat notificationManager;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
      // if(FireBase.getAuth().getCurrentUser() != null) {
        Map<String, String> data = remoteMessage.getData();
        String myCustomKey = data.get("my_custom_key");
        Log.d(TAG , "the title is "+remoteMessage.getNotification().getTitle());
            if(myCustomKey.equals("request")) {
                showNotificationRequest(remoteMessage.getNotification().getTitle() , remoteMessage.getNotification().getBody() , remoteMessage);
            }
            else if(myCustomKey.equals("accept")) {
                showNotificationAccept(remoteMessage.getNotification().getTitle() , remoteMessage.getNotification().getBody() , remoteMessage);
            }
        //}
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FireBase.usersDir.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("token").setValue(refreshedToken);
    }
    public void showNotificationAccept(String title , String body , RemoteMessage remoteMessage) {
        notificationManager = NotificationManagerCompat.from(this);
      //  Map<String, String> extraData = remoteMessage.getData();
/*
        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent actionIntent = PendingIntent
                .getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

*/
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_2_ID)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setLargeIcon(largeIcon)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true)
                        .setSmallIcon(R.drawable.ic_launcher);
/*
        Intent intent;
        if (category.equals("shoes")) {
            intent = new Intent(this, ReceiveNotificationActivity.class);

        } else {
            intent = new Intent(this, ReceiveNotificationActivity.class);

        }
        intent.putExtra("username" , sourceName);
        intent.putExtra("brandId", brandId);
        intent.putExtra("category", category);
        intent.putExtra("recipeName", recipeName);
        intent.putExtra("recipeType", recipeType);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);
 */
        int id = (int) System.currentTimeMillis();
        notificationManager.notify(id, notificationBuilder.build());
    }
    public void showNotificationRequest(String title , String body , RemoteMessage remoteMessage) {
        notificationManager = NotificationManagerCompat.from(this);
        Map<String, String> extraData = remoteMessage.getData();
        String recipeName = extraData.get("recipeName");
        String userIdSource = extraData.get("uidSource");
        Log.d(TAG , "MYFIREBASESERVICE UIDSOURCE we wanna see the remote message "+userIdSource);
        String recipeType = extraData.get("recipeType");
        String name = extraData.get("username");
        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent actionIntent = PendingIntent
                .getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_2_ID)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setLargeIcon(largeIcon)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(name+" requested to bake "+recipeName+" with you! click accept to confirm.")
                                .setBigContentTitle("Description")
                                .setSummaryText("Invitation"))
                        .setColor(Color.RED)
                     //   .addAction(R.mipmap.ic_launcher, "Reject", actionIntent)
                       // .addAction(R.mipmap.ic_launcher, "Accept", actionIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true)
                        .setSmallIcon(R.drawable.ic_launcher);

        Intent intent;
        Bundle bndl = new Bundle();
        bndl.putString("uidSource" , userIdSource);
        bndl.putString("recipeName" , recipeName);
        bndl.putString("recipeType" , recipeType);
        intent = new Intent(this, ReceiveNotificationActivity.class);
        intent.putExtras(bndl);
        intent.putExtra("uidSource" , userIdSource);
        Log.d(TAG , "uid source in the service request putting extra is "+userIdSource);
        intent.putExtra("recipeName", recipeName);
        intent.putExtra("recipeType", recipeType);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);

        int id = (int) System.currentTimeMillis();
        notificationManager.notify(id, notificationBuilder.build());
    }
}