module com.project.mp3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.project.mp3 to javafx.fxml;
    exports com.project.mp3;
}