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
import android.widget.Toast;

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
        //Button add for the amount - if it is empty it wont add to the object type Recipe otherwise everything will be added and a toast will confirm so
        addAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeAmountKey.getText().toString().equals("") || recipeAmountValue.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please enter item and amount!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    recipe.setAmount(recipeAmountKey.getText().toString() , Integer.parseInt(recipeAmountValue.getText().toString()));
                    Toast.makeText(getContext(), recipeAmountValue.getText().toString()+" "+recipeAmountKey.getText().toString()+" was added to the list", Toast.LENGTH_SHORT).show();
                    recipeAmountKey.getText().clear();
                    recipeAmountValue.getText().clear();
                }
            }
        });
        //Adding grams
        addG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipeGKey.getText().toString().equals("") || recipeGValue.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please enter item and Grams", Toast.LENGTH_SHORT).show();
                    return;
                }
                recipe.setG(recipeGKey.getText().toString() , Integer.parseInt(recipeGValue.getText().toString()));
                Toast.makeText(getContext(), recipeGValue.getText().toString()+" grams of "+recipeGKey.getText().toString()+" was added to the list", Toast.LENGTH_SHORT).show();
                recipeGKey.getText().clear();
                recipeGValue.getText().clear();
            }
        });
        addMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipeMlKey.getText().toString().equals("") || recipeMlValue.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please enter item and ML", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    recipe.setML(recipeMlKey.getText().toString() , Integer.parseInt(recipeMlValue.getText().toString()));
                    Toast.makeText(getContext(), recipeMlValue.getText().toString()+" ML of "+recipeMlKey.getText().toString()+" was added to the list", Toast.LENGTH_SHORT).show();
                    recipeMlKey.getText().clear();
                    recipeMlValue.getText().clear();
                }
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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
                catch (Exception NullPointerException) {
                    Toast.makeText(getContext(), "you need to fill everything!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        return view;
    }
}