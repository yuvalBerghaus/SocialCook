package com.example.socialcook;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.socialcook.classes.Recipe;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReceiveNotificationActivity extends AppCompatActivity {

    private static final String TAG = "DebuggingTogether";
    private static ArrayList<Recipe> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_notification);
        final DatabaseReference myRef = FireBase.recipeDir;
        final TextView recipeNameView = findViewById(R.id.recipeNameView);
        final TextView recipeDescription = findViewById(R.id.description);
        final TextView recipeTypeView = findViewById(R.id.brand);
        final TextView recipeItems = findViewById(R.id.items);
        if (getIntent().hasExtra("category")){
            final String category = getIntent().getStringExtra("category");
            String brand = getIntent().getStringExtra("brandId");
            final String recipeName = getIntent().getStringExtra("recipeName");
            String recipeType = getIntent().getStringExtra("recipeType");
           recipeNameView.setText(recipeType);
            recipeTypeView.setText(recipeName);
            myRef.orderByChild("recipeName").equalTo(recipeName).addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot datas: dataSnapshot.getChildren()){
                        String recipeNAme=datas.child("recipeName").getValue().toString();
                        String recipeTYpe = datas.child("recipeType").getValue().toString();
                        String recipeMethod = datas.child("recipeDescription").getValue().toString();
                        recipeNameView.setText(recipeNAme);
                        recipeTypeView.setText(recipeTYpe);
                        recipeDescription.setText(recipeMethod);
                        String recipeItemsAll = "";
                        for (DataSnapshot child: datas.child("recipeAmount").getChildren()) {
                            Log.d(TAG , child.getKey()+" we need AMOUNT "+child.getValue());
                            recipeItemsAll += (child.getKey() != null)?child.getValue().toString()+" "+child.getKey()+"\n":"";
                        }
                        for (DataSnapshot child: datas.child("recipeG").getChildren()) {
                            Log.d(TAG , child.getKey()+" = "+child.getValue());
                            recipeItemsAll += (child.getKey() != null)?"\n"+child.getValue().toString()+" grams of "+child.getKey():"";
                        }
                        for (DataSnapshot child: datas.child("recipeML").getChildren()) {
                            Log.d(TAG , child.getKey()+" we need ML "+child.getValue());
                            recipeItemsAll += (child.getKey() != null)?"\n"+child.getValue().toString()+" ML of "+child.getKey():"";
                        }
                        recipeItems.setText(recipeItemsAll);
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