package com.example.socialcook.beforelogin;

import java.util.Date;

public class User {
    public String name;
    public String email;
    public String address;
    public String birthday;
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