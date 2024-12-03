package catbreedersystem;

import java.util.ArrayList;
import java.util.List;

public class Cat {
    protected String catID;
    protected String name;
    protected String birthdate;
    protected String gender;
    protected String color;
    private int price;
    protected boolean availability;
    static final List<Cat> cats = new ArrayList<>();

    public Cat(String catID, String name, String birth, String gender, String color, int price, boolean availability) {
        this.catID = catID;
        this.name = name;
        this.birthdate = birth;
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
        this.birthdate = birth;
        this.gender = newGender;
    }

    @Override
    public String toString() {
        return "Cat{catID=" + catID + ", name=" + name + ", birthdate=" + birthdate + ", gender=" + gender + 
               ", color=" + color + ", price=" + price + ", availability=" + availability + "}";
    }

    public String getCatID() {
        return this.catID;
    }

    public String getName() {
        return this.name;
    }

    public String getBirthdate() {
        return this.birthdate;
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

    public boolean isAvailability() {
        return this.availability;
    }
}