module com.project.mp3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.jfoenix;


    opens com.project.mp3 to javafx.fxml;
    exports com.project.mp3;
}