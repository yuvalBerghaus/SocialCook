package com.example.socialcook.afterlogin.userListPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialcook.R;
import com.example.socialcook.SendNotificationPack.APIService;
import com.example.socialcook.SendNotificationPack.Client;
import com.example.socialcook.SendNotificationPack.Data;
import com.example.socialcook.SendNotificationPack.MyResponse;
import com.example.socialcook.SendNotificationPack.NotificationSender;
import com.example.socialcook.afterlogin.recipeListPage.MainPage;
import com.example.socialcook.beforelogin.User;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomAdapterUser extends RecyclerView.Adapter<CustomAdapterUser.MyViewHolder>{

    private static APIService apiService;
    private ArrayList<User> dataSet;
    private static MainPage mainPage;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textViewName;
        Button sendToButon;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textView2);
            this.sendToButon = (Button) itemView.findViewById(R.id.buttonSendTo);
        }


    }

    public CustomAdapterUser(ArrayList<User> data , MainPage mainPage) {
        this.dataSet = data;
        this.mainPage = mainPage;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_userlist_layout, parent, false);

        view.setOnTouchListener(UsersListFrag.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        CardView cardView = holder.cardView;
        Button sendButton = holder.sendToButon;
        textViewName.setText(dataSet.get(listPosition).getName());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(dataSet.get(listPosition).getName());
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("the send button has been complete");
                FirebaseDatabase.getInstance().getReference().child("users").child(dataSet.get(listPosition).getUID()).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String usertoken=dataSnapshot.getValue(String.class);
                        sendNotifications(usertoken, "you got an invitation","hello my friend!");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
    public static void sendNotifications(String usertoken, String title, String message) {
        System.out.println("send notification is "+usertoken);
        final Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService = Client.getClient("https://fcm.googleapis.com").create(APIService.class);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(mainPage, "Failed ", Toast.LENGTH_LONG);
                        System.out.println("Failed because response was "+response.body().success);
                    }
                    else {
                        System.out.println("THE RESPONSE WORKED AND IT IS "+ response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
