package com.example.socialcook.afterlogin;
import java.util.HashMap;
import java.util.Map;

public class Recipe {
    private String recipeName;
    private String recipeType;
    private Map<String , Integer> recipeAmount;
    private Map<String , Float>recipeMl;
    private Map<String , Float>recipeMg;
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
    public void setML(HashMap<String,Float>recipeMl) {
        this.recipeMl = recipeMl;
    }
    public void setMG(HashMap<String,Float>recipeMg) {
        this.recipeMg = recipeMg;
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
    public Map getRecipeMG() {
        return recipeMg;
    }
    public String convertRecipeAmountIteration() {
        Map<String,Integer> map = this.getRecipeAmount();
        StringBuilder mapAsString = new StringBuilder("{");
        for (String key : map.keySet()) {
            mapAsString.append(key + "=" + map.get(key).toString() + ", ");
        }
        mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");
        return mapAsString.toString();
    }
    public String convertRecipeMGIteration() {
        Map<String,Integer> map = this.getRecipeMG();
        StringBuilder mapAsString = new StringBuilder("{");
        for (String key : map.keySet()) {
            mapAsString.append(key + "=" + map.get(key).toString() + ", ");
        }
        mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");
        return mapAsString.toString();
    }
    public String convertRecipeMLIteration() {
        Map<String,Integer> map = this.getRecipeML();
        StringBuilder mapAsString = new StringBuilder("{");
        for (String key : map.keySet()) {
            mapAsString.append(key + "=" + map.get(key).toString() + ", ");
        }
        mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");
        return mapAsString.toString();
    }
}
