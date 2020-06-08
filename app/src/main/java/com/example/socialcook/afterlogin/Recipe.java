package com.example.socialcook.afterlogin;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Recipe implements Serializable {
    private String recipeName;
    private String recipeType;
    private Map<String , Integer> recipeAmount = new HashMap<>();
    private Map<String, Integer> recipeMl = new HashMap<>();
    private Map<String, Integer> recipeGrams = new HashMap<>();
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
        if(this.recipeMl.containsKey(recipeMlKey)) {
            this.recipeMl.put(recipeMlKey , recipeMlValue + this.recipeMl.get(recipeMlKey));
        }
        else {
            this.recipeMl.put(recipeMlKey , recipeMlValue);
        }
    }
    public void setG(String recipeGramsKey , int recipeGramsValue) {
        if(this.recipeGrams.containsKey(recipeGramsKey)) {
            this.recipeGrams.put(recipeGramsKey , recipeGramsValue + this.recipeGrams.get(recipeGramsKey));
        }
        else {
            this.recipeGrams.put(recipeGramsKey , recipeGramsValue);
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
        return recipeMl;
    }
    public Map getRecipeG() {
        return recipeGrams;
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
        mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("\n");
        return mapAsString.toString();
    }
    public String convertRecipeMLIteration() {
        Map<String,Integer> map = this.getRecipeML();
        StringBuilder mapAsString = new StringBuilder("\n");
        for (String key : map.keySet()) {
            mapAsString.append(key + " = " + map.get(key).toString() + " Mili-Liters\n ");
        }
        System.out.println("the length is "+mapAsString.length());
        mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("\n");
        return mapAsString.toString();
    }
    public void clear() {
        this.recipeAmount.clear();
        this.recipeName = null;
        this.recipeMl.clear();
        this.recipeType = null;
        this.recipeGrams.clear();
    }
}
