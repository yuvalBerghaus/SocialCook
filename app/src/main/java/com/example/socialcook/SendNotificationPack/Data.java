package com.example.socialcook.SendNotificationPack;

import com.example.socialcook.afterlogin.recipeListPage.Recipe;

// use wrap two parts of notification “Title and message” ad
// an data object , Note you can send more data as more different parts with more
// variables and getter setter for those in Data class
public class Data {
    private Recipe recipe;
    private String title;
    private String message;
    public Data(String title, String message) {
        this.title = title;
        this.message = message;
    }
    public Data() {
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
