package com.example.socialcook;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.socialcook.afterlogin.activities.MainPage;
import com.example.socialcook.afterlogin.userListFrag.CustomAdapterUser;
import com.example.socialcook.classes.Recipe;
import com.example.socialcook.classes.Room;
import com.example.socialcook.classes.User;
import com.example.socialcook.firebase.FireBase;
import com.example.socialcook.roomListFrag.CustomAdapterRoom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class RoomInfo extends Fragment {
    private static ArrayList<Map<String,Integer>> data;
    private MainPage mainPage = (MainPage)getActivity();
    private static CustomAdapterIngridients adapter;
    static View.OnTouchListener myOnClickListener;
    private static final String TAG = "my time has come";
    private static RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_info, container, false);
        Bundle extras = this.getArguments();
        final TextView recipeName = view.findViewById(R.id.nameOfRecipe);
        final TextView recipeType = view.findViewById(R.id.recipeTypeInsert);
        final String roomID = extras.get("roomID").toString();
        Log.d(TAG , "the room id is "+roomID);
        final DatabaseReference myRef = FireBase.getDataBase().getReference("rooms");
        recyclerView = view.findViewById(R.id.recyclerViewRoomInfo);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myRef.child(roomID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Room room = dataSnapshot.getValue(Room.class);
                //System.out.println(dataSnapshot.child("uid1").getValue());
                System.out.println(room.getRecipe().convertRecipeAmountIteration());
                recipeName.setText(room.getRecipe().getRecipeName());
                recipeType.setText(room.getRecipe().getRecipeType());
                Map<String, Integer> mapAmount = room.getRecipe().getRecipeAmount();
                Map mapGrams = room.getRecipe().getRecipeG();
                Map mapML = room.getRecipe().getRecipeML();
                data.add(mapAmount);
                adapter = new CustomAdapterIngridients(data , mainPage);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}