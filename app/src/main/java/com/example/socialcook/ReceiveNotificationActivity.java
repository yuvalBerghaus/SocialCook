package com.example.socialcook;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.socialcook.afterlogin.activities.MainPage;
import com.example.socialcook.classes.Recipe;
import com.example.socialcook.firebase.FireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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
        final Button accept = findViewById(R.id.acceptButton);
        final DatabaseReference myRef = FireBase.recipeDir;
        final TextView recipeNameView = findViewById(R.id.recipeNameView);
        final RequestQueue mRequestQue;
        final ImageView recipeImageView = findViewById(R.id.imageRecieve);
        final TextView recipeDescription = findViewById(R.id.description);
        final TextView recipeTypeView = findViewById(R.id.brand);
        final TextView recipeItems = findViewById(R.id.items);
        final String uidUser = getIntent().getStringExtra("uidSource");
        final String category = getIntent().getStringExtra("category");
        String brand = getIntent().getStringExtra("brandId");
        final String recipeName = getIntent().getStringExtra("recipeName");
        String recipeType = getIntent().getStringExtra("recipeType");
        if (getIntent().hasExtra("category")){
            mRequestQue = Volley.newRequestQueue(this);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("to","/topics/"+uidUser);
                        JSONObject notificationObj = new JSONObject();
                        notificationObj.put("title","Your request has been accepted!");
                        notificationObj.put("body",FireBase.getAuth().getCurrentUser().getDisplayName()+" just accepted your request");
                        JSONObject extraData = new JSONObject();
                        json.put("notification",notificationObj);
                        json.put("data",extraData);
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, FireBase.POST,
                                json,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("MUR", "onResponse: ");
                                        Toast.makeText(getApplicationContext(), "The message was sent successfully",
                                                Toast.LENGTH_SHORT).show();
                                        final Recipe recipe = new Recipe();
                                        myRef.orderByChild("recipeName").equalTo(recipeName).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot datas: dataSnapshot.getChildren()){
                                                    String recipeNAme=datas.child("recipeName").getValue().toString();
                                                    String recipeTYpe = datas.child("recipeType").getValue().toString();
                                                    String recipeMethod = datas.child("recipeDescription").getValue().toString();
                                                    String imageUrl = datas.child("imageUrl").getValue().toString();
                                                    Picasso.get().load(imageUrl).into(recipeImageView);
                                                    for (DataSnapshot child: datas.child("recipeAmount").getChildren()) {
                                                        recipe.setALLRecipeAmount(child.getKey());
                                                    }
                                                    for (DataSnapshot child: datas.child("recipeG").getChildren()) {
                                                        recipe.setALLRecipeGrams(child.getKey());
                                                    }
                                                    for (DataSnapshot child: datas.child("recipeML").getChildren()) {
                                                        recipe.setAllRecipeML(child.getKey());
                                                    }
                                                    Log.d(TAG , "recipe name = "+recipeNAme+"\nrecipe type = "+recipeTYpe);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        finish();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("MUR", "onError: "+error.networkResponse);
                            }
                        }
                        ){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String,String> header = new HashMap<>();
                                header.put("content-type","application/json");
                                header.put("authorization","key=AAAA8W53yXc:APA91bHXoE3oXyuCG1GixUIOoy9A8M3EiBbXIF5QH-nrHRTTgZ8l-RSExlX4ALFVnFWFXfGg7YWKZZzPQ9IR_kxksiLDguhRoTBmUfEHGC6qD1UfBTAMalL3WU-MCarVxh36EDCTNG3u");
                                return header;
                            }
                        };
                        mRequestQue.add(request);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
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
                        String imageUrl = datas.child("imageUrl").getValue().toString();
                        Picasso.get().load(imageUrl).into(recipeImageView);
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