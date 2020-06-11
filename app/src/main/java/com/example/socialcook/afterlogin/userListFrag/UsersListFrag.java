package com.example.socialcook.afterlogin.userListFrag;

import android.content.Intent;
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

import com.example.socialcook.R;
import com.example.socialcook.afterlogin.activities.MainPage;
import com.example.socialcook.classes.User;
import com.example.socialcook.beforelogin.MainActivity;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UsersListFrag extends Fragment implements FireBase.IMainPage {
    FirebaseUser user = FireBase.getAuth().getCurrentUser();
    FirebaseAuth userAuth = FireBase.getAuth();
    private static RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    MainPage mainPage;
    private static ArrayList<User> data;
    private static CustomAdapterUser adapter;
    static View.OnTouchListener myOnClickListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_list, container, false);

        if (user != null) {
            mainPage = (MainPage)getContext();
            final FirebaseDatabase database = FireBase.getDataBase();
            final DatabaseReference myRef = database.getReference().child("users");
            Bundle extras = this.getArguments();
            recyclerView = view.findViewById(R.id.recyclerViewUser);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setItemAnimator(new DefaultItemAnimator());
            data = new ArrayList<User>();

            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    User userIteration = dataSnapshot.getValue(User.class);
                    Log.d("<<< TESTING >>>", "onChildAdded: "+userIteration.getName());
                    data.add(userIteration);
                    Log.d("TESTING", "onChildAdded: data size = "+data.size());
                    adapter = new CustomAdapterUser(data , mainPage);
                    recyclerView.setAdapter(adapter);
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
            // Inflate the layout for this fragment

        }
        else {
            //Pass to the MainPage Activity in order to go to MainActivity
//            MainPage main = new MainPage();
            signOut();
        }
        return view;
    }

    @Override
    public void updateToken() {
        System.out.println("fdsfds");
    }

    @Override
    public void signOut() {
        userAuth.signOut();
        Intent i = new Intent(this.getContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}