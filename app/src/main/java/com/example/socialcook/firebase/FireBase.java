package com.example.socialcook.firebase;

import android.media.MediaDrm;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.example.socialcook.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.OnDisconnect;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.logging.Handler;

public class FireBase extends FirebaseMessagingService {

    private static final String TAG = "debugIT";

    public FireBase(){};
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    public static String POST = "https://fcm.googleapis.com/fcm/send";
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference usersDir = database.getReference("users");
    public static DatabaseReference recipeDir = database.getReference("recipes");
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
    public static void updateToken() {
        usersDir.child(FireBase.getAuth().getUid()).child("token").setValue(refreshedToken);
    }
}
