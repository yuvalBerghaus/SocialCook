package com.example.socialcook.afterlogin.userListFrag;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialcook.R;
import com.example.socialcook.afterlogin.activities.MainPage;
import com.example.socialcook.classes.Recipe;
import com.example.socialcook.classes.User;
import com.example.socialcook.firebase.FireBase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
        ProgressBar progressBar;
        Button infoButton;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.textViewName = (TextView) itemView.findViewById(R.id.itemName);
            this.infoButton = (Button) itemView.findViewById(R.id.buttonSelectRoom);
            this.imageView = (ImageView) itemView.findViewById(R.id.userImage);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar2);
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextView textViewName = holder.textViewName;
        final ImageView profilePhoto = holder.imageView;
        final ProgressBar progressBar = holder.progressBar;

        if(dataSet.get(listPosition).getUID() != user.getUid()) {

            String imagePath = dataSet.get(listPosition).getImagePath();
            if (imagePath != null)
            {
                FireBase.storageRef.child(imagePath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        // Load the image using Glide
                        Glide
                                .with(holder.cardView.getContext())
                                .load(uri)
                                .centerCrop()
                                .placeholder(progressBar.getProgressDrawable())
                                .into(profilePhoto);
                    /*
                    try {
                         System.out.println(uri);
                        Picasso.get().load(uri).into(profilePhoto, new Callback() {
                            @Override
                            public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }
                    catch (IllegalArgumentException error) {
                        System.out.println("FUCK");
                    }*/
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

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainPage.loadUserInfoPage(dataSet.get(listPosition));
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
