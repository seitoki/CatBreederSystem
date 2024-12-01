package catbreedersystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;

public class AdminController {

    @FXML
    private ListView<Cat> catListView;

    @FXML
    private ListView<Customer> customerListView;

    @FXML
    private ListView<Reservation> reservationListView;

    private final ObservableList<Cat> cats = FXCollections.observableArrayList();
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    public void initialize() {
        catListView.setItems(cats);
        customerListView.setItems(customers);
        reservationListView.setItems(reservations);
    }

    @FXML
    private void handleAddCat() {
        // Example: Adding a new cat with sample data; typically, you would open a form here
        Cat newCat = new Cat("C" + (cats.size() + 1), "New Cat", 2, "Male", "Brown", 200, true);
        cats.add(newCat);
        showAlert("Cat Added", "New cat has been added.");
    }

    @FXML
    private void handleDeleteCat() {
        Cat selectedCat = catListView.getSelectionModel().getSelectedItem();
        if (selectedCat != null) {
            cats.remove(selectedCat);
            showAlert("Cat Deleted", "The selected cat has been deleted.");
        } else {
            showAlert("No Selection", "Please select a cat to delete.");
        }
    }

    @FXML
    private void handleUpdateCat() {
        Cat selectedCat = catListView.getSelectionModel().getSelectedItem();
        if (selectedCat != null) {
            selectedCat.updateCat(250, true, "tbc", "red point", 2, "Female");  
            catListView.refresh();
            showAlert("Cat Updated", "The selected cat has been updated.");
        } else {
            showAlert("No Selection", "Please select a cat to update.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
