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
import android.widget.Button;
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
import java.util.Hashtable;
import java.util.Map;

public class RoomInfo extends Fragment {
    private static ArrayList<Map<String,Integer>> data;
    private MainPage mainPage = (MainPage)getActivity();
    Recipe recipe;
    private static CustomAdapterIngridients adapter;
    static View.OnTouchListener myOnClickListener;
    boolean amountFull = true;
    boolean gramsFull = false;
    boolean mlFull = false;
    Button nextButton;
    private static final String TAG = "my time has come";
    private static RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_room_info, container, false);
        Bundle extras = this.getArguments();
        final TextView recipeName = view.findViewById(R.id.nameOfRecipe);
        final TextView uidView = view.findViewById(R.id.uidNamesView);
        final String roomID = extras.get("roomID").toString();
        Log.d(TAG , "the room id is "+roomID);
        final DatabaseReference myRef = FireBase.getDataBase().getReference("rooms"); // This var contains reference to rooms
        final DatabaseReference userNamesRef = FireBase.getDataBase().getReference("users"); // This var contains reference to users room
        final DatabaseReference recipesRef = FireBase.getDataBase().getReference("recipes"); // This var contains reference to recipes room
        recyclerView = view.findViewById(R.id.recyclerViewRoomInfo);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        Recipe recipeALL;
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        nextButton = view.findViewById(R.id.Next);

        myRef.child(roomID).addListenerForSingleValueEvent(new ValueEventListener() {
            /*
            In this block we are comparing database realtime info to the original
            recipe quantity once its equal we will be able to click continue page
            +
            This block also contains both names of the accounts that use this room in a TextView
            +
            contains all the UI presentation -> recipe name ,
            */
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                Log.d("Where am i?" , ""+dataSnapshot.child("recipe").child("recipeAmount"));
                final Room room = dataSnapshot.getValue(Room.class);
                //System.out.println(dataSnapshot.child("uid1").getValue());
                recipeName.setText(room.getRecipe().getRecipeName());
                recipesRef.child(room.getRecipe().getRecipeName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot2) {
                        recipe = dataSnapshot2.getValue(Recipe.class); // this object now will contain all the room db quantity
                        for(DataSnapshot key:dataSnapshot.child("recipe").child("recipeAmount").getChildren()) {
                            if(recipe.getRecipeAmount().containsKey(key.getKey())) {
                                Log.d("WUHAN" , "FUCK "+key.getKey()+key.getValue()+recipe.getRecipeAmount().get(key.getKey()));
                                if (Integer.parseInt(recipe.getRecipeAmount().get(key.getKey()).toString()) != Integer.parseInt(key.getValue().toString())) {
                                    Log.d("WHYYYYY" , "SHIT "+amountFull);
                                    amountFull = false;
                                }
                            }
                        }
                        if(recipe.getRecipeAmount().containsKey(dataSnapshot.child("recipe").child("recipeAmount").getChildren())) {
                            Log.d("LIKA" , "YES there is");
                        }
                        if (amountFull) {
                            nextButton.setClickable(true);
                            nextButton.setAlpha(1f);
                        }
                        else {
                            nextButton.setClickable(false);
                            nextButton.setAlpha(0.5f);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
                /*
                These loops all belong hashmaps of the amount , grams and ML that the purpose is to concatanate
                 into a one array that is called all
                 */
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