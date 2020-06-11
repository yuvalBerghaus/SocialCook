package com.example.socialcook.afterlogin.mainPageFrag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialcook.R;
import com.example.socialcook.afterlogin.activities.MainPage;
import com.example.socialcook.classes.Recipe;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private ArrayList<Recipe> dataSet;
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

    public CustomAdapter(ArrayList<Recipe> data , MainPage mainPage) {
        this.dataSet = data;
        this.mainPage = mainPage;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnTouchListener(MainPageFrag.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        CardView cardView = holder.cardView;
        Button buttonInfo = holder.infoButton;
        textViewName.setText(dataSet.get(listPosition).getRecipeName());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(dataSet.get(listPosition).getRecipeName());
                mainPage.loadRecipePage(dataSet.get(listPosition));
            }
        });
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPage.loadRecipePage(dataSet.get(listPosition));
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
