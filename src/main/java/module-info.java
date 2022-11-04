module com.project.mp3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.jfoenix;


    opens com.project.mp3 to javafx.fxml;
    exports com.project.mp3;
    exports com.project.mp3.events;
    opens com.project.mp3.events to javafx.fxml;
    exports com.project.mp3.components;
    opens com.project.mp3.components to javafx.fxml;
    exports com.project.mp3.auxiliaryClasses;
    opens com.project.mp3.auxiliaryClasses to javafx.fxml;
}