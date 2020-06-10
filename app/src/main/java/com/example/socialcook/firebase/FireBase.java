package com.example.socialcook.firebase;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.socialcook.afterlogin.recipeListPage.MainPage;
import com.example.socialcook.beforelogin.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class FireBase {

    private FireBase(){};

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static void login(TextView email , TextView password , final MainActivity before) {
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(before, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(before, "Authentication Succeed.",
                                    Toast.LENGTH_SHORT).show();
                            //**********Intent i = new Intent(MainActivity.this , MainPage.class);
                            Intent i = new Intent(before , MainPage.class);
                            before.startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(before, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...:)
                    }
                });
    }

    public static FirebaseAuth getAuth(){
        return mAuth;
    }
    public static FirebaseDatabase getDataBase(){
        return database;
    }
    interface IFB{
        void login(TextView email , TextView password);
        void register(TextView email , TextView password);
    }
    public interface IMainPage {
        FirebaseAuth mAuth = FireBase.mAuth;
        void signOut();
    }
    public interface IRegister {
        void register(TextView email , TextView password);
    }


}
