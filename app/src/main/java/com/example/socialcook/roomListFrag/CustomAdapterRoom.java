package com.example.socialcook.roomListFrag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialcook.R;
import com.example.socialcook.afterlogin.activities.MainPage;

import com.example.socialcook.classes.Recipe;
import com.example.socialcook.classes.Room;
import com.example.socialcook.firebase.FireBase;

import java.util.ArrayList;

public class CustomAdapterRoom extends RecyclerView.Adapter<CustomAdapterRoom.MyViewHolder>{

    private ArrayList<Room> dataSet;
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
            this.textViewName = (TextView) itemView.findViewById(R.id.textView2);
            this.infoButton = (Button) itemView.findViewById(R.id.buttonSelectRoom);
        }


    }

    public CustomAdapterRoom(ArrayList<Room> data , MainPage mainPage) {
        this.dataSet = data;
        this.mainPage = mainPage;
        notificationManager = NotificationManagerCompat.from(mainPage);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_userlist_layout, parent, false);

        view.setOnTouchListener(RoomsListFrag.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        TextView textViewName = holder.textViewName;
        CardView cardView = holder.cardView;
        final Button buttonInfo = holder.infoButton;
        textViewName.setText(dataSet.get(listPosition).getRoomID());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(dataSet.get(listPosition).getRoomID());
            }
        });
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mainPage.sendNotification(dataSet.get(listPosition).getName());
                buttonInfo.setClickable(false);
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
    /*
    public void sendOnChannel1(User user , MainPage mainPage) {
        String title = user.getName();
        Notification notification = new NotificationCompat.Builder(mainPage, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(title+" was added to the list!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }
     */
}
