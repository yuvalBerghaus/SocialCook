package com.example.socialcook.firebase;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.socialcook.afterlogin.recipeListPage.MainPage;
import com.example.socialcook.beforelogin.MainActivity;
import com.example.socialcook.beforelogin.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBase {

    private FireBase(){};

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static FirebaseAuth getAuth(){
        return mAuth;
    }
    public static FirebaseDatabase getDataBase(){
        return database;
    }
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
