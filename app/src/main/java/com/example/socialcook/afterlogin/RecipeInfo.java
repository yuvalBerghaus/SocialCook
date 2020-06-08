package com.example.socialcook.afterlogin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.socialcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RecipeInfo extends Fragment {
    private static final String TAG = "<<< TESTING >>>";
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_info, container, false);
        MainPage mainPage = (MainPage)getActivity();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Bundle extras = this.getArguments();
            Recipe currentRecipe= (Recipe) extras.getSerializable("recipe");
            Log.d(TAG, "1: "+currentRecipe.getRecipeAmount().keySet().size());
            TextView recipeInfoView = view.findViewById(R.id.recipeInfo);
            //TextView recipeIngrediantsView = view.findViewById(R.id.recipeIngrediants);
            recipeInfoView.setText(currentRecipe.getRecipeName() + "\n\n" + currentRecipe.getRecipeType() + "\n\n"+currentRecipe.convertRecipeAmountIteration()+"\n\n"+currentRecipe.convertRecipeMLIteration()+"\n\n"+currentRecipe.convertRecipeGIteration());
            //System.out.println(currentRecipe.convertRecipeMLIteration());
            //recipeIngrediantsView.setText(currentRecipe.convertRecipeMLIteration());
        }
        // Inflate the layout for this fragment
        return view;
    }
}