package com.example.socialcook.afterlogin.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.socialcook.R;
import com.example.socialcook.afterlogin.MainPage;
import com.example.socialcook.afterlogin.Recipe;

import java.util.HashMap;
import java.util.List;


public class AdminPage extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_page, container, false);
        final ListView listView = view.findViewById(R.id.listView);
        final HashMap<String , Integer>localAmount = new HashMap<>();
        final HashMap<String , Integer>localMG = new HashMap<>();
        final HashMap<String , Integer>localML = new HashMap<>();
        Button addAmount = (Button)view.findViewById(R.id.buttonAddAmount);
        Button addMl = (Button)view.findViewById(R.id.buttonAddMl);
        Button addMg = (Button)view.findViewById(R.id.buttonAddMg);
        EditText recipeName = (EditText)view.findViewById(R.id.recipeNameInput);
        EditText recipeType = (EditText)view.findViewById(R.id.recipeTypeInput);
        final EditText recipeAmountKey = (EditText)view.findViewById(R.id.keyAmount);
        final EditText recipeAmountValue = (EditText)view.findViewById(R.id.valueAmount);
        final EditText recipeMgKey = (EditText)view.findViewById(R.id.keyMg);
        final EditText recipeMgValue = (EditText)view.findViewById(R.id.valueMg);

        final Recipe recipe = new Recipe();
        addAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localAmount.put(recipeAmountKey.getText().toString() , Integer.parseInt(String.valueOf(recipeAmountValue)));
                recipe.setAmount(localAmount);
            }
        });
        addMg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localMG.put(recipeMgKey.getText().toString() , Integer.parseInt(String.valueOf(recipeMgValue)));
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}