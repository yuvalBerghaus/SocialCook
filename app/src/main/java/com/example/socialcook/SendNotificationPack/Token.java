package com.example.socialcook.SendNotificationPack;
// use to get unique token for each user
public class Token {
    private String token;
    public Token(String token) {
        this.token = token;
    }
    public Token() {
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
