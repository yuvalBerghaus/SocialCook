package com.example.socialcook.afterlogin.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.socialcook.R;
import com.example.socialcook.afterlogin.MainPage;
import com.example.socialcook.afterlogin.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.List;


public class AdminPage extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_page, container, false);
        final ListView listView = view.findViewById(R.id.listView);
        final HashMap<String , Integer>localAmount = new HashMap<>();
        final HashMap<String , Float>localMG = new HashMap<>();
        final HashMap<String , Float>localML = new HashMap<>();
        Button addName = (Button)view.findViewById(R.id.addButtonName);
        Button addType = (Button)view.findViewById(R.id.addButtonType);
        Button addAmount = (Button)view.findViewById(R.id.buttonAddAmount);
        Button addMl = (Button)view.findViewById(R.id.buttonAddMl);
        Button addMg = (Button)view.findViewById(R.id.buttonAddMg);
        final EditText recipeName = (EditText)view.findViewById(R.id.recipeNameInput);
        final EditText recipeType = (EditText)view.findViewById(R.id.recipeTypeInput);
        final EditText recipeAmountKey = (EditText)view.findViewById(R.id.keyAmount);
        final EditText recipeAmountValue = (EditText)view.findViewById(R.id.valueAmount);
        final EditText recipeMgKey = (EditText)view.findViewById(R.id.keyMg);
        final EditText recipeMgValue = (EditText)view.findViewById(R.id.valueMg);
        final EditText recipeMlKey = (EditText)view.findViewById(R.id.keyMl);
        final EditText recipeMlValue = (EditText)view.findViewById(R.id.valueMl);
        ArrayAdapter<Recipe> viewRecipe;
        final Recipe recipe = new Recipe();
        addName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipe.setName(recipeName.getText().toString());
            }
        });
        addType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipe.setType(recipeType.getText().toString());
            }
        });
        addAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localAmount.put(recipeAmountKey.getText().toString() , Integer.parseInt(recipeAmountValue.getText().toString()));
                recipe.setAmount(localAmount);
            }
        });
        addMg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localMG.put(recipeMgKey.getText().toString() , Float.valueOf(recipeMgValue.getText().toString().trim()).floatValue());
                recipe.setMG(localMG);
            }
        });
        addMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localML.put(recipeMlKey.getText().toString() , Float.valueOf(recipeMlValue.getText().toString().trim()).floatValue());
                recipe.setML(localML);
            }
        });
        return view;
    }
}