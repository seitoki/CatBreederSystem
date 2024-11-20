package com.example;

import java.util.*;

public class Admin extends User {

    public Admin(String id, String name, String email, String phone) {
        super(id, name, email, phone, "Admin");
    }

    @Override
    public void viewProfile() {
        System.out.println("Admin Profile: " + name + ", Email: " + email + ", Phone: " + phone);
    }

    public void addCat(List<Cat> cats, Cat cat) {
        if (!cats.contains(cat)) {
            cats.add(cat);
            System.out.println("Cat added successfully.");
        } else {
            System.out.println("Cat already exists.");
        }
    }

    public void updateCat(Cat cat, int price, boolean availability, String name, String color, int age, String gender) {
        cat.updateCat(price, availability, name, color, age, gender);
    }

    public void deleteCat(List<Cat> cats, Cat cat) {
        if (cats.remove(cat)) {
            System.out.println("Cat deleted successfully.");
        } else {
            System.out.println("Cat not found.");
        }
    }

    public void viewAllCats(List<Cat> cats) {
        for (Cat cat : cats) {
            System.out.println(cat.toString());
        }
    }

    public void viewAllReservations(List<Reservation> reservations) {
        for (Reservation reservation : reservations) {
            System.out.println(reservation.toString());
        }
    }

    public void viewAllCustomers(List<Customer> customers) {
        for (Customer customer : customers) {
            customer.viewProfile();
        }
    }
}
