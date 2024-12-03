package catbreedersystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField userIdField;
    @FXML
    private TextField emailField;

    private final String jdbcUrl = "jdbc:mysql://localhost:3306/cat_breeder_system"; // Replace with your database URL
    private final String dbUsername = "root"; // Replace with your MySQL username
    private final String dbPassword = "oyrq1206"; // Replace with your MySQL
    // password

    @FXML
    private void handleLogin() {
        String userID = userIdField.getText().trim();
        String email = emailField.getText().trim();
    
        if (userID.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter both user ID and email.");
            return;
        }
    
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            String query = "SELECT role FROM User WHERE userID = ? AND email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userID);
                preparedStatement.setString(2, email);
    
                ResultSet resultSet = preparedStatement.executeQuery();
    
                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    switch (role.toLowerCase()) {
                        case "admin":
                            navigateTo("adminView.fxml", "Admin Dashboard", null);
                            break;
                        case "customer":
                            navigateTo("customerView.fxml", "Customer Dashboard", userID);
                            break;
                        default:
                            showAlert(Alert.AlertType.ERROR, "Error", "Unrecognized role: " + role);
                            break;
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid user ID or email.");
                }
            }
        } catch (Exception e) {

            showAlert(Alert.AlertType.ERROR, "Error", "Failed to connect to the database: " + e.getMessage());
        }
    }
    
    private void navigateTo(String fxmlFile, String title, String userID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
    
            if (userID != null) {
                Object controller = loader.getController();
                if (controller instanceof CustomerController) {
                    ((CustomerController) controller).setCurrentUserID(userID);
                    ((CustomerController) controller).loadMyReservations();
                }
            }
    
            Stage stage = (Stage) userIdField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the view: " + e.getMessage());
        }
    }
    

    // Helper method to show alert messages
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
