package com.example.socialcook.afterlogin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.socialcook.diet.*;
import com.example.socialcook.R;
import com.example.socialcook.beforelogin.MainActivity;
import com.example.socialcook.firebase.FireBase;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainPageFrag extends Fragment implements FireBase.IMainPage {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        ///////////////////////////////////////////////////////////////////////
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            //////////////////////////////////////////////////
            TextView welcome = view.findViewById(R.id.welcome);
            Button signOut = view.findViewById(R.id.signOutButton);
            signOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    MainPage main = new MainPage();
                    signOut();
                }
            });
            welcome.setText("Welcome Back " + email);
        }
        else {
            //Pass to the MainPage Activity in order to go to MainActivity
//            MainPage main = new MainPage();
            signOut();
        }
        return view;
    }


    @Override
    public void signOut() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        Intent i = new Intent(this.getContext(), MainActivity.class);
        startActivity(i);
    }
}