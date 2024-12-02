package catbreedersystem;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class AdminController implements Initializable{

    // TableViews and Columns
    // Cats Table
    @FXML
    private TableView<Cat> catTableView;
    @FXML
    private TableColumn<Cat, String> catIDColumn;
    @FXML
    private TableColumn<Cat, String> catNameColumn;
    @FXML
    private TableColumn<Cat, String> catGenderColumn;
    @FXML
    private TableColumn<Cat, String> catColorColumn;
    @FXML
    private TableColumn<Cat, String> catBirthdateColumn;
    @FXML
    private TableColumn<Cat, Integer> catPriceColumn;
    @FXML
    private TableColumn<Cat, Boolean> catAvailabilityColumn;

    // Customers Table
    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, String> customerIDColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> customerEmailColumn;
    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;

    // Reservations Table
    @FXML
    private TableView<Reservation> reservationTableView;
    @FXML
    private TableColumn<Reservation, String> reserveIDColumn;
    @FXML
    private TableColumn<Reservation, String> reserveCatIDColumn;
    @FXML
    private TableColumn<Reservation, String> reserveCustomerIDColumn;
    @FXML
    private TableColumn<Reservation, String> reserveDateColumn;

    // Input Fields for Add/Update
    @FXML
    private VBox addCatFields;
    @FXML
    private VBox updateCatFields;
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
    private VBox addUserFields;
    @FXML
    private TextField userNameField;
    @FXML
    private TextField userEmailField;
    @FXML
    private TextField userPhoneField;
    @FXML
    private TextField userRoleField;

    // ObservableLists for holding data
    private final ObservableList<Cat> cats = FXCollections.observableArrayList();
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // Cats Table
        catIDColumn.setCellValueFactory(new PropertyValueFactory<>("catID"));
        catNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        catGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        catBirthdateColumn.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        catColorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        catPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        catAvailabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
        catTableView.setItems(cats);

        // Customers Table
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerTableView.setItems(customers);

        // Reservations Table
        reserveIDColumn.setCellValueFactory(new PropertyValueFactory<>("reserveID"));
        reserveCatIDColumn.setCellValueFactory(new PropertyValueFactory<>("catID"));
        reserveCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        reserveDateColumn.setCellValueFactory(new PropertyValueFactory<>("reserveDate"));
        reservationTableView.setItems(reservations);

        // Load data
        loadCats();
        loadCustomers();
        loadReservations();


    }

    // Toggle Visibility of Add/Update Fields
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

    // Generate Next Cat ID
    private String generateCatID() {
        String query = "SELECT catID FROM Cat ORDER BY catID DESC LIMIT 1";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                String lastID = resultSet.getString("catID");
                int lastNumber = Integer.parseInt(lastID.substring(1));
                return String.format("C%04d", lastNumber + 1);
            }
        } catch (Exception e) {
        }
        return "C0000";
    }


    private boolean validateCatFields(TextField nameField, TextField birthdateField, TextField genderField, TextField colorField, TextField priceField) {
        // Check if any field is empty
        if (nameField.getText().trim().isEmpty() ||
            birthdateField.getText().trim().isEmpty() ||
            genderField.getText().trim().isEmpty() ||
            colorField.getText().trim().isEmpty() ||
            priceField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "All fields are required!", Alert.AlertType.ERROR);
            return false;
        }
    
        // Validate price (ensure it's numeric)
        try {
            Integer.parseInt(priceField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Price must be numeric!", Alert.AlertType.ERROR);
            return false;
        }
    
        // Additional validations (e.g., date format, gender options) can be added here
        if (!genderField.getText().trim().equalsIgnoreCase("Male") && !genderField.getText().trim().equalsIgnoreCase("Female")) {
            showAlert("Validation Error", "Gender must be 'Male' or 'Female'!", Alert.AlertType.ERROR);
            return false;
        }
    
        return true; // Validation passed
    }
    
    // Add a New Cat
    @FXML
    private void handleAddCat() {
        if (!validateCatFields(catNameField, catBirthdateField, catGenderField, catColorField, catPriceField)) return;

        String newCatID = generateCatID();
        String query = "INSERT INTO Cat (catID, name, birthdate, gender, color, price, availability) "
                     + "VALUES (?, ?, ?, ?, ?, ?, true)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newCatID);
            statement.setString(2, catNameField.getText().trim());
            statement.setString(3, catBirthdateField.getText().trim());
            statement.setString(4, catGenderField.getText().trim());
            statement.setString(5, catColorField.getText().trim());
            statement.setInt(6, Integer.parseInt(catPriceField.getText().trim()));

            statement.executeUpdate();
            showAlert("Success", "Cat added successfully!", Alert.AlertType.INFORMATION);
            loadCats();
            clearFields(catNameField, catBirthdateField, catGenderField, catColorField, catPriceField);
        } catch (Exception e) {
            showAlert("Error", "Failed to add cat: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Delete Selected Cat
    @FXML
    private void handleDeleteCat() {
        Cat selectedCat = catTableView.getSelectionModel().getSelectedItem();
        if (selectedCat == null) {
            showAlert("Error", "Please select a cat to delete.", Alert.AlertType.ERROR);
            return;
        }

        String query = "DELETE FROM Cat WHERE catID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, selectedCat.getCatID());
            statement.executeUpdate();
            showAlert("Success", "Cat deleted successfully!", Alert.AlertType.INFORMATION);
            loadCats();
        } catch (Exception e) {
            showAlert("Error", "Failed to delete cat: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void toggleAddUserFields() {
        toggleVisibility(addUserFields);
    }
    
    // Handle Add User action
    @FXML
    private void handleAddUser() {
        String name = userNameField.getText().trim();
        String email = userEmailField.getText().trim();
        String phone = userPhoneField.getText().trim();
        String role = userRoleField.getText().trim().toLowerCase();
    
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || role.isEmpty()) {
            showAlert("Validation Error", "All fields are required!", Alert.AlertType.ERROR);
            return;
        }
    
        if (!role.equals("admin") && !role.equals("customer")) {
            showAlert("Validation Error", "Role must be 'admin' or 'customer'!", Alert.AlertType.ERROR);
            return;
        }
    
        String newUserID = generateUserID(); // Generate the next user ID
    
        String query = "INSERT INTO User (userID, name, email, phone, role) VALUES (?, ?, ?, ?, ?)";
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            statement.setString(1, newUserID);
            statement.setString(2, name);
            statement.setString(3, email);
            statement.setString(4, phone);
            statement.setString(5, role);
            statement.executeUpdate();
    
            showAlert("Success", "User added successfully with ID: " + newUserID, Alert.AlertType.INFORMATION);
            clearFields(userNameField, userEmailField, userPhoneField, userRoleField);
            loadCustomers(); // Reload customers table to reflect the change
        } catch (Exception e) {
            showAlert("Error", "Failed to add user: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
 
    private String generateUserID() {
        String query = "SELECT userID FROM User ORDER BY userID DESC LIMIT 1"; 
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
    
            if (resultSet.next()) {
                String lastID = resultSet.getString("userID");
                int lastNumber = Integer.parseInt(lastID); 
                return String.format("%04d", lastNumber + 1); 
            }
        } catch (Exception e) {
        }
        return "0001"; 
    }

    
    // Update Selected Cat
    @FXML
    private void handleUpdateCat() {
        Cat selectedCat = catTableView.getSelectionModel().getSelectedItem();
        if (selectedCat == null) {
            showAlert("Error", "Please select a cat to update.", Alert.AlertType.ERROR);
            return;
        }

        String updatedName = updateCatNameField.getText().trim().isEmpty() ? selectedCat.getName() : updateCatNameField.getText().trim();
        String updatedBirthdate = updateCatBirthdateField.getText().trim().isEmpty() ? selectedCat.getBirthdate() : updateCatBirthdateField.getText().trim();
        String updatedGender = updateCatGenderField.getText().trim().isEmpty() ? selectedCat.getGender() : updateCatGenderField.getText().trim();
        String updatedColor = updateCatColorField.getText().trim().isEmpty() ? selectedCat.getColor() : updateCatColorField.getText().trim();
        int updatedPrice = updateCatPriceField.getText().trim().isEmpty() ? selectedCat.getPrice() : Integer.parseInt(updateCatPriceField.getText().trim());

        String query = "UPDATE Cat SET name = ?, birthdate = ?, gender = ?, color = ?, price = ? WHERE catID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, updatedName);
            statement.setString(2, updatedBirthdate);
            statement.setString(3, updatedGender);
            statement.setString(4, updatedColor);
            statement.setInt(5, updatedPrice);
            statement.setString(6, selectedCat.getCatID());

            statement.executeUpdate();
            showAlert("Success", "Cat updated successfully!", Alert.AlertType.INFORMATION);
            loadCats();
            clearFields(updateCatNameField, updateCatBirthdateField, updateCatGenderField, updateCatColorField, updateCatPriceField);
        } catch (Exception e) {
            showAlert("Error", "Failed to update cat: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    public void populateUpdateFields() {
        Cat selectedCat = catTableView.getSelectionModel().getSelectedItem();
        if (selectedCat == null) {
            showAlert("Error", "No cat selected to populate fields.", Alert.AlertType.ERROR);
            return;
        }
    
        updateCatNameField.setText(selectedCat.getName());
        updateCatBirthdateField.setText(selectedCat.getBirthdate());
        updateCatGenderField.setText(selectedCat.getGender());
        updateCatColorField.setText(selectedCat.getColor());
        updateCatPriceField.setText(String.valueOf(selectedCat.getPrice()));
    }

    
    private void loadCats() {
        cats.clear();
        String query = "SELECT * FROM Cat";
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
                cats.add(cat);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load cats: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void loadCustomers() {
        customers.clear();
        String query = "SELECT * FROM User WHERE role = 'customer'";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Customer customer = new Customer(
                        resultSet.getString("userID"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                );
                customers.add(customer);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load customers: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void loadReservations() {
        reservations.clear();
        String query = "SELECT * FROM Reservation";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                        resultSet.getString("reserveID"),
                        resultSet.getString("catID"),
                        resultSet.getString("userID"),
                        resultSet.getString("reserveDate")
                );
                reservations.add(reservation);

            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load reservations: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    


    // Utility Methods
    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
