package com.example.socialcook;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.socialcook.afterlogin.activities.MainPage;
import com.example.socialcook.classes.Recipe;
import com.example.socialcook.classes.Room;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoomInfo extends Fragment {
    private static ArrayList<Map<String,Integer>> data;
    Recipe recipe;
    private static CustomAdapterIngridients adapter;
    private static CustomAdapterPersonal adapter2;
    TextView logInfo;
    static View.OnTouchListener myOnClickListener;
    boolean amountFull = true;
    boolean gramsFull = true;
    boolean mlFull = true;
    Button nextButton;
    private static final String TAG = "my time has come";
    private static RecyclerView recyclerView;
    private static RecyclerView recyclerView2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_room_info, container, false);
        final MainPage mainPage = (MainPage)getActivity();
        Bundle extras = this.getArguments();
        final TextView recipeName = view.findViewById(R.id.nameOfRecipe);
        final Button deleteRoomButton = view.findViewById(R.id.deleteRoomButton);
        logInfo = view.findViewById(R.id.logInfo);
        final TextView uidView = view.findViewById(R.id.uidNamesView);
        final String roomID = extras.get("roomID").toString();
        Log.d(TAG , "the room id is "+roomID);
        final DatabaseReference myRef = FireBase.getDataBase().getReference("rooms"); // This var contains reference to rooms
        final DatabaseReference userNamesRef = FireBase.getDataBase().getReference("users"); // This var contains reference to users room
        final DatabaseReference recipesRef = FireBase.getDataBase().getReference("recipes"); // This var contains reference to recipes room
        final DatabaseReference logRef = FireBase.getDataBase().getReference("rooms").child(roomID).child("logs");
        recyclerView = view.findViewById(R.id.recyclerViewRoomInfo);
        recyclerView2 = view.findViewById(R.id.recyclerPersonal);
        recyclerView.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(view.getContext());
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Recipe recipeALL;
        nextButton = view.findViewById(R.id.Next);
       //// final DatabaseReference refLogs = myRef.child(roomID).child("recipe").
        ValueEventListener valueChangeRoomID;
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
                Log.d("CHECK" , room.getRecipeUid1().convertRecipeAmountIteration());
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
                        ValueEventListener valueChangeButton = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Recipe recipeShared = dataSnapshot.getValue(Recipe.class);
                                try {

                                    Log.d("recipeShared ", ""+recipe.convertRecipeAmountIteration().matches(recipeShared.convertRecipeAmountIteration()));
                                    if(recipe.convertRecipeAmountIteration().matches(recipeShared.convertRecipeAmountIteration()) && recipe.convertRecipeGIteration().matches(recipeShared.convertRecipeGIteration()) && recipe.convertRecipeMLIteration().matches(recipeShared.convertRecipeMLIteration()) ) {
                                        nextButton.setClickable(true);
                                        nextButton.setEnabled(true);
                                        nextButton.setAlpha(1f);
                                    }
                                    else {
                                        nextButton.setClickable(false);
                                        nextButton.setEnabled(false);
                                        nextButton.setAlpha(0.5f);
                                    }
                                }
                                catch(NullPointerException d) {
                                    return;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        myRef.child(roomID).child("recipe").addValueEventListener(valueChangeButton);
                        if (amountFull && gramsFull && mlFull) {
                            nextButton.setClickable(true);
                            nextButton.setEnabled(true);
                            nextButton.setAlpha(1f);
                        }
                        else {
                            nextButton.setClickable(false);
                            nextButton.setEnabled(false);
                            nextButton.setAlpha(0.5f);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                ValueEventListener valueChangeLog = new ValueEventListener() {
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
                };
                logRef.addValueEventListener(valueChangeLog);
                /*
                This function block collects the user's email in order to put it in the event creator
                 */
                userNamesRef.child(room.getUid1()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot1) {
                        final String nameUid1 = dataSnapshot1.getValue(String.class);
                        userNamesRef.child(room.getUid2()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot2) {
                                final String nameUid2 = dataSnapshot2.getValue(String.class);
                                String together = nameUid1+" and "+nameUid2;
                                uidView.setText(together);
                                dataSnapshot1.getRef().getParent().addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot12) {
                                        dataSnapshot2.getRef().getParent().addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot45) {
                                                final String email1 = dataSnapshot45.child("email").getValue().toString();
                                                final String email2 = dataSnapshot12.child("email").getValue().toString();
                                                /*
                                                Clicking the next button in order to create an event! :)
                                                 */
                                                nextButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        myRef.child(roomID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot5) {
                                                                Recipe recipeUid1 = dataSnapshot5.child("recipeUid1").getValue(Recipe.class);
                                                                Recipe recipeUid2 = dataSnapshot5.child("recipeUid2").getValue(Recipe.class);

                                                                Map<String , Integer>user1 = new HashMap<>();
                                                                Map<String , Integer>user2 = new HashMap<>();
                                                                Intent intent = new Intent(Intent.ACTION_INSERT);
                                                                intent.setData(CalendarContract.Events.CONTENT_URI);
                                                                intent.putExtra(CalendarContract.Events.TITLE , "Lets make "+recipeName.getText().toString()+"!!!");
                                                                intent.putExtra(CalendarContract.Events.DESCRIPTION, nameUid1+" is going to bring "+recipeUid1.convertRecipeAmountIteration()+recipeUid1.convertRecipeGIteration()+recipeUid1.convertRecipeMLIteration()+"\nAnd "+nameUid2+" is going to bring "+recipeUid2.convertRecipeAmountIteration()+recipeUid2.convertRecipeGIteration()+recipeUid2.convertRecipeMLIteration());
                                                                intent.putExtra(CalendarContract.Events.ALL_DAY, false);
                                                                intent.putExtra(Intent.EXTRA_EMAIL, email1+","+email2);
                                                                if(intent.resolveActivity(mainPage.getPackageManager()) != null){
                                                                    startActivity(intent);
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                });
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
                Map<String, Integer> mapAmountRecipeUid1 = room.getRecipeUid1().getRecipeAmount();
                Map<String , Integer>mapGramsRecipeUid1 = room.getRecipeUid1().getRecipeG();
                Map<String , Integer> mapMLRecipeUid1 = room.getRecipeUid1().getRecipeML();
                Map<String, Integer> mapAmountRecipeUid2 = room.getRecipeUid2().getRecipeAmount();
                Map<String , Integer>mapGramsRecipeUid2 = room.getRecipeUid2().getRecipeG();
                Map<String , Integer> mapMLRecipeUid2 = room.getRecipeUid2().getRecipeML();
                Map<String , Integer>all;
                Map<String , Integer>allUid1;
                Map<String , Integer>allUid2;
                /*
                These loops all belong hashmaps of the amount , grams and ML that the purpose is to concatanate
                 into a one array that is called all
                 */
                all = putAllInOne(mapAmount , mapGrams , mapML);
                allUid1 = putAllInOne(mapAmountRecipeUid1 , mapGramsRecipeUid1 , mapMLRecipeUid1);
                allUid2 = putAllInOne(mapAmountRecipeUid2,mapGramsRecipeUid2,mapMLRecipeUid2);
                /*
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

                 */
                adapter = new CustomAdapterIngridients(all, mainPage , roomID , room.getRecipe().getRecipeName());
                adapter2 = new CustomAdapterPersonal(all, mainPage, roomID, room.getRecipe().getRecipeName(), allUid1, allUid2);
                recyclerView2.setAdapter(adapter2);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            public Map<String, Integer> putAllInOne(Map<String, Integer> mapAmount, Map<String, Integer> mapGrams , Map<String, Integer> mapML) {
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
                return all;
            }
        });
        // Inflate the layout for this fragment
        deleteRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRoom(myRef , roomID);
            }
        });
        return view;
    }

    public void deleteRoom(final DatabaseReference myRef, final String roomID) {
        final CharSequence[] options = { "Yes", "No"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure you want to delete this room?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Yes"))
                {
                    myRef.child(roomID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String uid1 = dataSnapshot.child("uid1").getValue().toString();
                            String uid2 = dataSnapshot.child("uid2").getValue().toString();
                            Log.d("delete",uid1+"\n"+uid2);
                            myRef.getParent().child("users").child(uid1).child("myRooms").child(roomID).removeValue();
                            myRef.getParent().child("users").child(uid2).child("myRooms").child(roomID).removeValue();
                            myRef.child(roomID).removeValue();
                            getActivity().onBackPressed();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else if (options[item].equals("No")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
}