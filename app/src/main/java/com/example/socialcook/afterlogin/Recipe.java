package com.example.socialcook.afterlogin;
import java.util.HashMap;
import java.util.Map;

public class Recipe {
    private String recipeName;
    private String recipeType;
    private Map<String , Integer> recipeAmount;
    private Map<String , Integer>recipeMl;
    private Map<String , Integer>recipeMg;
    public Recipe() {
    }
    public void setName(String recipeName) {
        this.recipeName = recipeName;
    }
    public void setType(String recipeType) {
        this.recipeType = recipeType;
    }
    public void setAmount(HashMap<String,Integer>amount) {
        this.recipeAmount = amount;
    }
    public void setML(HashMap<String,Integer>recipeMl) {
        this.recipeMl = recipeMl;
    }
    public void setMG(HashMap<String,Integer>recipeMg) {
        this.recipeMg = recipeMg;
    }
}
