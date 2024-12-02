package catbreedersystem;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CustomerController implements Initializable{

    // TableViews and Columns
    @FXML
    private TableView<Cat> availableCatsTable;
    @FXML
    private TableColumn<Cat, String> catIDColumn;
    @FXML
    private TableColumn<Cat, String> catNameColumn;
    @FXML
    private TableColumn<Cat, String> catGenderColumn;
    @FXML
    private TableColumn<Cat, Integer> catPriceColumn;
    @FXML
    private TableColumn<Cat, String> catBirthdateColumn;
    @FXML
    private TableColumn<Cat, String> catColorColumn;

    @FXML
    private TableView<Reservation> reservationsTable;
    @FXML
    private TableColumn<Reservation, String> reservationIDColumn;
    @FXML
    private TableColumn<Reservation, String> reservedCatIDColumn;
    @FXML
    private TableColumn<Reservation, String> reservationDateColumn;

    // ObservableLists
    private final ObservableList<Cat> availableCats = FXCollections.observableArrayList();
    private final ObservableList<Reservation> myReservations = FXCollections.observableArrayList();

    // Current User ID (To be set when user logs in)
    private String currentUserID = "0001";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
  
        // Initialize Available Cats Table
        catIDColumn.setCellValueFactory(new PropertyValueFactory<>("catID"));
        catNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        catBirthdateColumn.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        catGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        catColorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        catPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        availableCatsTable.setItems(availableCats);

        // Initialize My Reservations Table
        reservationIDColumn.setCellValueFactory(new PropertyValueFactory<>("reserveID"));
        reservedCatIDColumn.setCellValueFactory(new PropertyValueFactory<>("catID"));
        reservationDateColumn.setCellValueFactory(new PropertyValueFactory<>("reserveDate"));
        reservationsTable.setItems(myReservations);

        // Load Data
        loadAvailableCats();
        loadMyReservations();
    }

    public void setCurrentUserID(String userID) {
        this.currentUserID = userID;
    }

    private void loadAvailableCats() {
        availableCats.clear();
        String query = "SELECT * FROM Cat WHERE availability = true";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

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
            showAlert("Error", "Failed to load available cats: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadMyReservations() {
        myReservations.clear();
        String query = "SELECT r.reserveID, r.catID, r.reserveDate FROM Reservation r WHERE r.userID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, currentUserID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                        resultSet.getString("reserveID"),
                        resultSet.getString("catID"),
                        currentUserID,
                        resultSet.getString("reserveDate")
                );
                myReservations.add(reservation);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load reservations: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleReserveCat() {
        Cat selectedCat = availableCatsTable.getSelectionModel().getSelectedItem();
        if (selectedCat == null) {
            showAlert("Error", "Please select a cat to reserve.", Alert.AlertType.ERROR);
            return;
        }

        // Ask for reservation date
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Reservation Date");
        dialog.setHeaderText("Reserve Cat: " + selectedCat.getName());
        dialog.setContentText("Enter Reservation Date (YYYY-MM-DD):");
        String reservationDate = dialog.showAndWait().orElse("");

        if (reservationDate.trim().isEmpty()) {
            showAlert("Error", "Reservation date cannot be empty.", Alert.AlertType.ERROR);
            return;
        }

        String newReserveID = generateReservationID();
        String query = "INSERT INTO Reservation (reserveID, catID, userID, reserveDate) VALUES (?, ?, ?, ?)";
        String updateQuery = "UPDATE Cat SET availability = false WHERE catID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement insertStatement = connection.prepareStatement(query);
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            // Insert reservation
            insertStatement.setString(1, newReserveID);
            insertStatement.setString(2, selectedCat.getCatID());
            insertStatement.setString(3, currentUserID);
            insertStatement.setString(4, reservationDate);
            insertStatement.executeUpdate();

            // Update cat availability
            updateStatement.setString(1, selectedCat.getCatID());
            updateStatement.executeUpdate();

            showAlert("Success", "Reservation successful!", Alert.AlertType.INFORMATION);
            loadAvailableCats();
            loadMyReservations();
        } catch (Exception e) {
            showAlert("Error", "Failed to reserve cat: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private String generateReservationID() {
        String query = "SELECT reserveID FROM Reservation ORDER BY reserveID DESC LIMIT 1";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                String lastID = resultSet.getString("reserveID");
                int lastNumber = Integer.parseInt(lastID.substring(1));
                return String.format("R%04d", lastNumber + 1);
            }
        } catch (Exception e) {
        }
        return "R0000";
    }
    

    @FXML
    private void handleCancelReservation() {
        Reservation selectedReservation = reservationsTable.getSelectionModel().getSelectedItem();
        if (selectedReservation == null) {
            showAlert("Error", "Please select a reservation to cancel.", Alert.AlertType.ERROR);
            return;
        }

        String query = "DELETE FROM Reservation WHERE reserveID = ?";
        String updateQuery = "UPDATE Cat SET availability = true WHERE catID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(query);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            // Delete reservation
            deleteStatement.setString(1, selectedReservation.getReserveID());
            deleteStatement.executeUpdate();

            // Update cat availability
            updateStatement.setString(1, selectedReservation.getCatID());
            updateStatement.executeUpdate();

            showAlert("Success", "Reservation cancelled successfully!", Alert.AlertType.INFORMATION);
            loadAvailableCats();
            loadMyReservations();
        } catch (Exception e) {
            showAlert("Error", "Failed to cancel reservation: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleViewProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profileView.fxml"));
            Parent profileView = loader.load();

            ProfileController profileController = loader.getController();
            profileController.setUserID(currentUserID);

            Stage stage = new Stage();
            stage.setTitle("My Profile");
            stage.setScene(new Scene(profileView));
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to load profile view: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
