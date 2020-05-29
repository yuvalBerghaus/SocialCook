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
        ArrayList<DatabaseReference>a;
        //final DatabaseReference myRef = database.getReference("recipes");
        final DatabaseReference myRef = database.getReference("recipes");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_page, container, false);
        MainPage mainPage = (MainPage) getActivity();
        final ListView listView = view.findViewById(R.id.listView);
        final HashMap<String , Integer>localAmount = new HashMap<>();
        final HashMap<String , Integer>localG = new HashMap<>();
        final HashMap<String , Integer>localML = new HashMap<>();
        Button addAmount = view.findViewById(R.id.buttonAddAmount);
        Button addMl = view.findViewById(R.id.buttonAddMl);
        Button addG = view.findViewById(R.id.buttonAddMg);
        Button saveButton = view.findViewById(R.id.buttonSend);
        final EditText recipeName = view.findViewById(R.id.recipeNameInput);
        final EditText recipeType = view.findViewById(R.id.recipeTypeInput);
        final EditText recipeAmountKey = view.findViewById(R.id.keyAmount);
        final EditText recipeAmountValue = view.findViewById(R.id.valueAmount);
        final EditText recipeGKey = view.findViewById(R.id.keyMg);
        final EditText recipeGValue = view.findViewById(R.id.valueMg);
        final EditText recipeMlKey = view.findViewById(R.id.keyMl);
        final EditText recipeMlValue = view.findViewById(R.id.valueMl);
        final Recipe recipe = new Recipe();
        final ArrayList<String>arrayList;
        ArrayAdapter<String>adapter;
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_list_item_1 , arrayList);
        listView.setAdapter(adapter);
        addAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipeAmountKey.getText() != null && recipeAmountValue.getText()!= null) {
                    localAmount.put(recipeAmountKey.getText().toString() , Integer.parseInt(recipeAmountValue.getText().toString()));
                    recipe.setAmount(localAmount);
                }
                recipeAmountKey.getText().clear();
                recipeAmountValue.getText().clear();
            }
        });
        addG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipeGKey.getText().toString() != "" && recipeGValue.getText().toString() != "") {
                    localG.put(recipeGKey.getText().toString() , Integer.parseInt(recipeGValue.getText().toString()));
                    recipe.setG(localG);
                }
                recipeGKey.getText().clear();
                recipeGValue.getText().clear();
            }
        });
        addMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipeMlKey.getText() != null && recipeMlValue.getText() != null) {
                    localML.put(recipeMlKey.getText().toString() , Integer.parseInt(recipeMlValue.getText().toString()));
                    recipe.setML(localML);
                }
                recipeMlKey.getText().clear();
                recipeMlValue.getText().clear();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipe.setName(recipeName.getText().toString());
                recipe.setType(recipeType.getText().toString());
                String result = "recipe name: "+recipe.getRecipeName()+"\n recipe Type : "+recipe.getRecipeType()+"\nRecipeAmount : "+recipe.convertRecipeAmountIteration()+"\nRecipe ML : "+recipe.convertRecipeMLIteration()+"\nRecipe Grams : "+recipe.convertRecipeMGIteration();
                arrayList.add(result);
                myRef.child(recipe.getRecipeName()).setValue(recipe);
                recipeName.getText().clear();
                recipeType.getText().clear();
                recipeAmountKey.getText().clear();
                recipeAmountValue.getText().clear();
                recipeGKey.getText().clear();
                recipeGValue.getText().clear();
                recipeMlKey.getText().clear();
                recipeMlValue.getText().clear();
                recipe.clear();
            }
        });
        return view;
    }
}