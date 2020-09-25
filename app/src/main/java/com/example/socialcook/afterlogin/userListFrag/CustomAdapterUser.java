package com.example.socialcook.afterlogin.userListFrag;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.socialcook.R;
import com.example.socialcook.afterlogin.activities.MainPage;
import com.example.socialcook.classes.Log;
import com.example.socialcook.classes.Recipe;
import com.example.socialcook.classes.User;
import com.example.socialcook.firebase.FireBase;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;

public class CustomAdapterUser extends RecyclerView.Adapter<CustomAdapterUser.MyViewHolder>{

    private ArrayList<User> dataSet;
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    MainPage mainPage;
    Recipe chosenRecipe;
    private NotificationManagerCompat notificationManager;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textViewName;
        ImageView imageView;
        Button infoButton;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemName);
            this.infoButton = (Button) itemView.findViewById(R.id.buttonSelectRoom);
            this.imageView = (ImageView) itemView.findViewById(R.id.userImage);
        }


    }

    public CustomAdapterUser(ArrayList<User> data , MainPage mainPage , Recipe chosenRecipe) {
        this.chosenRecipe = chosenRecipe;
        this.dataSet = data;
        this.mainPage = mainPage;
        notificationManager = NotificationManagerCompat.from(mainPage);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_userlist_layout, parent, false);

        view.setOnTouchListener(UsersListFrag.myOnClickListener);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
// Load the image using Glide
        TextView textViewName = holder.textViewName;
       final ImageView profilePhoto = holder.imageView;
       String imagePath = dataSet.get(listPosition).getImagePath();
       if (imagePath != null)
       {
           FireBase.storageRef.child(imagePath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
               @Override
               public void onSuccess(Uri uri) {
                   // Got the download URL for 'users/me/profile.png'
                   try {
                       System.out.println(uri);
                       Picasso.get().load(uri).into(profilePhoto);
                   }
                   catch (IllegalArgumentException error) {
                       System.out.println("FUCK");
                   }
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception exception) {
                   // Handle any errors
               }
           });
       }
        CardView cardView = holder.cardView;
        final Button buttonInfo = holder.infoButton;
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
                //mainPage.sendNotification(dataSet.get(listPosition).getName());
                buttonInfo.setClickable(false);
                buttonInfo.setAlpha(0.5f);
                mainPage.sendNotificationUID(FireBase.getAuth().getCurrentUser().getDisplayName(), dataSet.get(listPosition).getUID() , chosenRecipe , FireBase.getAuth().getCurrentUser().getUid());
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
