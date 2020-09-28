package com.example.socialcook.classes;

import java.io.Serializable;

public class Room implements Serializable {
    private String roomID;

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getUid1() {
        return uid1;
    }

    public void setUid1(String uid1) {
        this.uid1 = uid1;
    }

    public String getUid2() {
        return uid2;
    }

    public void setUid2(String uid2) {
        this.uid2 = uid2;
    }

    public Boolean getReady() {
        return isReady;
    }

    public void setReady(Boolean ready) {
        isReady = ready;
    }

    Boolean isReady;

    private Recipe recipe;

    public Recipe getRecipeUid1() {
        return recipeUid1;
    }

    public void setRecipeUid1(Recipe recipeUid1) {
        this.recipeUid1 = recipeUid1;
    }

    private Recipe recipeUid1;

    public Recipe getRecipeUid2() {
        return recipeUid2;
    }

    public void setRecipeUid2(Recipe recipeUid2) {
        this.recipeUid2 = recipeUid2;
    }

    private Recipe recipeUid2;
    private String uid1;
    private String uid2;
    public Room() {
    }
}
