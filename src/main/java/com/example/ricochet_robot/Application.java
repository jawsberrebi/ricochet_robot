package com.example.ricochet_robot;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("draft.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        // Init keystroke event handler
        initKeyEventHandler(scene);
    }

    private void initKeyEventHandler(Scene scene) {
        scene.setOnKeyPressed(e -> {
            System.out.println("Pressed");
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
