module catbreedersystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens catbreedersystem to javafx.fxml;
    exports catbreedersystem;
}

