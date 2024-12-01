package catbreedersystem;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class ProfileController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    public void initialize() {
        loadProfile();
    }

    private void loadProfile() {

    }

    @FXML
    private void handleUpdateProfile() {

        // String name = nameField.getText();
        // String email = emailField.getText();
        // String phone = phoneField.getText();

        // DatabaseConnection.updateUserProfile(name, email, phone);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Profile Updated");
        alert.setHeaderText(null);
        alert.setContentText("Profile updated successfully!");
        alert.showAndWait();
    }
}

