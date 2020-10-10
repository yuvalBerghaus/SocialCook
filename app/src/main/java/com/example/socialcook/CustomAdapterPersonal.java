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
import android.widget.Toast;

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
    String recipeUid;
    int initiateInputAmount;
    int recipeValueAmount;
    int sharedValueAmount;
    int InputValueAmount;
    int initiateInputGrams;
    int recipeValueGrams;
    int sharedValueGrams;
    int InputValueGrams;
    int initiateInputML;
    int recipeValueML;
    int sharedValueML;
    int InputValueML;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textViewName;
        Button saveButton;
        TextView textInput;
        TextView typeSpecifier;
        TextView leftOver;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemName);
            this.saveButton = (Button) itemView.findViewById(R.id.saveButton);
            this.textInput = (TextView) itemView.findViewById(R.id.itemValue);
            this.typeSpecifier = (TextView) itemView.findViewById(R.id.log);
            this.leftOver = (TextView) itemView.findViewById(R.id.leftOver); // this variable contains the value of the left over of each item
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
        final TextView leftOver = holder.leftOver;
        final FirebaseDatabase database = FireBase.getDataBase();
        final DatabaseReference recipeRef = database.getReference("recipes").child(recipeName);
        final DatabaseReference rootRef = database.getReference();
        final DatabaseReference amountRef = database.getReference("rooms").child(roomID).child("recipe").child("recipeAmount");
        final DatabaseReference gramsRef = database.getReference("rooms").child(roomID).child("recipe").child("recipeG");
        final DatabaseReference mlRef = database.getReference("rooms").child(roomID).child("recipe").child("recipeML");
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
                Log.d("TAGTAG",dataSnapshot.child("uid1").getValue().toString());
                if(FireBase.getAuth().getUid().toString().equals(dataSnapshot.child("uid1").getValue().toString())) {
                    uid = dataSnapshot.child("uid1").getValue().toString();
                    recipeUid = "recipeUid1";
                    key = (new ArrayList<>(dataSetUid1.keySet())).get(listPosition);
                    value = Objects.requireNonNull(dataSetUid1.get(key).toString());
                    textViewName.setText(key);
                    textInput.setText(value);
                    draw(uid , key , value, recipeUid);

                }
                else {
                    uid = dataSnapshot.child("uid2").getValue().toString();
                    recipeUid = "recipeUid2";
                    key = (new ArrayList<>(dataSetUid2.keySet())).get(listPosition);
                    value = Objects.requireNonNull(dataSetUid2.get(key).toString());
                    textViewName.setText(key);
                    textInput.setText(value);
                    draw(uid , key , value, recipeUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            public void draw(String uid , final String key , String value, final String recipeUid) {
                //Amount ref
                ValueEventListener valueChangeRecipeAmount = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Recipe recipe = dataSnapshot.child("recipes").child(recipeName).getValue(Recipe.class);
                        if(recipe.getRecipeAmount().containsKey(key)) {
                            typeSpecifier.setText("Units");
                            initiateInputAmount = Integer.parseInt(dataSnapshot.child("rooms").child(roomID).child(recipeUid).child("recipeAmount").child(key).getValue().toString());
                            recipeValueAmount = Integer.parseInt(recipe.getRecipeAmount().get(key).toString());
                            sharedValueAmount = Integer.parseInt(dataSnapshot.child("rooms").child(roomID).child("recipe").child("recipeAmount").child(key).getValue().toString());
                            InputValueAmount = 0;
                            if (!textInput.getText().toString().matches("")) {
                                InputValueAmount = Integer.parseInt(textInput.getText().toString());
                            }
                            else {
                                InputValueAmount = 0;
                            }
                            int leftOverValue = recipeValueAmount - sharedValueAmount;
                            Log.d("TEST", "leftOverValue"+leftOverValue+"sharedValueAmount"+sharedValueAmount+"InputValue"+InputValueAmount);
                            leftOver.setText(""+leftOverValue);

                            if(InputValueAmount > recipeValueAmount - (sharedValueAmount - InputValueAmount) || recipeValueAmount == sharedValueAmount) {
                                buttonSave.setClickable(false);
                                buttonSave.setAlpha(0.5f);
                                leftOver.setText("0");
                                leftOver.setTextColor(Color.RED);

                            }
                            else {
                                buttonSave.setClickable(true);
                                buttonSave.setAlpha(1f);
                                leftOver.setTextColor(Color.WHITE);
                            }
                            textInput.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                int leftOverValue = 0;
                                if (!textInput.getText().toString().matches("")) {
                                    InputValueAmount = Integer.parseInt(textInput.getText().toString());
                                    if (InputValueAmount - initiateInputAmount > 0) {
                                        leftOverValue = recipeValueAmount - sharedValueAmount - (InputValueAmount - initiateInputAmount);
                                    }
                                    else if (InputValueAmount - initiateInputAmount < 0) {
                                        leftOverValue = recipeValueAmount - sharedValueAmount - (InputValueAmount - initiateInputAmount);
                                    }
                                    else
                                    {
                                        leftOverValue = recipeValueAmount - sharedValueAmount;
                                    }
                                    leftOver.setText(""+leftOverValue);

                                }
                                else {
                                    InputValueAmount = 0;
                                    leftOver.setText(""+(recipeValueAmount-sharedValueAmount));
                                }

                                Log.d("TEST", "leftOverValue="+leftOverValue+"recipeValueAmount"+recipeValueAmount+"-sharedValueAmount"+sharedValueAmount+"-(InputValueAmount"+InputValueAmount +"-initiateInputAmount"+initiateInputAmount+")");

                                if(InputValueAmount > recipeValueAmount - (sharedValueAmount - initiateInputAmount)) {
                                    buttonSave.setClickable(false);
                                    buttonSave.setAlpha(0.5f);
                                    leftOver.setText("0");
                                    leftOver.setTextColor(Color.RED);

                                }
                                else {
                                    buttonSave.setClickable(true);
                                    buttonSave.setAlpha(1f);
                                    leftOver.setTextColor(Color.WHITE);
                                }
                            }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                rootRef.addValueEventListener(valueChangeRecipeAmount);
                //Grams ref
                ValueEventListener valueChangeRecipeGrams = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Recipe recipe = dataSnapshot.child("recipes").child(recipeName).getValue(Recipe.class);
                        if(recipe.getRecipeG().containsKey(key)) {
                            typeSpecifier.setText("Grams");
                            initiateInputGrams = Integer.parseInt(dataSnapshot.child("rooms").child(roomID).child(recipeUid).child("recipeG").child(key).getValue().toString());
                            recipeValueGrams = Integer.parseInt(recipe.getRecipeG().get(key).toString());
                            sharedValueGrams = Integer.parseInt(dataSnapshot.child("rooms").child(roomID).child("recipe").child("recipeG").child(key).getValue().toString());
                            InputValueGrams = 0;
                            if (!textInput.getText().toString().matches("")) {
                                InputValueGrams = Integer.parseInt(textInput.getText().toString());
                            }
                            else {
                                InputValueGrams = 0;
                            }
                            int leftOverValue = recipeValueGrams - sharedValueGrams;
                            Log.d("TEST", "leftOverValue"+leftOverValue+"sharedValueGrams"+sharedValueGrams+"InputValue"+InputValueGrams);
                            leftOver.setText(""+leftOverValue);

                            if(InputValueGrams > recipeValueGrams - (sharedValueGrams - InputValueGrams) || recipeValueGrams == sharedValueGrams) {
                                buttonSave.setClickable(false);
                                buttonSave.setAlpha(0.5f);
                                leftOver.setText("0");
                                leftOver.setTextColor(Color.RED);

                            }
                            else {
                                buttonSave.setClickable(true);
                                buttonSave.setAlpha(1f);
                                leftOver.setTextColor(Color.WHITE);
                            }
                            textInput.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    int leftOverValue = 0;
                                    if (!textInput.getText().toString().matches("")) {
                                        InputValueGrams = Integer.parseInt(textInput.getText().toString());
                                        if (InputValueGrams - initiateInputGrams > 0) {
                                            leftOverValue = recipeValueGrams - sharedValueGrams - (InputValueGrams - initiateInputGrams);
                                        }
                                        else if (InputValueGrams - initiateInputGrams < 0) {
                                            leftOverValue = recipeValueGrams - sharedValueGrams - (InputValueGrams - initiateInputGrams);
                                        }
                                        else
                                        {
                                            leftOverValue = recipeValueGrams - sharedValueGrams;
                                        }
                                        leftOver.setText(""+leftOverValue);

                                    }
                                    else {
                                        InputValueGrams = 0;
                                        leftOver.setText(""+(recipeValueGrams-sharedValueGrams));
                                    }

                                    Log.d("TEST", "leftOverValue="+leftOverValue+"recipeValueGrams"+recipeValueGrams+"-sharedValueGrams"+sharedValueGrams+"-(InputValueGrams"+InputValueGrams +"-initiateInputGrams"+initiateInputGrams+")");

                                    if(InputValueGrams > recipeValueGrams - (sharedValueGrams - initiateInputGrams)) {
                                        buttonSave.setClickable(false);
                                        buttonSave.setAlpha(0.5f);
                                        leftOver.setText("0");
                                        leftOver.setTextColor(Color.RED);

                                    }
                                    else {
                                        buttonSave.setClickable(true);
                                        buttonSave.setAlpha(1f);
                                        leftOver.setTextColor(Color.WHITE);
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                rootRef.addValueEventListener(valueChangeRecipeGrams);

                //Mili-Liters ref
                ValueEventListener valueChangeRecipeML = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Recipe recipe = dataSnapshot.child("recipes").child(recipeName).getValue(Recipe.class);
                        if(recipe.getRecipeML().containsKey(key)) {
                            typeSpecifier.setText("ML");
                            initiateInputML = Integer.parseInt(dataSnapshot.child("rooms").child(roomID).child(recipeUid).child("recipeML").child(key).getValue().toString());
                            recipeValueML = Integer.parseInt(recipe.getRecipeML().get(key).toString());
                            sharedValueML = Integer.parseInt(dataSnapshot.child("rooms").child(roomID).child("recipe").child("recipeML").child(key).getValue().toString());
                            InputValueML = 0;
                            if (!textInput.getText().toString().matches("")) {
                                InputValueML = Integer.parseInt(textInput.getText().toString());
                            }
                            else {
                                InputValueML = 0;
                            }
                            int leftOverValue = recipeValueML - sharedValueML;
                            Log.d("TEST", "leftOverValue"+leftOverValue+"sharedValueML"+sharedValueML+"InputValue"+InputValueML);
                            leftOver.setText(""+leftOverValue);

                            if(InputValueML > recipeValueML - (sharedValueML - InputValueML) || recipeValueML == sharedValueML) {
                                buttonSave.setClickable(false);
                                buttonSave.setAlpha(0.5f);
                                leftOver.setText("0");
                                leftOver.setTextColor(Color.RED);

                            }
                            else {
                                buttonSave.setClickable(true);
                                buttonSave.setAlpha(1f);
                                leftOver.setTextColor(Color.WHITE);
                            }
                            textInput.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    int leftOverValue = 0;
                                    if (!textInput.getText().toString().matches("")) {
                                        InputValueML = Integer.parseInt(textInput.getText().toString());
                                        if (InputValueML - initiateInputML > 0) {
                                            leftOverValue = recipeValueML - sharedValueML - (InputValueML - initiateInputML);
                                        }
                                        else if (InputValueML - initiateInputML < 0) {
                                            leftOverValue = recipeValueML - sharedValueML - (InputValueML - initiateInputML);
                                        }
                                        else
                                        {
                                            leftOverValue = recipeValueML - sharedValueML;
                                        }
                                        leftOver.setText(""+leftOverValue);

                                    }
                                    else {
                                        InputValueML = 0;
                                        leftOver.setText(""+(recipeValueML-sharedValueML));
                                    }

                                    Log.d("TEST", "leftOverValue="+leftOverValue+"recipeValueML"+recipeValueML+"-sharedValueML"+sharedValueML+"-(InputValueML"+InputValueML +"-initiateInputML"+initiateInputML+")");

                                    if(InputValueML > recipeValueML - (sharedValueML - initiateInputML)) {
                                        buttonSave.setClickable(false);
                                        buttonSave.setAlpha(0.5f);
                                        leftOver.setText("0");
                                        leftOver.setTextColor(Color.RED);

                                    }
                                    else {
                                        buttonSave.setClickable(true);
                                        buttonSave.setAlpha(1f);
                                        leftOver.setTextColor(Color.WHITE);
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                rootRef.addValueEventListener(valueChangeRecipeML);
            }
        });

        // Once we click on the save button of each row this function will be executed
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //ref to personal amount
                    Log.d("WHATISTHEKEY", ""+key);
                    rootRef.child("rooms").child(roomID).child(recipeUid).child("recipeAmount").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(key)) {
                                if (!textInput.getText().toString().matches("")) {
                                    int diff = Integer.parseInt(dataSnapshot.child(key).getValue().toString()) - Integer.parseInt(textInput.getText().toString());
                                    if(diff < 0) {
                                        rootRef.child("rooms").child(roomID).child("logs").push().setValue(FireBase.getAuth().getCurrentUser().getDisplayName()+" added "+Math.abs(diff)+" "+dataSnapshot.child(key).getKey());
                                    }
                                    else if(diff > 0) {
                                        rootRef.child("rooms").child(roomID).child("logs").push().setValue(FireBase.getAuth().getCurrentUser().getDisplayName()+" removed "+Math.abs(diff)+" "+dataSnapshot.child(key).getKey());
                                    }
                                }
                                rootRef.child("rooms").child(roomID).child(recipeUid).child("recipeAmount").child(key).setValue(Integer.parseInt(textInput.getText().toString()));

                                rootRef.child("rooms").child(roomID).child("recipeUid1").child("recipeAmount").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull final DataSnapshot dataSnapshotUid1) {
                                        Log.d("do i even", "do i even");
                                        rootRef.child("rooms").child(roomID).child("recipeUid2").child("recipeAmount").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshotUid2) {
                                                int uid1Value;
                                                int uid2Value;
                                                uid1Value = Integer.parseInt(dataSnapshotUid1.getValue().toString());
                                                uid2Value = Integer.parseInt(dataSnapshotUid2.getValue().toString());
                                                rootRef.child("rooms").child(roomID).child("recipe").child("recipeAmount").child(key).setValue(uid1Value + uid2Value);
                                                Toast.makeText(holder.cardView.getContext(), key+" amount has been updated!", Toast.LENGTH_SHORT).show();
                                                Log.d("check!!!", "uid1: "+uid1Value+" uid2: "+uid2Value+" sharedValue"+dataSnapshot.child(key).getValue().toString());
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
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //ref to personal grams
                    rootRef.child("rooms").child(roomID).child(recipeUid).child("recipeG").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(key)) {
                                if (!textInput.getText().toString().matches("")) {
                                    int diff = Integer.parseInt(dataSnapshot.child(key).getValue().toString()) - Integer.parseInt(textInput.getText().toString());
                                    if(diff < 0) {
                                        rootRef.child("rooms").child(roomID).child("logs").push().setValue(FireBase.getAuth().getCurrentUser().getDisplayName()+" added "+Math.abs(diff)+" "+dataSnapshot.child(key).getKey());
                                    }
                                    else if(diff > 0) {
                                        rootRef.child("rooms").child(roomID).child("logs").push().setValue(FireBase.getAuth().getCurrentUser().getDisplayName()+" removed "+Math.abs(diff)+" "+dataSnapshot.child(key).getKey());
                                    }
                                }
                                rootRef.child("rooms").child(roomID).child(recipeUid).child("recipeG").child(key).setValue(Integer.parseInt(textInput.getText().toString()));

                                rootRef.child("rooms").child(roomID).child("recipeUid1").child("recipeG").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull final DataSnapshot dataSnapshotUid1) {
                                        Log.d("do i even", "do i even");
                                        rootRef.child("rooms").child(roomID).child("recipeUid2").child("recipeG").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshotUid2) {
                                                int uid1Value;
                                                int uid2Value;
                                                uid1Value = Integer.parseInt(dataSnapshotUid1.getValue().toString());
                                                uid2Value = Integer.parseInt(dataSnapshotUid2.getValue().toString());
                                                rootRef.child("rooms").child(roomID).child("recipe").child("recipeG").child(key).setValue(uid1Value + uid2Value);
                                                Toast.makeText(holder.cardView.getContext(), key+" amount has been updated!", Toast.LENGTH_SHORT).show();
                                                Log.d("check!!!", "uid1: "+uid1Value+" uid2: "+uid2Value+" sharedValue"+dataSnapshot.child(key).getValue().toString());
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
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //ref to personal mili-liters
                    rootRef.child("rooms").child(roomID).child(recipeUid).child("recipeML").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(key)) {
                                if (!textInput.getText().toString().matches("")) {
                                    int diff = Integer.parseInt(dataSnapshot.child(key).getValue().toString()) - Integer.parseInt(textInput.getText().toString());
                                    if(diff < 0) {
                                        rootRef.child("rooms").child(roomID).child("logs").push().setValue(FireBase.getAuth().getCurrentUser().getDisplayName()+" added "+Math.abs(diff)+" "+dataSnapshot.child(key).getKey());
                                    }
                                    else if(diff > 0) {
                                        rootRef.child("rooms").child(roomID).child("logs").push().setValue(FireBase.getAuth().getCurrentUser().getDisplayName()+" removed "+Math.abs(diff)+" "+dataSnapshot.child(key).getKey());
                                    }
                                }
                                rootRef.child("rooms").child(roomID).child(recipeUid).child("recipeML").child(key).setValue(Integer.parseInt(textInput.getText().toString()));

                                rootRef.child("rooms").child(roomID).child("recipeUid1").child("recipeML").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull final DataSnapshot dataSnapshotUid1) {
                                        Log.d("do i even", "do i even");
                                        rootRef.child("rooms").child(roomID).child("recipeUid2").child("recipeML").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshotUid2) {
                                                int uid1Value;
                                                int uid2Value;
                                                uid1Value = Integer.parseInt(dataSnapshotUid1.getValue().toString());
                                                uid2Value = Integer.parseInt(dataSnapshotUid2.getValue().toString());
                                                rootRef.child("rooms").child(roomID).child("recipe").child("recipeML").child(key).setValue(uid1Value + uid2Value);
                                                Toast.makeText(holder.cardView.getContext(), key+" amount has been updated!", Toast.LENGTH_SHORT).show();
                                                Log.d("check!!!", "uid1: "+uid1Value+" uid2: "+uid2Value+" sharedValue"+dataSnapshot.child(key).getValue().toString());
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

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

}
