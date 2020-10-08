package com.example.socialcook.afterlogin.recipeInfoFrag;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.socialcook.R;
import com.example.socialcook.afterlogin.activities.MainPage;
import com.example.socialcook.classes.OnSwipeTouchListener;
import com.example.socialcook.classes.Recipe;
import com.example.socialcook.firebase.FireBase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class RecipeInfo extends Fragment {
    private static final String TAG = "<<< TESTING >>>";
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_info, container, false);
        final MainPage mainPage = (MainPage)getActivity();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Bundle extras = this.getArguments();
            final Recipe currentRecipe= (Recipe) extras.getSerializable("recipe");
            TextView recipeInfoView = view.findViewById(R.id.recipeInfo);
            final ImageView recipeImage = view.findViewById(R.id.recipeInfoImage);
            if (currentRecipe.getImageUrl().startsWith("images/")) {
                FireBase.storageRef.child(currentRecipe.getImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide
                                .with(Objects.requireNonNull(getContext()))
                                .load(uri)
                                .centerCrop()
                                //    .placeholder(progressBar.getProgressDrawable())
                                .into(recipeImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
            else {
                Glide
                        .with(getContext())
                        .load(currentRecipe.getImageUrl())
                        .centerCrop()
                        //    .placeholder(progressBar.getProgressDrawable())
                        .into(recipeImage);
            }
            Glide
                    .with(getContext())
                    .load(currentRecipe.getImageUrl())
                    .centerCrop()
                //    .placeholder(progressBar.getProgressDrawable())
                    .into(recipeImage);
//            Picasso.get().load(currentRecipe.getImageUrl()).into(recipeImage);
            //TextView recipeIngrediantsView = view.findViewById(R.id.recipeIngrediants);
            recipeInfoView.setText("Recipe name : "+currentRecipe.getRecipeName() + "\n\nRecipe type : " + currentRecipe.getRecipeType() + "\n\nRequirements\n"+currentRecipe.convertRecipeAmountIteration()+""+currentRecipe.convertRecipeMLIteration()+""+currentRecipe.convertRecipeGIteration()+"\nDescription\n"+currentRecipe.getRecipeDescription());
            //System.out.println(currentRecipe.convertRecipeMLIteration());
            //recipeIngrediantsView.setText(currentRecipe.convertRecipeMLIteration());
            Button nextButton = view.findViewById(R.id.nextButton);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainPage.loadUsersPage(currentRecipe);
                }
            });
            view.setOnTouchListener(new OnSwipeTouchListener(mainPage) {

                @Override
                public void onSwipeRight() {
                    super.onSwipeLeft();
                    mainPage.loadMainPage();
                }
                @Override
                public void onSwipeLeft() {
                    super.onSwipeLeft();
                    mainPage.loadUsersPage(currentRecipe);
                }
            });
        }
        // Inflate the layout for this fragment
        return view;
    }
}