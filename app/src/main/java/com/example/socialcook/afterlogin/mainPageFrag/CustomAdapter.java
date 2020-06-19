package com.example.socialcook.afterlogin.mainPageFrag;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialcook.R;
import com.example.socialcook.afterlogin.activities.MainPage;
import com.example.socialcook.classes.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private static final String TAG = "imageURL";
    private ArrayList<Recipe> dataSet;
    MainPage mainPage;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        String url;
        TextView textViewName;
        ImageView imageURL;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageURL = (ImageView) itemView.findViewById(R.id.imageID);
            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemName);
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
        ImageView image = holder.imageURL;
        String url =holder.url;
        Picasso.get().load(dataSet.get(listPosition).getImageUrl()).into(image);
        textViewName.setText(dataSet.get(listPosition).getRecipeName());
        cardView.setOnClickListener(new View.OnClickListener() {
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
