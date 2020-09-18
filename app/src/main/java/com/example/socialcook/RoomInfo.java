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
import java.util.HashMap;
import java.util.Map;

public class RoomInfo extends Fragment {
    private static ArrayList<Map<String,Integer>> data;
    private MainPage mainPage = (MainPage)getActivity();
    Recipe recipe;
    private static CustomAdapterIngridients adapter;
    static View.OnTouchListener myOnClickListener;
    boolean full = false;
    private static final String TAG = "my time has come";
    private static RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_room_info, container, false);
        Bundle extras = this.getArguments();
        final TextView recipeName = view.findViewById(R.id.nameOfRecipe);
        //final TextView recipeType = view.findViewById(R.id.recipeTypeInsert);
        final TextView uidView = view.findViewById(R.id.uidNamesView);
        final String roomID = extras.get("roomID").toString();
        Log.d(TAG , "the room id is "+roomID);
        final DatabaseReference myRef = FireBase.getDataBase().getReference("rooms");
        final DatabaseReference userNamesRef = FireBase.getDataBase().getReference("users");
        final DatabaseReference recipesRef = FireBase.getDataBase().getReference("recipes");
        recyclerView = view.findViewById(R.id.recyclerViewRoomInfo);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        Recipe recipeALL;
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myRef.child(roomID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Room room = dataSnapshot.getValue(Room.class);
                //System.out.println(dataSnapshot.child("uid1").getValue());
                recipeName.setText(room.getRecipe().getRecipeName());
                recipesRef.child(room.getRecipe().getRecipeName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        recipe = dataSnapshot.getValue(Recipe.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //recipeType.setText(room.getRecipe().getRecipeType());
                userNamesRef.child(room.getUid1()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String nameUid1 = dataSnapshot.getValue(String.class);
                        userNamesRef.child(room.getUid2()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String nameUid2 = dataSnapshot.getValue(String.class);
                                String together = nameUid1+" and "+nameUid2;
                                uidView.setText(together);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Map<String, Integer> mapAmount = room.getRecipe().getRecipeAmount();
                Map<String , Integer>mapGrams = room.getRecipe().getRecipeG();
                Map<String , Integer> mapML = room.getRecipe().getRecipeML();
                Map<String , Integer>all = new HashMap<>();
                for(String key:mapAmount.keySet()) {
                    Log.d(TAG , "the key of "+key+" issss "+mapAmount.get(key));
                    all.put(key ,mapAmount.get(key));
                }
                for(String key:mapGrams.keySet()) {
                    Log.d(TAG , "the key of "+key+" issss "+mapGrams.get(key));
                    all.put(key , mapGrams.get(key));
                }
                for(String key:mapML.keySet()) {
                    Log.d(TAG , "the key of "+key+" issss "+mapML.get(key));
                    all.put(key , mapML.get(key));
                }
                System.out.println("------------------------------------------------------------------------\n-------------------------");
                for(String key:all.keySet()) {
                    Log.d(TAG , "the key of "+key+" issss "+all.get(key));
                }
                Log.d("dsa" , room.getRecipe().getRecipeName());
                adapter = new CustomAdapterIngridients(all, mainPage , roomID , room.getRecipe().getRecipeName());
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