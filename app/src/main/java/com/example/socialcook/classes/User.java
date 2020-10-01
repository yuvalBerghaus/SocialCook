package com.example.socialcook.classes;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {
    public String name;
    public String email;
    public String imagePath;
    public String address;
    public String birthday;
    public String country;
    public String uid;
    public String description;

    public String token;
    public String getUID() {
        return uid;
    }
    public void setUID(String uid) {
        this.uid = uid;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
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
    public void setCountry(String country) { this.country = country; }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public void setDescription(String description) { this.description = description; }
    public String getName() {
        return this.name;
    }
    public String getEmail() {
        return this.email;
    }
    public String getAddress() {
        return this.address;
    }
    public String getCountry() { return country; }
    public String getImagePath() {
        return imagePath;
    }
    public String getDescription() { return description; }

}