module org.example.calendar1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.calendar1 to javafx.fxml;
    exports org.example.calendar1;
}