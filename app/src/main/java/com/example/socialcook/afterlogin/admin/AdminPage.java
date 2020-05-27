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
import com.example.socialcook.beforelogin.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminPage extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Write a message to the database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("recipes");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_page, container, false);
        MainPage mainPage = (MainPage) getActivity();
        final ListView listView = view.findViewById(R.id.listView);
        final HashMap<String , Integer>localAmount = new HashMap<>();
        final HashMap<String , Integer>localG = new HashMap<>();
        final HashMap<String , Integer>localML = new HashMap<>();
        Button addName = (Button)view.findViewById(R.id.addButtonName);
        Button addType = (Button)view.findViewById(R.id.addButtonType);
        Button addAmount = (Button)view.findViewById(R.id.buttonAddAmount);
        Button addMl = (Button)view.findViewById(R.id.buttonAddMl);
        Button addG = (Button)view.findViewById(R.id.buttonAddMg);
        Button saveButton = (Button)view.findViewById(R.id.buttonSend);
        final EditText recipeName = (EditText)view.findViewById(R.id.recipeNameInput);
        final EditText recipeType = (EditText)view.findViewById(R.id.recipeTypeInput);
        final EditText recipeAmountKey = (EditText)view.findViewById(R.id.keyAmount);
        final EditText recipeAmountValue = (EditText)view.findViewById(R.id.valueAmount);
        final EditText recipeGKey = (EditText)view.findViewById(R.id.keyMg);
        final EditText recipeGValue = (EditText)view.findViewById(R.id.valueMg);
        final EditText recipeMlKey = (EditText)view.findViewById(R.id.keyMl);
        final EditText recipeMlValue = (EditText)view.findViewById(R.id.valueMl);
        final Recipe recipe = new Recipe();
        final ArrayList<String>arrayList;
        ArrayAdapter<String>adapter;
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getContext() , android.R.layout.simple_list_item_1 , arrayList);
        listView.setAdapter(adapter);
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
        addG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localG.put(recipeGKey.getText().toString() , Integer.parseInt(recipeGValue.getText().toString()));
                recipe.setG(localG);
            }
        });
        addMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localML.put(recipeMlKey.getText().toString() , Integer.parseInt(recipeMlValue.getText().toString()));
                recipe.setML(localML);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "recipe name: "+recipe.getRecipeName()+"\n recipe Type : "+recipe.getRecipeType()+"\nRecipeAmount : "+recipe.convertRecipeAmountIteration()+"\nRecipe ML : "+recipe.convertRecipeMLIteration()+"\nRecipe Grams : "+recipe.convertRecipeMGIteration();
                arrayList.add(result);
                myRef.setValue(recipe);
            }
        });
        return view;
    }
}