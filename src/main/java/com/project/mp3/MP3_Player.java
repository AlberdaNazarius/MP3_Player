package com.project.mp3;

import com.project.mp3.auxiliaryClasses.Resizer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MP3_Player extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MP3_Player.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Vanci");
        stage.setMinHeight(650);
        stage.setMinWidth(830);

        stage.initStyle(StageStyle.UNDECORATED);

        stage.setScene(scene);
        stage.show();

        Resizer.addResizeListener(stage);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}