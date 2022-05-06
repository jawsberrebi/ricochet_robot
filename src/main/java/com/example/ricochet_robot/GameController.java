package com.example.ricochet_robot;

import com.example.ricochet_robot.backend.Cell;
import com.example.ricochet_robot.backend.Game;
import com.example.ricochet_robot.backend.Orientation;
import com.example.ricochet_robot.backend.Robot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private Game game = new Game();
    private final Pane[][] board = new Pane[16][16];

    private final String filePathRoot = "src/main/resources/com/example/ricochet_robot/";

    @FXML
    private GridPane boardPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Lancement du jeu
        this.game.play();
        //Création du plateau en frontend
        boardGeneration();
    }

    private void boardGeneration(){
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                StackPane stackPane = new StackPane();

                Image cellImage = new Image(new File(filePathRoot + "boards/Cell.PNG").toURI().toString() , 44, 44, false, false);
                ImageView cellImageView = new ImageView(cellImage);
                stackPane.getChildren().add(cellImageView);

                Cell currentCell = this.game.getBoard().getCells()[j + 1][i + 1];

                String wallImageFilename = null;
                if (currentCell.isThereWall()) {
                    for (int w = 0; w < currentCell.getWalls().size(); w++) {
                        switch (currentCell.getWalls().get(w).getOrientation()) {
                            case NORTH -> wallImageFilename = "wallNorth.png";
                            case SOUTH -> wallImageFilename = "wallSouth.png";
                            case EAST -> wallImageFilename = "wallEast.png";
                            case WEST -> wallImageFilename = "wallWest.png";
                        }

                        // Add wall image to cell
                        Image wallImage = new Image(new File(filePathRoot + "boards/" + wallImageFilename).toURI().toString() , 44, 44, false, false);
                        ImageView wallImageView = new ImageView(wallImage);

                        stackPane.getChildren().add(wallImageView);
                    }
                }

                if (this.game.getBoard().getCells()[j + 1][i + 1].isThereARobot()) {
                    Robot robot = this.game.getBoard().getCells()[j + 1][i + 1].getCurrentRobot();
                    String filename;
                    System.out.println(j + 1 + ", " + i + 1);

                    if (robot.getColor().equals(Color.RED)) {
                        filename = "robotRed.png";
                    } else if (robot.getColor().equals(Color.BLUE)) {
                        filename = "robotBlue.png";
                    } else if (robot.getColor().equals(Color.GREEN)) {
                        filename = "robotGreen.png";
                    } else {
                        filename = "robotYellow.png";
                    }

                    Image robotImage = new Image(new File("src/main/resources/com/example/ricochet_robot/robots/" + filename).toURI().toString() , 44, 44, false, false);
                    ImageView robotImageView = new ImageView(robotImage);

                    stackPane.getChildren().add(robotImageView);
                }


                if((i != 8 && i != 7) || (j != 7 && j != 8)){

                    this.board[i][j] = stackPane;
                    this.boardPane.add(stackPane, i, j);
                }
            }
        }
    }
}
