package catbreedersystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CustomerController {

    @FXML
    private ListView<Cat> availableCatsListView;

    @FXML
    private ListView<Reservation> myReservationsListView;

    @FXML
    private TextField searchField;

    private final ObservableList<Cat> availableCats = FXCollections.observableArrayList();
    private final ObservableList<Reservation> myReservations = FXCollections.observableArrayList();
    private String currentUserID; 

    public void initialize() {
        availableCatsListView.setItems(availableCats);
        myReservationsListView.setItems(myReservations);

        loadAvailableCats();
        loadMyReservations();
    }

    public void setCurrentUserID(String userID) {
        this.currentUserID = userID;
    }

    private void loadAvailableCats() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Cat WHERE availability = true");
            while (resultSet.next()) {
                Cat cat = new Cat(
                    resultSet.getString("catID"),
                    resultSet.getString("name"),
                    resultSet.getString("birthdate"),
                    resultSet.getString("gender"),
                    resultSet.getString("color"),
                    resultSet.getInt("price"),
                    resultSet.getBoolean("availability")
                );
                availableCats.add(cat);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load available cats: " + e.getMessage());
        }
    }

    private void loadMyReservations() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            String query = "SELECT r.reserveID, r.catID, r.reserveDate, c.name " +
                           "FROM Reservation r " +
                           "JOIN Cat c ON r.catID = c.catID " +
                           "WHERE r.userID = '" + currentUserID + "'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                    resultSet.getString("reserveID"),
                    resultSet.getString("catID"),
                    currentUserID,
                    resultSet.getDate("reserveDate").toString()
                );
                myReservations.add(reservation);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load reservations: " + e.getMessage());
        }
    }

    @FXML
    private void handleReserveCat() {
        Cat selectedCat = availableCatsListView.getSelectionModel().getSelectedItem();
        if (selectedCat != null) {
            try (Connection connection = DatabaseConnection.getConnection();
                 Statement statement = connection.createStatement()) {
                String reserveID = "R" + (myReservations.size() + 1);
                String query = "INSERT INTO Reservation (reserveID, catID, userID, reserveDate) VALUES " +
                               "('" + reserveID + "', '" + selectedCat.getCatID() + "', '" + currentUserID + "', CURDATE())";
                statement.executeUpdate(query);

                Reservation newReservation = new Reservation(
                    reserveID,
                    selectedCat.getCatID(),
                    currentUserID,
                    java.time.LocalDate.now().toString()
                );
                myReservations.add(newReservation);
                availableCats.remove(selectedCat);

                statement.executeUpdate("UPDATE Cat SET availability = false WHERE catID = '" + selectedCat.getCatID() + "'");

                showAlert("Success", "Cat reserved successfully!");
            } catch (Exception e) {
                showAlert("Error", "Failed to reserve cat: " + e.getMessage());
            }
        } else {
            showAlert("No Selection", "Please select a cat to reserve.");
        }
    }

    @FXML
    private void handleCancelReservation() {
        Reservation selectedReservation = myReservationsListView.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            try (Connection connection = DatabaseConnection.getConnection();
                 Statement statement = connection.createStatement()) {
                String query = "DELETE FROM Reservation WHERE reserveID = '" + selectedReservation.getReserveID() + "'";
                statement.executeUpdate(query);

                myReservations.remove(selectedReservation);

                statement.executeUpdate("UPDATE Cat SET availability = true WHERE catID = '" + selectedReservation.getCatID() + "'");
                showAlert("Success", "Reservation cancelled successfully!");
            } catch (Exception e) {
                showAlert("Error", "Failed to cancel reservation: " + e.getMessage());
            }
        } else {
            showAlert("No Selection", "Please select a reservation to cancel.");
        }
    }


    @FXML
    public void handleViewProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profileview.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Profile View");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
        }
    }

    @FXML
    private void handleSearchCats() {
        String keyword = searchField.getText().toLowerCase();
        availableCatsListView.setItems(availableCats.filtered(cat -> cat.toString().toLowerCase().contains(keyword)));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
