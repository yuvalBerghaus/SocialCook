package com.example.socialcook.beforelogin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialcook.afterlogin.recipeListPage.MainPage;
import com.example.socialcook.firebase.FireBase;
import com.example.socialcook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginFragment extends Fragment implements FireBase.ILogin {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button registerButton = (Button)view.findViewById(R.id.registerButton);
        Button loginButton = (Button)view.findViewById(R.id.loginButton);
        final TextView email = (TextView)view.findViewById(R.id.emailText);
        final TextView password = (TextView)view.findViewById(R.id.passwordText);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.loadRegister();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                login(email , password);
            }
        });
        return view;
    }

    @Override
    public void login(TextView email, TextView password) {
        final MainActivity main = (MainActivity)getActivity();
        FireBase.getAuth().signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(main, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(main, "Authentication Succeed.",
                                    Toast.LENGTH_SHORT).show();
                            //**********Intent i = new Intent(MainActivity.this , MainPage.class);
                            Intent i = new Intent(main , MainPage.class);
                            main.startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(main, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...:)
                    }
                });
    }
}