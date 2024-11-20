package com.example;

import java.util.*;

public class Cat {
    protected String catID;
    protected String name;
    protected int age;
    protected String gender;
    protected String color;
    private int price;
    protected boolean availability;
    private static final List<Cat> cats = new ArrayList<>();

    public Cat(String catID, String name, int age, String gender, String color, int price, boolean availability) {
        if (!isUniqueCatID(catID)) {
            throw new IllegalArgumentException("Duplicate catID found.");
        }
        this.catID = catID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.color = color;
        this.price = price;
        this.availability = availability;
        cats.add(this);
    }

    public void updateCat(int newPrice, boolean availability, String newName, String newColor, int newAge, String newGender) {
        this.price = newPrice;
        this.availability = availability;
        this.name = newName;
        this.color = newColor;
        this.age = newAge;
        this.gender = newGender;
    }

    private boolean isUniqueCatID(String id) {
        return cats.stream().noneMatch(cat -> cat.catID.equals(id));
    }

    @Override
    public String toString() {
        return "Cat{catID=" + catID + ", name=" + name + ", age=" + age + ", gender=" + gender + 
               ", color=" + color + ", price=" + price + ", availability=" + availability + "}";
    }
}
