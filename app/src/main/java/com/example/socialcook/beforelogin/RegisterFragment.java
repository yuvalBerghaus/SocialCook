package com.example.socialcook.beforelogin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        final MainActivity main = (MainActivity)getActivity();
        /*
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String userID = user.getUid();
         */
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users");
        final EditText nameSignUp = view.findViewById(R.id.nameReg);
        final EditText emailSignUp = view.findViewById(R.id.emailReg);
        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_country);
// Get the string array
        String[] countries = getResources().getStringArray(R.array.countries_array);
// Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(main, android.R.layout.simple_list_item_1, countries);
        textView.setAdapter(adapter);

        final TextView passwordSignUp = view.findViewById(R.id.passwordReg);
        final EditText addressSignUp = view.findViewById(R.id.addressReg);
        final EditText birthdaySignUp = view.findViewById(R.id.birthdayReg);
        final User userSignUp = new User();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    userSignUp.setAddress(addressSignUp.getText().toString());
                    userSignUp.setEmail(emailSignUp.getText().toString());
                    userSignUp.setName(nameSignUp.getText().toString());
                    userSignUp.setBirthday(birthdaySignUp.getText().toString());
                    FireBase.register(emailSignUp , passwordSignUp , main , myRef , userSignUp);
                }
                catch (Exception NullPointerException) {
                    Toast.makeText(getContext(), "you need to fill everything!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        return view;
    }
}