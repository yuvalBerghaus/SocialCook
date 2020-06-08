package com.example.socialcook.afterlogin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.socialcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RecipeInfo extends Fragment {
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
            TextView recipeInfoView = view.findViewById(R.id.recipeInfo);
            //TextView recipeIngrediantsView = view.findViewById(R.id.recipeIngrediants);
            recipeInfoView.setText(currentRecipe.getRecipeName() + "\n\n" + currentRecipe.getRecipeType() + "\n\nIngrediants:\n"+currentRecipe.convertRecipeGIteration());
            //System.out.println(currentRecipe.convertRecipeMLIteration());
            //recipeIngrediantsView.setText(currentRecipe.convertRecipeMLIteration());
            /*
            TextView recipeTypeView = view.findViewById(R.id.recipeType);
            TextView recipeAmountView = view.findViewById(R.id.recipeAmount);
            TextView recipeGView = view.findViewById(R.id.recipeG);
            TextView recipeMlView = view.findViewById(R.id.recipeMl);
            recipeNameView.setText(currentRecipe.getRecipeName());
            System.out.println(currentRecipe.convertRecipeAmountIteration());
            recipeTypeView.setText(currentRecipe.getRecipeType());
            recipeAmountView.setText(currentRecipe.convertRecipeAmountIteration());
            recipeGView.setText(currentRecipe.convertRecipeMGIteration());
            recipeMlView.setText(currentRecipe.convertRecipeMLIteration());
            */
        }
        // Inflate the layout for this fragment
        return view;
    }
}