package com.example.socialcook.firebase;

import android.app.Service;
import android.content.Intent;
import android.media.MediaDrm;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.example.socialcook.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.OnDisconnect;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.StorageReference;

import java.util.logging.Handler;

public class FireBase {

    private static final String TAG = "debugIT";

    public FireBase(){}

    ;
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
        void register(TextView email , TextView password, DatabaseReference myRef , User newUser, StorageReference riversRef , Uri image_Uri);
    }
    public interface ILogin {
        void login(TextView email , TextView password);
    }
}
