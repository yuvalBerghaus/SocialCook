package com.example.socialcook.afterlogin.userListPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialcook.R;
import com.example.socialcook.afterlogin.recipeListPage.MainPage;
import com.example.socialcook.beforelogin.User;

import java.util.ArrayList;

public class CustomAdapterUser extends RecyclerView.Adapter<CustomAdapterUser.MyViewHolder>{

    private ArrayList<User> dataSet;
    MainPage mainPage;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textViewName;
        Button infoButton;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textView2);
            this.infoButton = (Button) itemView.findViewById(R.id.buttonSendTo);
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
        Button buttonInfo = holder.infoButton;
        textViewName.setText(dataSet.get(listPosition).getName());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(dataSet.get(listPosition).getName());
            }
        });
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
