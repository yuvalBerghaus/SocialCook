package com.example.socialcook.SendNotificationPack;
// use wrap two parts of notification “Title and message” ad
// an data object , Note you can send more data as more different parts with more
// variables and getter setter for those in Data class
public class Data {
    private String Title;
    private String Message;
    public Data(String title, String message) {
        Title = title;
        Message = message;
    }
    public Data() {
    }
    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }
    public String getMessage() {
        return Message;
    }
    public void setMessage(String message) {
        Message = message;
    }
}
