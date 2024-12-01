package catbreedersystem;

import java.util.ArrayList;
import java.util.List;

public class Cat {
    protected String catID;
    protected String name;
    protected String birth;
    protected String gender;
    protected String color;
    private int price;
    protected boolean availability;
    static final List<Cat> cats = new ArrayList<>();

    public Cat(String catID, String name, String birth, String gender, String color, int price, boolean availability) {
        if (!isUniqueCatID(catID)) {
            throw new IllegalArgumentException("Duplicate catID found.");
        }
        this.catID = catID;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.color = color;
        this.price = price;
        this.availability = availability;
        cats.add(this);
    }

    public void updateCat(int newPrice, boolean availability, String newName, String newColor, String birth, String newGender) {
        this.price = newPrice;
        this.availability = availability;
        this.name = newName;
        this.color = newColor;
        this.birth = birth;
        this.gender = newGender;
    }

    private boolean isUniqueCatID(String id) {
        return cats.stream().noneMatch(cat -> cat.catID.equals(id));
    }

    @Override
    public String toString() {
        return "Cat{catID=" + catID + ", name=" + name + ", birth=" + birth + ", gender=" + gender + 
               ", color=" + color + ", price=" + price + ", availability=" + availability + "}";
    }

    public String getCatID() {
        return this.catID;
    }

    public Object getName() {
        return this.name;
    }

    public String getBirthdate() {
        return this.birth;
    }

    public String getGender() {
        return this.gender;
    }

    public String getColor() {
        return this.color;
    }

    public int getPrice() {
        return this.price;
    }
}