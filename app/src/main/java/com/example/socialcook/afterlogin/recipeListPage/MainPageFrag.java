package com.example.socialcook.afterlogin.recipeListPage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.socialcook.R;
import com.example.socialcook.beforelogin.MainActivity;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainPageFrag extends Fragment implements FireBase.IMainPage {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private static ArrayList<Recipe> data;
    private static CustomAdapter adapter;
    static View.OnTouchListener myOnClickListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);

        ///////////////////////////////////////////////////////////////////////
        if (user != null) {
            final FirebaseDatabase database = FireBase.getDataBase();
            final DatabaseReference myRef = database.getReference().child("recipes");
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setItemAnimator(new DefaultItemAnimator());
            data = new ArrayList<Recipe>();

            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    Recipe recipeIteration = dataSnapshot.getValue(Recipe.class);
                    Log.d("<<< TESTING >>>", "onChildAdded: "+recipeIteration.getRecipeName());
                    data.add(recipeIteration);
                    Log.d("TESTING", "onChildAdded: data size = "+data.size());
                    adapter = new CustomAdapter(data , (MainPage) getActivity());
                    recyclerView.setAdapter(adapter);
                    //arrayList.add(recipeIteration.getRecipeName());
                    //listView.setAdapter(adapter);
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

                // ...
            });
            MainPage currentActivity = (MainPage)getActivity();
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            final String uid = user.getUid();
            //////////////////////////////////////////////////
            TextView welcome = view.findViewById(R.id.welcome);
            Button signOut = view.findViewById(R.id.signOutButton);
            signOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signOut();
                }
            });
            welcome.setText("Welcome Back "+user.getDisplayName()+"!!!");
            Button adminPageButton = view.findViewById(R.id.accountOption);
            adminPageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainPage mainPage = (MainPage) getActivity();
                    mainPage.loadAdminPage();
                }
            });
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
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}