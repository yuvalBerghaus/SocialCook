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

public class CustomAdapterIngridients extends RecyclerView.Adapter<CustomAdapterIngridients.MyViewHolder>{
    RoomInfo recipeInfoPage;
    private Map<String , Integer> dataSet;
    MainPage mainPage;
    String roomID;
    String recipeName;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textViewName;
        Button saveButton;
        TextView sharedText;
        TextView typeSpecifier;
        TextView maxAmount;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemName);
            this.saveButton = (Button) itemView.findViewById(R.id.saveButton);
            this.sharedText = (TextView) itemView.findViewById(R.id.sharedText);
            this.typeSpecifier = (TextView) itemView.findViewById(R.id.log);
            this.maxAmount = (TextView) itemView.findViewById(R.id.maxAmount); // this variable contains the value of the max amount of each item
        }


    }

    public CustomAdapterIngridients(Map<String, Integer> data, MainPage mainPage, String roomID, String recipeName) {
        this.dataSet = data;
        this.mainPage = mainPage;
        this.roomID = roomID;
        this.recipeName = recipeName;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_ingridients, parent, false);

        view.setOnTouchListener(RoomInfo.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        final String key = (new ArrayList<>(dataSet.keySet())).get(listPosition); // IMPORTANT!!!!
        String value = Objects.requireNonNull(dataSet.get(key).toString());
        final DatabaseReference myRef = FireBase.getDataBase().getReference("recipes");
        TextView textViewName = holder.textViewName;
        final TextView sharedText = holder.sharedText;
        final TextView typeSpecifier = holder.typeSpecifier;
        final CardView cardView = holder.cardView;
        final Button buttonSave = holder.saveButton;
        final TextView maxAmount = holder.maxAmount;
        final FirebaseDatabase database = FireBase.getDataBase();
        final DatabaseReference recipeRef = database.getReference("recipes").child(recipeName);
        final DatabaseReference amountRef = database.getReference("rooms").child(roomID).child("recipe").child("recipeAmount");
        final DatabaseReference gramsRef = database.getReference("rooms").child(roomID).child("recipe").child("recipeG");
        final DatabaseReference mlRef = database.getReference("rooms").child(roomID).child("recipe").child("recipeML");
        textViewName.setText(key);
        //ref to shared amount
        amountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            /*
            This snapshot contains :
            1)datasnapshot1 -> here we fill all of the items of hashtable that contain Amounts
            2)datasnapshot2 -> recipeRef belongs to the original item quantities we use it in order to compare to the immidiate text in order to display if the amount is correct
             */
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot1) {
                if(dataSnapshot1.hasChild(key)) {
                    typeSpecifier.setText("Units");
                    //ref to recipe amount
                    recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            final Recipe recipe = dataSnapshot2.getValue(Recipe.class);
                            final Recipe recipeShared = dataSnapshot1.getValue(Recipe.class);
                            if(recipe.getRecipeAmount().containsKey(key)) {
                                Log.d("AfterMath" , recipe.getRecipeAmount().get(key).toString());
                                maxAmount.setText(recipe.getRecipeAmount().get(key).toString());
                                sharedText.setText(dataSnapshot1.child(key).getValue().toString());
                                int recipeValue = Integer.parseInt(recipe.getRecipeAmount().get(key).toString());
                                int sharedValue = Integer.parseInt(dataSnapshot1.child(key).getValue().toString());

                                if(sharedValue != recipeValue) {
                                    sharedText.setTextColor(Color.RED);
                                }
                                else {
                                    sharedText.setTextColor(Color.WHITE);
                                }
                            }
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
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        gramsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            /*
            This snapshot contains :
            1)datasnapshot1 -> here we fill all of the items of hashtable that contain Grams
            2)datasnapshot2 -> recipeRef belongs to the original item quantities we use it in order to compare to the immidiate text in order to display if the amount is correct
             */
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot1) {
                if(dataSnapshot1.hasChild(key)) {
                    typeSpecifier.setText("Grams");
                    //ref to recipe grams
                    recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            final Recipe recipe = dataSnapshot2.getValue(Recipe.class);
                            final Recipe recipeShared = dataSnapshot1.getValue(Recipe.class);
                            Log.d("ROSH","Recipe grams from DB = "+recipe.getRecipeG().get(key)+"\nRecipe grams from text is "+sharedText.getText().toString());
                            if(recipe.getRecipeG().containsKey(key)) {
                                Log.d("AfterMath" , recipe.getRecipeG().get(key).toString());
                                maxAmount.setText(recipe.getRecipeG().get(key).toString());
                                sharedText.setText(dataSnapshot1.child(key).getValue().toString());
                                int recipeValue = Integer.parseInt(recipe.getRecipeG().get(key).toString());
                                int sharedValue = Integer.parseInt(dataSnapshot1.child(key).getValue().toString());

                                if(sharedValue != recipeValue) {
                                    sharedText.setTextColor(Color.RED);
                                }
                                else {
                                    sharedText.setTextColor(Color.WHITE);
                                }
                            }
                            int recipeValue = Integer.parseInt(recipe.getRecipeG().get(key).toString());
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

        mlRef.addListenerForSingleValueEvent(new ValueEventListener() {
            /*
            This snapshot contains :
            1)datasnapshot1 -> here we fill all of the items of hashtable that contain Mili-Liters
            2)datasnapshot2 -> recipeRef belongs to the original item quantities we use it in order to compare to the immidiate text in order to display if the amount is correct
             */
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot1) {
                if(dataSnapshot1.hasChild(key)) {
                    typeSpecifier.setText("ML");
                    //ref to recipe mili-liters
                    recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            final Recipe recipe = dataSnapshot2.getValue(Recipe.class);
                            final Recipe recipeShared = dataSnapshot1.getValue(Recipe.class);
                            Log.d("ROSH","Recipe mili-liters from DB = "+recipe.getRecipeML().get(key)+"\nRecipe mili-liters from text is "+sharedText.getText().toString());
                            if(recipe.getRecipeML().containsKey(key)) {
                                Log.d("AfterMath" , recipe.getRecipeML().get(key).toString());
                                maxAmount.setText(recipe.getRecipeML().get(key).toString());
                                sharedText.setText(dataSnapshot1.child(key).getValue().toString());
                                int recipeValue = Integer.parseInt(recipe.getRecipeML().get(key).toString());
                                int sharedValue = Integer.parseInt(dataSnapshot1.child(key).getValue().toString());

                                if(sharedValue != recipeValue) {
                                    sharedText.setTextColor(Color.RED);
                                }
                                else {
                                    sharedText.setTextColor(Color.WHITE);
                                }
                            }
                            int recipeValue = Integer.parseInt(recipe.getRecipeML().get(key).toString());
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
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
