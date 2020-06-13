package com.example.socialcook.firebase;

import android.media.MediaDrm;
import android.util.Log;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.example.socialcook.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class FireBase extends FirebaseMessagingService {

    private static final String TAG = "debugIT";

    public FireBase(){};
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    public static String POST = "https://fcm.googleapis.com/fcm/send";
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference usersDir = database.getReference("users");
    public static FirebaseAuth getAuth(){
        return mAuth;
    }
    public static FirebaseDatabase getDataBase(){
        return database;
    }
    public static FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
    public interface IMainPage {
        void signOut();
    }
    public interface IRegister {
        void register(TextView email , TextView password, DatabaseReference myRef , User newUser);
    }
    public interface ILogin {
        void login(TextView email , TextView password);
    }
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        usersDir.child(FireBase.getAuth().getUid()).child("token").setValue(token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }
    public static void updateToken() {
        usersDir.child(FireBase.getAuth().getUid()).child("token").setValue(refreshedToken);
    }
}
