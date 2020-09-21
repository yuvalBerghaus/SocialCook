package com.example.socialcook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import java.lang.Object;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.pm.PackageManager;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class RoomInfo extends Fragment {
    private static ArrayList<Map<String,Integer>> data;

    Recipe recipe;
    private static CustomAdapterIngridients adapter;
    TextView logInfo;
    static View.OnTouchListener myOnClickListener;
    boolean amountFull = true;
    boolean gramsFull = true;
    boolean mlFull = true;
    Button nextButton;
    private static final String TAG = "my time has come";
    private static RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_room_info, container, false);
        final MainPage mainPage = (MainPage)getActivity();
        Bundle extras = this.getArguments();
        final TextView recipeName = view.findViewById(R.id.nameOfRecipe);
        logInfo = view.findViewById(R.id.logInfo);
        final TextView uidView = view.findViewById(R.id.uidNamesView);
        final String roomID = extras.get("roomID").toString();
        Log.d(TAG , "the room id is "+roomID);
        final DatabaseReference myRef = FireBase.getDataBase().getReference("rooms"); // This var contains reference to rooms
        final DatabaseReference userNamesRef = FireBase.getDataBase().getReference("users"); // This var contains reference to users room
        final DatabaseReference recipesRef = FireBase.getDataBase().getReference("recipes"); // This var contains reference to recipes room
        final DatabaseReference logRef = FireBase.getDataBase().getReference("rooms").child(roomID).child("logs");
        recyclerView = view.findViewById(R.id.recyclerViewRoomInfo);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        Recipe recipeALL;
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        nextButton = view.findViewById(R.id.Next);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPage.loadEventPage();
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE , "Cooking "+recipeName);
                intent.putExtra(CalendarContract.Events.DESCRIPTION, "you put buk lau");
                intent.putExtra(CalendarContract.Events.ALL_DAY, "true");
                intent.putExtra(Intent.EXTRA_EMAIL, FireBase.getAuth().getCurrentUser().getEmail());
                if(intent.resolveActivity(mainPage.getPackageManager()) != null){
                    startActivity(intent);
                }
            }
        });
       // final DatabaseReference refLogs = myRef.child(roomID).child("recipe").;
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
                final Room room = dataSnapshot.getValue(Room.class);
                //System.out.println(dataSnapshot.child("uid1").getValue());
                recipeName.setText(room.getRecipe().getRecipeName());
                recipesRef.child(room.getRecipe().getRecipeName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot2) {
                        recipe = dataSnapshot2.getValue(Recipe.class); // this object now will contain all the room db quantity
                        for(DataSnapshot key:dataSnapshot.child("recipe").child("recipeAmount").getChildren()) {
                            if(recipe.getRecipeAmount().containsKey(key.getKey())) {
                                if (Integer.parseInt(recipe.getRecipeAmount().get(key.getKey()).toString()) != Integer.parseInt(key.getValue().toString())) {
                                    amountFull = false;
                                }
                            }
                        }

                        for(DataSnapshot key:dataSnapshot.child("recipe").child("recipeG").getChildren()) {
                            if(recipe.getRecipeG().containsKey(key.getKey())) {
                                if (Integer.parseInt(recipe.getRecipeG().get(key.getKey()).toString()) != Integer.parseInt(key.getValue().toString())) {
                                    gramsFull = false;
                                }
                            }
                        }

                        for(DataSnapshot key:dataSnapshot.child("recipe").child("recipeML").getChildren()) {
                            if(recipe.getRecipeML().containsKey(key.getKey())) {
                                Log.d("WUHAN" , "FUCK "+key.getKey()+key.getValue()+recipe.getRecipeML().get(key.getKey()));
                                if (Integer.parseInt(recipe.getRecipeML().get(key.getKey()).toString()) != Integer.parseInt(key.getValue().toString())) {
                                    Log.d("WHYYYYY" , "SHIT "+amountFull);
                                    mlFull = false;
                                }
                            }
                        }
                        if (amountFull && gramsFull && mlFull) {
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
                logRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String str = "";
                        for(DataSnapshot db:dataSnapshot.getChildren()) {
                            str += db.getValue().toString()+"\n";
                        }
                        Log.d("Kol hayom",str);
                        logInfo.setText(str);
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