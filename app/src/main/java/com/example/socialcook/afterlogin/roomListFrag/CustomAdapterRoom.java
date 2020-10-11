package com.example.socialcook.afterlogin.roomListFrag;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialcook.R;
import com.example.socialcook.afterlogin.activities.MainPage;

import com.example.socialcook.classes.Recipe;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomAdapterRoom extends RecyclerView.Adapter<CustomAdapterRoom.MyViewHolder>{

    private ArrayList<String> dataSet;
    MainPage mainPage;
    Recipe chosenRecipe;
    private NotificationManagerCompat notificationManager;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textViewName;
        Button infoButton;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemName);
            this.infoButton = (Button) itemView.findViewById(R.id.buttonSelectRoom);
        }


    }

    public CustomAdapterRoom(ArrayList<String> data , MainPage mainPage) {
        this.dataSet = data;
        this.mainPage = mainPage;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_roomlist_layout, parent, false);

        view.setOnTouchListener(RoomsListFrag.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        final TextView textViewName = holder.textViewName;
        CardView cardView = holder.cardView;
        final Button buttonInfo = holder.infoButton;
        //textViewName.setText(dataSet.get(listPosition));
        final FirebaseDatabase database = FireBase.getDataBase();
        DatabaseReference myRef = database.getReference("rooms").child(dataSet.get(listPosition).toString());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                DatabaseReference myRefUsers = database.getReference("users");
                myRefUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                        if(FireBase.getAuth().getCurrentUser().getUid().toString().matches(dataSnapshot.child("uid2").getValue().toString())) {
                            textViewName.setText(dataSnapshot.child("recipe").child("recipeName").getValue().toString()+" with "+dataSnapshot2.child(dataSnapshot.child("uid1").getValue().toString()).child("name").getValue().toString());
                        }
                        else {
                            textViewName.setText(dataSnapshot.child("recipe").child("recipeName").getValue().toString()+" with "+dataSnapshot2.child(dataSnapshot.child("uid2").getValue().toString()).child("name").getValue().toString());
                        }
                        Log.d("WAKA", dataSnapshot.child("recipe").child("recipeName").getValue().toString());
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
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPage.loadRoomInfo(dataSet.get(listPosition));
                buttonInfo.setClickable(false);
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
