package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @SuppressWarnings("exports")
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cat Breeder System Login");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
