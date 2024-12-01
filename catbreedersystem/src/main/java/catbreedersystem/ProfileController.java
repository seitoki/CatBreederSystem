package catbreedersystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ProfileController {

    @FXML
    private Label idLabel;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    private String userID = "0001";

    public void initialize() {
        loadProfile();
    }

    private void loadProfile() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM User WHERE userID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, userID);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                idLabel.setText(rs.getString("userID"));
                nameField.setText(rs.getString("name"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("phone"));
            }
        } catch (Exception e) {
            showError("Error loading profile", e.getMessage());
        }
    }

    @FXML
    private void handleUpdateProfile() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showError("Validation Error", "All fields must be filled.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String updateQuery = "UPDATE User SET name = ?, email = ?, phone = ? WHERE userID = ?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, userID);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                showInfo("Profile Updated", "Your profile has been updated successfully.");
            } else {
                showError("Update Failed", "No changes were made to your profile.");
            }
        } catch (Exception e) {
            showError("Error updating profile", e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


