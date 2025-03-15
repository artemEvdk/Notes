module notes.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.notesapp to javafx.fxml;
    opens org.example.notesapp.controllers to javafx.fxml;
    exports org.example.notesapp;
    exports org.example.notesapp.controllers;
}