package com.example.socialcook.afterlogin.activities;
import static com.example.socialcook.afterlogin.NotificationApp.CHANNEL_1_ID;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.socialcook.R;
import com.example.socialcook.afterlogin.adminFrag.AdminPage;
import com.example.socialcook.afterlogin.mainPageFrag.MainPageFrag;
import com.example.socialcook.afterlogin.recipeInfoFrag.RecipeInfo;
import com.example.socialcook.afterlogin.userListFrag.UsersListFrag;
import com.example.socialcook.classes.Recipe;
import com.example.socialcook.beforelogin.MainActivity;
import com.example.socialcook.firebase.FireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainPage extends AppCompatActivity implements FireBase.IMainPage {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private NotificationManagerCompat notificationManager;
    private RequestQueue mRequestQue;
    private String URL = FireBase.POST;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        notificationManager = NotificationManagerCompat.from(this);
        mRequestQue = Volley.newRequestQueue(this);
        FireBase.firebaseMessaging.subscribeToTopic("news");
        FireBase.firebaseMessaging.subscribeToTopic(FireBase.getAuth().getUid().toString());
        mAuth = FireBase.getAuth();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
///////////////////////////////////////////////////////////////////////////////////////////////////
            if (findViewById(R.id.fragment_mainPage) != null) {
                if (savedInstanceState != null) {
                    return;
                }
                // Create a new Fragment to be placed in the activity layout
                Fragment firstFragment = new MainPageFrag();
                Bundle args = new Bundle();
                args.putString("email", email);
                firstFragment.setArguments(args);
                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                    /*
                    firstFragment.setArguments(getIntent().getExtras());
                    */
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_mainPage, firstFragment).commit();
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        FireBase.usersDir.child(FireBase.getAuth().getUid()).child("token").setValue(task.getResult().getToken());
                    }
                });
            }
        } else {
            Toast.makeText(MainPage.this, "User not logged In",
                    Toast.LENGTH_SHORT).show();
        }
        FireBase.updateToken();
    }   //:)
    public void loadAdminPage() {
        // Create fragment and give it an argument specifying the article it should show
        Fragment newFragment = new AdminPage();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_mainPage, newFragment);
        transaction.addToBackStack(null);
// Commit the transaction
        transaction.commit();
    }
    public void loadRecipePage(Recipe recipe) {
        // Create fragment and give it an argument specifying the article it should show
        Fragment newFragment = new RecipeInfo();
        Bundle bundle = new Bundle();
        bundle.putSerializable("recipe" , recipe);
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_mainPage, newFragment);
        transaction.addToBackStack(null);
// Commit the transaction
        transaction.commit();
    }
    public void loadUsersPage(Recipe recipe) {
        // Create fragment and give it an argument specifying the article it should show
        Fragment newFragment = new UsersListFrag();
        Bundle bundle = new Bundle();
        bundle.putSerializable("recipe" , recipe);
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_mainPage, newFragment);
        transaction.addToBackStack(null);
// Commit the transaction
        transaction.commit();
    }

    @Override
    public void signOut() {
        mAuth.signOut();
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    public void sendOnChannel1(String Uid) {
        String title = "hello";
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(title+" was added to the list!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }
    public void sendNotificationUID(String name , String uid) {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+uid);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","New request!");
            notificationObj.put("body",name+" just sent a notification to you!");

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");



            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("MUR", "onResponse: ");
                            Toast.makeText(MainPage.this, "The message was sent successfully",
                                    Toast.LENGTH_SHORT).show();
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
}