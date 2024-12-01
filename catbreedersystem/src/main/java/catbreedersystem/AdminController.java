package catbreedersystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AdminController {

    @FXML
    private ListView<Cat> catListView;

    @FXML
    private ListView<Customer> customerListView;

    @FXML
    private ListView<Reservation> reservationListView;

    @FXML
    private VBox addCatFields;
    @FXML
    private VBox  updateCatFields;

    @FXML
    private TextField catNameField;
    @FXML
    private TextField catBirthdateField;
    @FXML
    private TextField catGenderField;
    @FXML
    private TextField catColorField;
    @FXML
    private TextField catPriceField;
    @FXML
    private TextField updateCatNameField;
    @FXML
    private TextField updateCatBirthdateField;
    @FXML
    private TextField updateCatGenderField;
    @FXML
    private TextField updateCatColorField;
    @FXML
    private TextField updateCatPriceField;

    @FXML
    private TextField updateCatIDField;

    private final ObservableList<Cat> cats = FXCollections.observableArrayList();
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    public void initialize() {
        catListView.setItems(cats);
        customerListView.setItems(customers);
        reservationListView.setItems(reservations);

        loadCats();
        loadCustomers();
        loadReservations();
    }

    @FXML
    private void toggleAddCatFields() {
        toggleVisibility(addCatFields);
    }

    @FXML
    private void toggleUpdateCatFields() {
        toggleVisibility(updateCatFields);
    }

    private void toggleVisibility(VBox fieldBox) {
        boolean isVisible = fieldBox.isVisible();
        fieldBox.setVisible(!isVisible);
        fieldBox.setManaged(!isVisible);
    }

    @FXML
    private void handleAddCat() {
        if (!validateCatFields(catNameField, catBirthdateField, catGenderField, catColorField, catPriceField)) return;

        String query = "INSERT INTO Cat (catID, name, birthdate, gender, color, price, availability) "
        + "VALUES (UUID(), ?, ?, ?, ?, ?, true)";

        executeCatUpdate(query, catNameField, catBirthdateField, catGenderField, catColorField, catPriceField);
        loadCats();
    }

    @FXML
    private void handleDeleteCat() {
        Cat selectedCat = catListView.getSelectionModel().getSelectedItem();
        if (selectedCat == null) {
            showAlert("No Selection", "Please select a cat to delete.");
            return;
        }

        String query = "DELETE FROM Cat WHERE catID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, selectedCat.getCatID());
            statement.executeUpdate();
            showAlert("Success", "Cat deleted successfully!");
            loadCats();
        } catch (Exception e) {
            showAlert("Error", "Failed to delete cat: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateCat() {
        Cat selectedCat = catListView.getSelectionModel().getSelectedItem();
        if (selectedCat == null || !validateCatFields(updateCatNameField, updateCatBirthdateField, updateCatGenderField, updateCatColorField, updateCatPriceField)) {
            return;
        }

        String query = "UPDATE Cat " +
        "SET name = ?, birthdate = ?, gender = ?, color = ?, price = ? " +
        "WHERE catID = ?";

        updateCatIDField.setText(selectedCat.getCatID());;
        executeCatUpdate(query, updateCatNameField, updateCatBirthdateField, updateCatGenderField, updateCatColorField, updateCatPriceField, updateCatIDField);
        loadCats();
    }

    private void executeCatUpdate(String query, TextField... fields) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < fields.length - 1; i++) {
                statement.setString(i + 1, fields[i].getText().trim());
            }
            if (fields.length == 6) { // For update queries with catID
                statement.setString(6, fields[5]);
            }
            statement.executeUpdate();
            showAlert("Success", "Operation successful!");
            clearFields(fields);
        } catch (Exception e) {
            showAlert("Error", "Operation failed: " + e.getMessage());
        }
    }

    private boolean validateCatFields(TextField... fields) {
        for (TextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                showAlert("Validation Error", "All fields are required!");
                return false;
            }
        }
        try {
            Integer.parseInt(fields[4].getText().trim());
            return true;
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Price must be numeric!");
            return false;
        }
    }

    private void loadCats() {
        loadList(cats, "SELECT * FROM Cat", rs -> new Cat(
                rs.getString("catID"),
                rs.getString("name"),
                rs.getString("birthdate"),
                rs.getString("gender"),
                rs.getString("color"),
                rs.getInt("price"),
                rs.getBoolean("availability")
        ));
    }

    private void loadCustomers() {
        loadList(customers, "SELECT * FROM User WHERE role = 'customer'", rs -> new Customer(
                rs.getString("userID"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone")
        ));
    }

    private void loadReservations() {
        loadList(reservations, "SELECT * FROM Reservation", rs -> new Reservation(
                rs.getString("reserveID"),
                rs.getString("catID"),
                rs.getString("userID"),
                rs.getString("reserveDate")
        ));
    }

    private <T> void loadList(ObservableList<T> list, String query, ResultMapper<T> mapper) {
        list.clear();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                list.add(mapper.map(resultSet));
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load data: " + e.getMessage());
        }
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FunctionalInterface
    interface ResultMapper<T> {
        T map(ResultSet resultSet) throws Exception;
    }
}
