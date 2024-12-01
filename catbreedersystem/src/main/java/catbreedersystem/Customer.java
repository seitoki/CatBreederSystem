package catbreedersystem;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private final List<Reservation> reservations = new ArrayList<>();

    public Customer(String id, String name, String email, String phone) {
        super(id, name, email, phone, "Customer");
    }

    @Override
    public void viewProfile() {
        System.out.println("Customer Profile: " + name + ", Email: " + email + ", Phone: " + phone);
    }

    public void updateProfile(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public List<Cat> viewAvaliableCats(List<Cat> cats) {
        List<Cat> avaliableCats = new ArrayList<>();
        for (Cat cat : cats) {
            if (cat.availability) {
                avaliableCats.add(cat);
            }
        }
        return avaliableCats;
    }

    public List<Cat> searchCats(List<Cat> cats, String criteria) {
        List<Cat> foundCats = new ArrayList<>();
        for (Cat cat : cats) {
            if (cat.toString().contains(criteria)) {
                foundCats.add(cat);
            }
        }
        return foundCats;
    }

    public Reservation reserveCat(Cat cat, String dateString) {
        if (cat.availability) {
            Reservation reservation = new Reservation("R" + System.currentTimeMillis(), cat.catID, this.id, dateString);
            this.reservations.add(reservation);
            cat.availability = false;
            return reservation;
        } else {
            System.out.println("Cat is not available!");
            return null;
        }
    }

    public void cancelReservation(String resID) {
        reservations.removeIf(reservation -> reservation.reserveID.equals(resID));
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}