package com.example.socialcook.firebase;

import android.widget.TextView;

import com.example.socialcook.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class FireBase {

    private FireBase(){};

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static String POST = "https://fcm.googleapis.com/fcm/send";
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static FirebaseAuth getAuth(){
        return mAuth;
    }
    public static FirebaseDatabase getDataBase(){
        return database;
    }
    public static FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
    public interface IMainPage {
        void updateToken();
        void signOut();
    }
    public interface IRegister {
        void register(TextView email , TextView password, DatabaseReference myRef , User newUser);
    }
    public interface ILogin {
        void login(TextView email , TextView password);
    }

}
