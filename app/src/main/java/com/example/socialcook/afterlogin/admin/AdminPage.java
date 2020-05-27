package com.example.socialcook.afterlogin.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.socialcook.R;
import com.example.socialcook.afterlogin.MainPage;


public class AdminPage extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button addAmount = (Button)getView().findViewById(R.id.buttonAddAmount);
        Button addMl = (Button)getView().findViewById(R.id.buttonAddMl);
        Button addMg = (Button)getView().findViewById(R.id.buttonAddMg);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_page, container, false);
    }
}