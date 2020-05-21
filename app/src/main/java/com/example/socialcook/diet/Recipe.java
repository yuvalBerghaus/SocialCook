package com.example.socialcook.diet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Recipe {
    private String name;
    private List<Item> items;
    private String tags="diet,sugarfree,";

    public Recipe(String name, String tags) {
        super();
        this.name = name;
        this.tags = tags;
        this.items = new ArrayList<Item>();
        items.add(new Item("Tomato","Red",3));
    }
}
