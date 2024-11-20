package com.example;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField userIdField;
    //@FXML
    //private TextField emailField;

    @SuppressWarnings("exports")
    @FXML
    public void handleLogin(ActionEvent event) {
        String userId = userIdField.getText();
        //String email = emailField.getText();

        if (userId.startsWith("admin")) {
            openAdminDashboard();
        } else {
            openCustomerDashboard();
        }
    }

    private void openAdminDashboard() {
        Stage stage = (Stage) userIdField.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminView.fxml"));
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException ex) {
        }
        stage.show();
    }

    private void openCustomerDashboard() {
        Stage stage = (Stage) userIdField.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerView.fxml"));
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException ex) {
        }
        stage.show();
    }
}

