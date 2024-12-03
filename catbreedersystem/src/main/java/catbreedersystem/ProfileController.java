package catbreedersystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProfileController {

    @FXML
    private Label userNameLabel;

    @FXML
    private Label userEmailLabel;

    @FXML
    private Label userPhoneLabel;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;
    
    @FXML
    private TextField phoneField;

    private String userID;

    public void setUserID(String userID) {
        this.userID = userID;
        loadProfile();
    }

    private void loadProfile() {
        String query = "SELECT name, email, phone FROM User WHERE userID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                userNameLabel.setText("Name: " + resultSet.getString("name"));
                userEmailLabel.setText("Email: " + resultSet.getString("email"));
                userPhoneLabel.setText("Phone: " + resultSet.getString("phone"));
            }
        } catch (Exception e) {
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void handleUpdateProfile() {
        String updatedName = nameField.getText().trim();
        String updatedEmail = emailField.getText().trim();
        String updatedPhone = phoneField.getText().trim();

        if (updatedName.isEmpty() && updatedEmail.isEmpty() && updatedPhone.isEmpty()) {
            showAlert("Validation Error", "Please fill at least one field to update.", Alert.AlertType.ERROR);
            return;
        }

        String query = "UPDATE User SET name = COALESCE(NULLIF(?, ''), name), " +
                       "email = COALESCE(NULLIF(?, ''), email), " +
                       "phone = COALESCE(NULLIF(?, ''), phone) WHERE userID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, updatedName);
            statement.setString(2, updatedEmail);
            statement.setString(3, updatedPhone);
            statement.setString(4, userID);
            
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "Profile updated successfully!", Alert.AlertType.INFORMATION);
                loadProfile();
                clearFields(nameField, emailField, phoneField);
            } else {
                showAlert("Error", "No changes were made.", Alert.AlertType.WARNING);
            }

        } catch (Exception e) {
            showAlert("Error", "Failed to update profile: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) userNameLabel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
