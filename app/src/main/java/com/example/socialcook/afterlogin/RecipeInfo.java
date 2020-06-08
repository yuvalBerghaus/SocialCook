package com.example.socialcook.afterlogin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.socialcook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

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
            Recipe obj= (Recipe) extras.getSerializable("recipe");
            TextView firstText = view.findViewById(R.id.test);
            TextView secondText = view.findViewById(R.id.textView3);
            TextView thirdText = view.findViewById(R.id.textView4);
            TextView fourthText = view.findViewById(R.id.textView5);
            firstText.setText(obj.getRecipeName());
            secondText.setText(obj.getRecipeType());
        }
        // Inflate the layout for this fragment
        return view;
    }
}