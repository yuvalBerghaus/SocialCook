package com.example.socialcook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.socialcook.afterlogin.activities.MainPage;
import com.example.socialcook.afterlogin.mainPageFrag.CustomAdapter;
import com.example.socialcook.classes.Recipe;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReceiveNotificationActivity extends AppCompatActivity {

    private static final String TAG = "DebuggingTogether";
    private static ArrayList<Recipe> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_notification);
        final DatabaseReference myRef = FireBase.recipeDir;
        final TextView categotyTv = findViewById(R.id.category);
        final TextView brandTv = findViewById(R.id.brand);

        if (getIntent().hasExtra("category")){
            final String category = getIntent().getStringExtra("category");
            String brand = getIntent().getStringExtra("brandId");
            final String recipeName = getIntent().getStringExtra("recipeName");
            String recipeType = getIntent().getStringExtra("recipeType");
            String recipe;
           categotyTv.setText(recipeType);
            brandTv.setText(recipeName);
            myRef.orderByChild("recipeName").equalTo(recipeName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot datas: dataSnapshot.getChildren()){
                        String recipeNAme=datas.child("recipeName").getValue().toString();
                        String recipeTYpe = datas.child("recipeType").getValue().toString();
                        for (DataSnapshot child: datas.child("recipeAmount").getChildren()) {
                            Log.d(TAG , child.getKey()+" = "+child.getValue());
                        }
                        for (DataSnapshot child: datas.child("recipeG").getChildren()) {
                            Log.d(TAG , child.getKey()+" = "+child.getValue());
                        }
                        for (DataSnapshot child: datas.child("recipeML").getChildren()) {
                            Log.d(TAG , child.getKey()+" = "+child.getValue());
                        }
                        Log.d(TAG , "recipe name = "+recipeNAme+"\nrecipe type = "+recipeTYpe);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        /*
        if (getIntent().hasExtra("category")){
            String category = getIntent().getStringExtra("category");
            String brand = getIntent().getStringExtra("brandId");
            String recipeName = getIntent().getStringExtra("recipeName");
            categotyTv.setText(category);
            brandTv.setText(recipeName);
        }
*/


    }
}