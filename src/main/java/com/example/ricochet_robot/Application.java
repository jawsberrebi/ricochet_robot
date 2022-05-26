package com.example.ricochet_robot;

import com.example.ricochet_robot.backend.Game;
import com.example.ricochet_robot.backend.Orientation;
import com.example.ricochet_robot.backend.Robot;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    GameController gameController;
    Game game;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("draft.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        // Get controller
        gameController = fxmlLoader.getController();
        // Init keystroke event handler
        initKeyEventHandler(scene);
    }

    private void initKeyEventHandler(Scene scene) {
        if(Game.Status != Game.Status.END_ROUND){
            scene.setOnKeyPressed(e -> {
                System.out.println("Pressed");
                if (gameController.itIsFinished()) {
                    return;
                }
                Robot selectedRobot = gameController.selectedRobot;
                if (selectedRobot != null) {
                    switch (e.getCode()) {
                        case W -> {
                            gameController.move(Orientation.NORTH);
                            gameController.setHits();
                        }
                        case S -> {
                            gameController.move(Orientation.SOUTH);
                            gameController.setHits();
                        }
                        case D -> {
                            gameController.move(Orientation.EAST);
                            gameController.setHits();
                        }
                        case A -> {
                            gameController.move(Orientation.WEST);
                            gameController.setHits();
                        }
                    }
                } else {
                    System.out.println("Il faut choisir un robot d'abord");
                }
                e.consume();
            });
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
