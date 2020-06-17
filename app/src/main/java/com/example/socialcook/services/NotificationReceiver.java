package com.example.socialcook.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = "Broadcast Works";
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }
}
