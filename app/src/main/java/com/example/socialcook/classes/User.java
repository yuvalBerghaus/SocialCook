package com.example.socialcook.classes;

import java.util.Date;

public class User {
    public String name;
    public String email;
    public String address;
    public String birthday;
    public String uid;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String token;
    public String getUID() {
        return uid;
    }
    public void setUID(String uid) {
        this.uid = uid;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getName() {
        return this.name;
    }
    public String getEmail() {
        return this.email;
    }
    public String getAddress() {
        return this.address;
    }

}