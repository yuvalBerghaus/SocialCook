package com.example.socialcook;

import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialcook.afterlogin.activities.MainPage;
import com.example.socialcook.classes.Recipe;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class CustomAdapterPersonal extends RecyclerView.Adapter<CustomAdapterPersonal.MyViewHolder>{

    RoomInfo recipeInfoPage;
    private Map<String , Integer> dataSet;
    private Map<String , Integer> dataSetUid1;
    private Map<String , Integer> dataSetUid2;
    MainPage mainPage;
    String roomID;
    String recipeName;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textViewName;
        Button saveButton;
        TextView textInput;
        TextView typeSpecifier;
        TextView maxAmount;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemName);
            this.saveButton = (Button) itemView.findViewById(R.id.saveButton);
            this.textInput = (TextView) itemView.findViewById(R.id.itemValue);
            this.typeSpecifier = (TextView) itemView.findViewById(R.id.log);
            this.maxAmount = (TextView) itemView.findViewById(R.id.leftOver); // this variable contains the value of the max amount of each item
        }


    }

    public CustomAdapterPersonal(Map<String, Integer> data, MainPage mainPage, String roomID, String recipeName , Map<String , Integer>dataUid1 , Map<String , Integer>dataUid2) {
        this.dataSet = data;
        this.mainPage = mainPage;
        this.roomID = roomID;
        this.recipeName = recipeName;
        this.dataSetUid1 = dataUid1;
        this.dataSetUid2 = dataUid2;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_personal, parent, false);

        view.setOnTouchListener(RoomInfo.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        final String key = (new ArrayList<>(dataSetUid1.keySet())).get(listPosition); // IMPORTANT!!!!
        String value = Objects.requireNonNull(dataSetUid1.get(key).toString());
        final DatabaseReference myRef = FireBase.getDataBase().getReference("recipes");
        final DatabaseReference roomsRef = FireBase.getDataBase().getReference("rooms");
        final TextView textViewName = holder.textViewName;
        final TextView textInput = holder.textInput;
        final TextView typeSpecifier = holder.typeSpecifier;
        final CardView cardView = holder.cardView;
        final Button buttonSave = holder.saveButton;
        final TextView leftOver = holder.maxAmount;
        final FirebaseDatabase database = FireBase.getDataBase();
        final DatabaseReference recipeRef = database.getReference("recipes").child(recipeName);
        final DatabaseReference rootRef = database.getReference();
        final DatabaseReference amountRef = database.getReference("rooms").child(roomID).child("recipeUid1").child("recipeAmount");
        final DatabaseReference gramsRef = database.getReference("rooms").child(roomID).child("recipeUid1").child("recipeG");
        final DatabaseReference mlRef = database.getReference("rooms").child(roomID).child("recipeUid1").child("recipeML");
        final DatabaseReference personalAmountRef = database.getReference("rooms").child(roomID).child(FireBase.getAuth().getUid()).child("recipe").child("recipeAmount");
        final DatabaseReference personalGramsRef = database.getReference("rooms").child(roomID).child("recipe").child("recipeG");
        final DatabaseReference personalMlRef = database.getReference("rooms").child(roomID).child("recipe").child("recipeML");
        textViewName.setText(key);
        textInput.setText(value);
        rootRef.child("rooms").child(roomID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Recipe recipe = dataSnapshot.getValue(Recipe.class);
                String uid;
                String key;
                String value;
                if(FireBase.getAuth().getUid() == dataSnapshot.child("uid1").getValue()) {
                    uid = dataSnapshot.child("uid1").getValue().toString();
                    key = (new ArrayList<>(dataSetUid1.keySet())).get(listPosition);
                    value = Objects.requireNonNull(dataSetUid1.get(key).toString());
                    textViewName.setText(key);
                    textInput.setText(value);
                    draw(uid , key , value);

                }
                else {
                    uid = dataSnapshot.child("uid2").getValue().toString();
                    key = (new ArrayList<>(dataSetUid2.keySet())).get(listPosition);
                    value = Objects.requireNonNull(dataSetUid2.get(key).toString());
                    textViewName.setText(key);
                    textInput.setText(value);
                    draw(uid , key , value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            public void draw(String uid , final String key , String value) {
                amountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    /*
                    This snapshot contains :
                    1)datasnapshot1 -> here we fill all of the items of hashtable that contain Amounts
                    2)datasnapshot2 -> recipeRef belongs to the original item quantities we use it in order to compare to the immidiate text in order to display if the amount is correct
                     --------------------------------------------------------------------------------------------
                     */
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot1) {
                        if(dataSnapshot1.hasChild(key)) {
                            typeSpecifier.setText("Amount");
                            recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    final Recipe recipe = dataSnapshot2.getValue(Recipe.class);
                                    Log.d("ROSH","Recipe amount from DB = "+recipe.getRecipeAmount().get(key)+"\nRecipe amount from text is "+textInput.getText().toString());
                                    if(recipe.getRecipeAmount().containsKey(key)) {
                                        Log.d("AfterMath" , recipe.getRecipeAmount().get(key).toString());
                                        int recipeValue = Integer.parseInt(recipe.getRecipeAmount().get(key).toString());
                                        int InputValue = 0;
                                        if (!textInput.getText().toString().matches("")) {
                                            InputValue = Integer.parseInt(textInput.getText().toString());
                                        }
                                        else {
                                            InputValue = 0;
                                        }
                                leftOver.setText(recipe.getRecipeAmount().get(key).toString());;
                                if(InputValue != recipeValue) {
                                    leftOver.setTextColor(Color.RED);

                                }
                                else {
                                    leftOver.setTextColor(Color.WHITE);
                                }
                                    }
                                    Log.d("DIE KVAR" , ""+Integer.parseInt(dataSnapshot1.child(key).getValue().toString()));
                                    textInput.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            int recipeValue = Integer.parseInt(recipe.getRecipeAmount().get(key).toString());
                                            int InputValue = 0;
                                            if (!textInput.getText().toString().matches("")) {
                                                InputValue = Integer.parseInt(textInput.getText().toString());
                                            }
                                            if(InputValue > recipeValue) {
                                                Log.d("MIKE" , "YESSSS!!!!");
                                                buttonSave.setClickable(false);
                                                buttonSave.setAlpha(0.5f);
                                                leftOver.setTextColor(Color.RED);

                                            }
                                            else {
                                                buttonSave.setClickable(true);
                                                buttonSave.setAlpha(1f);
                                                leftOver.setTextColor(Color.WHITE);
                                            }
                                            if (InputValue != recipeValue) {
                                                leftOver.setTextColor(Color.RED);
                                            }
                                            else {
                                                leftOver.setTextColor(Color.WHITE);
                                            }
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {

                                        }
                                    });
                                    int recipeValue = Integer.parseInt(recipe.getRecipeAmount().get(key).toString());
                                    int roomValue = Integer.parseInt(dataSnapshot1.child(key).getValue().toString());
                                    if(recipeValue == roomValue) {
                                        //enter here...........................................................
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        /*
            This snapshot contains :
            1)datasnapshot3 -> gramsRef refers to the current quantity of hashtable grams that was stored in the room database
         */
                gramsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                        if(dataSnapshot3.hasChild(key)) {
                            typeSpecifier.setText("Grams");
                            recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final Recipe recipe = dataSnapshot.getValue(Recipe.class);
                                    Log.d("GUY",key);
                                    Log.d("YUVAL" , recipe.getRecipeName());
                                    if(recipe.getRecipeG().containsKey(key)) {
                                        Log.d("AfterMath" , recipe.getRecipeG().get(key).toString());
                                        leftOver.setText(recipe.getRecipeG().get(key).toString());
                                        //if(recipe.getRecipeG().get(key) == )
                                        int recipeValue = Integer.parseInt(recipe.getRecipeG().get(key).toString());
                                        int InputValue = 0;
                                        if (!textInput.getText().toString().matches("")) {
                                            InputValue = Integer.parseInt(textInput.getText().toString());
                                        }
                                        else {
                                            InputValue = 0;
                                        }
                                        if(InputValue != recipeValue) {
                                            leftOver.setTextColor(Color.RED);

                                        }
                                        else {
                                            leftOver.setTextColor(Color.WHITE);
                                        }
                                    }
                                    textInput.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            int recipeValue = Integer.parseInt(recipe.getRecipeG().get(key).toString());
                                            int InputValue = 0;
                                            if (!textInput.getText().toString().matches("")) {
                                                InputValue = Integer.parseInt(textInput.getText().toString());
                                            }
                                            if(InputValue > recipeValue) {
                                                Log.d("MIKE" , "YESSSS!!!!");
                                                buttonSave.setClickable(false);
                                                buttonSave.setAlpha(0.5f);
                                                leftOver.setTextColor(Color.RED);

                                            }
                                            else {
                                                buttonSave.setClickable(true);
                                                buttonSave.setAlpha(1f);
                                                leftOver.setTextColor(Color.WHITE);
                                            }
                                            if (InputValue != recipeValue) {
                                                leftOver.setTextColor(Color.RED);
                                            }
                                            else {
                                                leftOver.setTextColor(Color.WHITE);
                                            }
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        /*
            This snapshot contains :
            1)datasnapshot4 -> mlRef refers to the current quantity of hashtable ML that was stored in the room database
         */
                mlRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot4) {
                        if(dataSnapshot4.hasChild(key)) {
                            typeSpecifier.setText("ML");
                            recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final Recipe recipe = dataSnapshot.getValue(Recipe.class);
                                    Log.d("GUY",key);
                                    Log.d("YUVAL" , recipe.getRecipeName());
                                    if(recipe.getRecipeML().containsKey(key)) {
                                        Log.d("AfterMath" , recipe.getRecipeML().get(key).toString());
                                        leftOver.setText(recipe.getRecipeML().get(key).toString());

                                        int recipeValue = Integer.parseInt(recipe.getRecipeML().get(key).toString());
                                        int InputValue = 0;
                                        if (!textInput.getText().toString().matches("")) {
                                            InputValue = Integer.parseInt(textInput.getText().toString());
                                        }
                                        else {
                                            InputValue = 0;
                                        }
                                        if(InputValue != recipeValue) {
                                            leftOver.setTextColor(Color.RED);

                                        }
                                        else {
                                            leftOver.setTextColor(Color.WHITE);
                                        }
                                    }
                                    textInput.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            int recipeValue = Integer.parseInt(recipe.getRecipeML().get(key).toString());
                                            int InputValue = 0;
                                            if (!textInput.getText().toString().matches("")) {
                                                InputValue = Integer.parseInt(textInput.getText().toString());
                                            }
                                            if(InputValue > recipeValue) {
                                                Log.d("MIKE" , "YESSSS!!!!");
                                                buttonSave.setClickable(false);
                                                buttonSave.setAlpha(0.5f);
                                                leftOver.setTextColor(Color.RED);

                                            }
                                            else {
                                                buttonSave.setClickable(true);
                                                buttonSave.setAlpha(1f);
                                                leftOver.setTextColor(Color.WHITE);
                                            }
                                            if (InputValue != recipeValue) {
                                                leftOver.setTextColor(Color.RED);
                                            }
                                            else {
                                                leftOver.setTextColor(Color.WHITE);
                                            }
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // Once we click on the save button of each row this function will be executed
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    amountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(key)) {
                                int diff = Integer.parseInt(dataSnapshot.child(key).getValue().toString()) - Integer.parseInt(textInput.getText().toString());
                                if(diff < 0) {
                                    amountRef.getParent().getParent().child("logs").push().setValue(FireBase.getAuth().getCurrentUser().getDisplayName()+" added "+Math.abs(diff)+" "+dataSnapshot.child(key).getKey());
                                }
                                else if(diff > 0) {
                                    amountRef.getParent().getParent().child("logs").push().setValue(FireBase.getAuth().getCurrentUser().getDisplayName()+" removed "+Math.abs(diff)+" "+dataSnapshot.child(key).getKey());
                                }
                                Log.d("DIFF" , "diff = "+diff+"\nDb = "+textInput.getText().toString());///////////////////////////////////////////////////////////////////////////////////////////////////////////
                                amountRef.child(key).setValue(Integer.parseInt(textInput.getText().toString()));

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    gramsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(key)) {
                                int diff = Integer.parseInt(dataSnapshot.child(key).getValue().toString()) - Integer.parseInt(textInput.getText().toString());
                                if(diff < 0) {
                                    gramsRef.getParent().getParent().child("logs").push().setValue(FireBase.getAuth().getCurrentUser().getDisplayName()+" added "+Math.abs(diff)+" grams of "+dataSnapshot.child(key).getKey());
                                }
                                else if(diff > 0) {
                                    gramsRef.getParent().getParent().child("logs").push().setValue(FireBase.getAuth().getCurrentUser().getDisplayName()+" removed "+Math.abs(diff)+" grams of "+dataSnapshot.child(key).getKey());
                                }
                                Log.d("DIFF" , "diff = "+diff+"\nDb = "+textInput.getText().toString());
                                gramsRef.child(key).setValue(Integer.parseInt(textInput.getText().toString()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    mlRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(key)) {
                                int diff = Integer.parseInt(dataSnapshot.child(key).getValue().toString()) - Integer.parseInt(textInput.getText().toString());
                                if(diff < 0) {
                                    mlRef.getParent().getParent().child("logs").push().setValue(FireBase.getAuth().getCurrentUser().getDisplayName()+" added "+Math.abs(diff)+" ml of "+dataSnapshot.child(key).getKey());
                                }
                                else if(diff > 0) {
                                    mlRef.getParent().getParent().child("logs").push().setValue(FireBase.getAuth().getCurrentUser().getDisplayName()+" removed "+Math.abs(diff)+" ml of "+dataSnapshot.child(key).getKey());
                                }
                                Log.d("DIFF" , "diff = "+diff+"\nDb = "+textInput.getText().toString());
                                mlRef.child(key).setValue(Integer.parseInt(textInput.getText().toString()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                catch (Exception NullPointerException) {
                    System.out.println("du hello");
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
