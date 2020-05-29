package com.example.socialcook.beforelogin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.socialcook.R;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class RegisterFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Button registerButton = view.findViewById(R.id.signUpButton);
        /*
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String userID = user.getUid();
         */
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users");
        final TextView nameSignUp = view.findViewById(R.id.nameReg);
        final TextView emailSignUp = view.findViewById(R.id.emailReg);
        final TextView passwordSignUp = view.findViewById(R.id.passwordReg);
        final TextView addressSignUp = view.findViewById(R.id.addressReg);
        final TextView birthdaySignUp = view.findViewById(R.id.birthdayReg);
        final User userSignUp = new User();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignUp.setAddress(addressSignUp.getText().toString());
                userSignUp.setEmail(emailSignUp.getText().toString());
                userSignUp.setName(nameSignUp.getText().toString());
                myRef.child(userSignUp.getName()).setValue(userSignUp);
                FireBase.register(emailSignUp , passwordSignUp , (MainActivity) getActivity());
            }
        });
        return view;
    }
}