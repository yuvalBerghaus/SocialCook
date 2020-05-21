package com.example.socialcook.diet;
public class Item {
    private String name;
    private String description;
    private float amount;
    public Item(String name, String description, float amount) {
        super();
        this.name = name;
        this.description = description;
        this.amount = amount;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }



}
