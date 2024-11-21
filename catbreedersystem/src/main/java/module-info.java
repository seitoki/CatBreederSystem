module catbreedersystem {
    requires javafx.controls;
    requires javafx.fxml;

    opens catbreedersystem to javafx.fxml;
    exports catbreedersystem;
}
