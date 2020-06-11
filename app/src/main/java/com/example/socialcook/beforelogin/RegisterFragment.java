package com.example.socialcook.beforelogin;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.socialcook.classes.User;
import com.example.socialcook.firebase.FireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFragment extends Fragment implements FireBase.IRegister {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity main = (MainActivity)getActivity();
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Button registerButton = view.findViewById(R.id.signUpButton);
        /*
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String userID = user.getUid();
         */
        final FirebaseDatabase database = FireBase.getDataBase();
        final DatabaseReference myRef = database.getReference("users");
        final EditText nameSignUp = view.findViewById(R.id.nameReg);
        final EditText emailSignUp = view.findViewById(R.id.emailReg);
        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_country);
// Get the string array
        String[] countries = getResources().getStringArray(R.array.countries_array);
// Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, countries);
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
                    register(emailSignUp , passwordSignUp , myRef, userSignUp);
                }
                catch (Exception NullPointerException) {
                    Toast.makeText(getContext(), "you need to fill everything!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        return view;
    }

    @Override
    public void register(TextView email , TextView password , final DatabaseReference myRef, final User userSignUp) {
        final MainActivity main = (MainActivity)getActivity();
        FireBase.getAuth().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(main, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getContext(), "Register Succeed.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = FireBase.getAuth().getCurrentUser();
                            user.updateProfile(new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userSignUp.name).build());
                            userSignUp.setUID(user.getUid());
                            myRef.child(user.getUid()).setValue(userSignUp);
                            main.loadLoginFrag();
                        }
                         else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(main, "Register failed.",
                                    Toast.LENGTH_SHORT).show();
                            main.loadLoginFrag();
                        }

                        // ...
                    }
                });
    }
}