package com.example.socialcook.afterlogin.editProfilePage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.socialcook.R;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class EditProfilePage extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        final FirebaseDatabase database = FireBase.getDataBase();
        Log.d("getuid!!", ""+user.getUid());
        final DatabaseReference userRef = database.getReference().child("users").child(user.getUid());
        final EditText nameInput = view.findViewById(R.id.nameInput);
        final EditText addressInput = view.findViewById(R.id.addressInput);
        final EditText birthdayInput = view.findViewById(R.id.birthdayInput);
        Button nameButton = view.findViewById(R.id.nameSave);
        Button addressButton = view.findViewById(R.id.addressSave);
        Button birthdayButton = view.findViewById(R.id.birthdaySave);
        Button countryButton = view.findViewById(R.id.countrySave);
        final Spinner editCountrySpinner = view.findViewById(R.id.editCountrySpinner);

        Locale[] locales = Locale.getAvailableLocales();
        final ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }

        Collections.sort(countries);
        for (String country : countries) {
            System.out.println(country);
        }

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, countries);

        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the your spinner
        editCountrySpinner.setAdapter(countryAdapter);

        userRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String name = dataSnapshot.getValue(String.class);
                nameInput.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userRef.child("address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String address = dataSnapshot.getValue(String.class);
                addressInput.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userRef.child("birthday").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String birthday = dataSnapshot.getValue(String.class);
                birthdayInput.setText(birthday);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userRef.child("country").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {//NO COUNTRY?????
                final String country = dataSnapshot.getValue(String.class);
                int spinnerPosition = countries.indexOf(country);
                editCountrySpinner.setSelection(spinnerPosition);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.child("name").setValue(nameInput.getText().toString());
            }
        });

        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.child("address").setValue(addressInput.getText().toString());
            }
        });

        birthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.child("birthday").setValue(birthdayInput.getText().toString());
            }
        });

        countryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.child("country").setValue(editCountrySpinner.getSelectedItem().toString());
            }
        });

        return view;
    }

}
