module com.example.mellotron2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.mellotron2 to javafx.fxml;
    exports com.example.mellotron2;
}