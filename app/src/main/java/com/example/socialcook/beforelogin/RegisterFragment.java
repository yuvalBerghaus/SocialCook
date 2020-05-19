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

public class RegisterFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Button registerButton = (Button)view.findViewById(R.id.signUpButton);
        final TextView emailSignUp = (TextView)view.findViewById(R.id.emailReg);
        final TextView passwordSignUp = (TextView)view.findViewById(R.id.passwordReg);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                FireBase.register(emailSignUp , passwordSignUp , mainActivity);
            }
        });
        return view;
    }
}