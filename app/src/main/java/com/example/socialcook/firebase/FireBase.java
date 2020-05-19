package com.example.socialcook.firebase;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.socialcook.afterlogin.MainPage;
import com.example.socialcook.beforelogin.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBase {

    private FireBase(){};

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
                        // ...
                    }
                });
    }
    public static void register(TextView email , TextView password,final MainActivity before) {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(before, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(before, "Register Succeed.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            before.loadLoginFrag();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(before, "Register failed.",
                                    Toast.LENGTH_SHORT).show();
                            before.loadLoginFrag();
                        }

                        // ...
                    }
                });
    }

    public static FirebaseAuth getAuth(){
        return mAuth;
    }
    interface IFB{
    }
}
