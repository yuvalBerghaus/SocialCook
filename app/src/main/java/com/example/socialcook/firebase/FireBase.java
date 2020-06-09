package com.example.socialcook.firebase;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.socialcook.SendNotificationPack.Token;
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
import com.google.firebase.iid.FirebaseInstanceId;

public class FireBase {

    private FireBase(){};
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
    public static void register(TextView email , TextView password, final MainActivity before , final DatabaseReference myRef , final User userSignUp) {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(before, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(before, "Register Succeed.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            userSignUp.setUID(user.getUid());
                            myRef.child(userSignUp.getUID()).setValue(userSignUp);
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
    public static void UpdateToken(){
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("token").setValue(token.getToken());
    }
    public static FirebaseAuth getAuth(){
        return mAuth;
    }
    interface IFB{
        void login(TextView email , TextView password);
        void register(TextView email , TextView password);
    }
    public interface IMainPage {
        void signOut();
    }



}
