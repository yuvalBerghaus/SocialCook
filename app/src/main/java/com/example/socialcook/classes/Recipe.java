package com.example.socialcook.classes;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Recipe implements Serializable {
    private static final String TAG = "<<< TESTING >>>";
    private String recipeName;
    private String recipeType;

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    private String recipeDescription;
    private Map<String , Integer> recipeAmount = new HashMap<>();

    private Map<String, Integer> recipeML = new HashMap<>();
    private Map<String, Integer> recipeG = new HashMap<>();
    public Recipe() {
    }
    public void setName(String recipeName) {
        this.recipeName = recipeName;
    }
    public void setType(String recipeType) {
        this.recipeType = recipeType;
    }
    public void setAmount(String recipeAmountKey , int recipeAmountValue) {
        if(this.recipeAmount.containsKey(recipeAmountKey)) {
            this.recipeAmount.put(recipeAmountKey , recipeAmountValue + this.recipeAmount.get(recipeAmountKey));
        }
        else {
            this.recipeAmount.put(recipeAmountKey , recipeAmountValue);
        }
    }
    public void setML(String recipeMlKey , int recipeMlValue) {
        if(this.recipeML.containsKey(recipeMlKey)) {
            this.recipeML.put(recipeMlKey , recipeMlValue + this.recipeML.get(recipeMlKey));
        }
        else {
            this.recipeML.put(recipeMlKey , recipeMlValue);
        }
    }
    public void setG(String recipeGramsKey , int recipeGramsValue) {
        if(this.recipeG.containsKey(recipeGramsKey)) {
            this.recipeG.put(recipeGramsKey , recipeGramsValue + this.recipeG.get(recipeGramsKey));
        }
        else {
            this.recipeG.put(recipeGramsKey , recipeGramsValue);
        }
    }
    public String getRecipeName() {
        return recipeName;
    }
    public String getRecipeType() {
        return recipeType;
    }
    public Map getRecipeAmount() {
        return recipeAmount;
    }
    public Map getRecipeML() {
        return recipeML;
    }
    public Map getRecipeG() {
        return recipeG;
    }
    public String convertRecipeAmountIteration() {
        Map<String,Integer> map = this.getRecipeAmount();
        StringBuilder mapAsString = new StringBuilder("\n");
        for (String key : map.keySet()) {
            mapAsString.append(key + " = " + map.get(key).toString() + " Units\n ");
        }
        mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("\n");
        return mapAsString.toString();
    }
    public String convertRecipeGIteration() {
        Map<String,Integer> map = this.getRecipeG();
        StringBuilder mapAsString = new StringBuilder("\n");
        for (String key : map.keySet()) {
            mapAsString.append(key + " = " + map.get(key).toString() + " grams\n ");
        }
        //mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("\n");
        return mapAsString.toString();
    }
    public String convertRecipeMLIteration() {
        Log.d(TAG, "convertRecipeMLIteration 1: called");
        Map<String,Integer> map = this.getRecipeML();
        Log.d(TAG, "convertRecipeMLIteration 2: "+map.keySet().toString());
        StringBuilder mapAsString = new StringBuilder("\n");
        for (String key : map.keySet()) {
            Log.d(TAG, "convertRecipeMLIteration 3: key = "+key);
            Log.d(TAG, "convertRecipeMLIteration 4: map.get =  "+map.get(key).toString());
            mapAsString.append(key + " = " + map.get(key).toString() + " Mili-Liters\n ");
        }
        System.out.println("the length is "+mapAsString.length());
        //mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("\n");
        Log.d(TAG, mapAsString.toString());
        return mapAsString.toString();
    }
    public void clear() {
        this.recipeAmount.clear();
        this.recipeName = null;
        this.recipeML.clear();
        this.recipeType = null;
        this.recipeG.clear();
    }
}
