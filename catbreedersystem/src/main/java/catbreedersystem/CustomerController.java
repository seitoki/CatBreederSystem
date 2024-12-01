package catbreedersystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;

public class CustomerController {

    @FXML
    private ListView<Cat> availableCatListView;

    @FXML
    private ListView<Reservation> reservationListView;

    private Customer currentCustomer;
    private final ObservableList<Cat> availableCats = FXCollections.observableArrayList();
    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    public void initialize(Customer customer) {
        this.currentCustomer = customer;
        availableCats.addAll(currentCustomer.viewAvaliableCats(Cat.cats));
        availableCatListView.setItems(availableCats);
        reservationListView.setItems(reservations);
    }

    @FXML
    private void handleReserveCat() {
        Cat selectedCat = availableCatListView.getSelectionModel().getSelectedItem();
        if (selectedCat != null) {
            Reservation reservation = currentCustomer.reserveCat(selectedCat, "2024-11-20");  // Example date
            if (reservation != null) {
                reservations.add(reservation);
                availableCats.remove(selectedCat);
                showAlert("Reservation Successful", "You have reserved the selected cat.");
            } else {
                showAlert("Reservation Failed", "The selected cat is not available.");
            }
        } else {
            showAlert("No Selection", "Please select a cat to reserve.");
        }
    }

    @FXML
    private void handleCancelReservation() {
        Reservation selectedReservation = reservationListView.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            currentCustomer.cancelReservation(selectedReservation.getReserveID());
            reservations.remove(selectedReservation);
            showAlert("Reservation Canceled", "The selected reservation has been canceled.");
        } else {
            showAlert("No Selection", "Please select a reservation to cancel.");
        }
    }

    @FXML
    private void handleViewProfile() {
        showAlert("Profile", "Customer Profile:\nName: " + currentCustomer.name +
                "\nEmail: " + currentCustomer.email + "\nPhone: " + currentCustomer.phone);
    }

    @FXML
    private void handleUpdateProfile() {
        // Example: Updating the profile with sample data
        currentCustomer.updateProfile("Updated Name", "updatedemail@example.com", "1234567890");
        showAlert("Profile Updated", "Your profile has been updated.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
