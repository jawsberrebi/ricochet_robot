package com.example.ricochet_robot;

import com.example.ricochet_robot.backend.*;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class GameController implements Initializable {

    private final Game game = new Game();
    private final Pane[][] board = new Pane[16][16];

    private final String filePathRoot = "src/main/resources/com/example/ricochet_robot/";
    private Robot selectedRobot;

    @FXML
    private GridPane boardPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Lancement du jeu
        this.game.play();
        //Création du plateau en frontend
        Scene scene = boardPane.getScene();
        initKeyListeners(scene);
        boardGeneration();
    }

    private void initKeyListeners(Scene scene) {
        /*
        scene.setOnKeyPressed(e -> {
            System.out.println("Keystroke");
            if (selectedRobot != null) {
                switch (e.getCode()) {
                    case UP -> move(Orientation.NORTH);
                    case DOWN -> move(Orientation.SOUTH);
                    case RIGHT -> move(Orientation.EAST);
                    case LEFT -> move(Orientation.WEST);
                }
            } else {
                System.out.println("Il faut choisir un robot d'abord");
            }
        });
        */
    }

    private void boardGeneration() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                StackPane stackPane = new StackPane();

                // Ajouter un identifiant
                stackPane.setId(Integer.toString(i + 1) + "," + Integer.toString(j + 1));

                // Ajouter image de cellule
                Image cellImage = new Image(new File(filePathRoot + "boards/Cell.PNG").toURI().toString() , 44, 44, false, false);
                ImageView cellImageView = new ImageView(cellImage);
                stackPane.getChildren().add(cellImageView);

                Cell currentCell = this.game.getBoard().getCells()[i + 1][j + 1];

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

                // Afficher robots
                if (this.game.getBoard().getCells()[i + 1][j + 1].isThereARobot()) {
                    Robot robot = this.game.getBoard().getCells()[i + 1][j + 1].getCurrentRobot();

                    String filename = getRobotImageFilename(robot.getColor());

                    Image robotImage = new Image(new File("src/main/resources/com/example/ricochet_robot/robots/" + filename).toURI().toString() , 44, 44, false, false);
                    ImageView robotImageView = new ImageView(robotImage);

                    stackPane.getChildren().add(robotImageView);
                }


                if((i != 7 && i != 8) || (j != 7 && j != 8)){
                    // Rendre stackPane cliquable
                    stackPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            String id = ((StackPane) event.getSource()).getId();
                            System.out.println("Tile id : " + id);

                            // Get robot from cell
                            int[] coordinates = Stream.of(id.split(",")).mapToInt(Integer::parseInt).toArray();
                            Cell currentCell = game.getBoard().getCells()[coordinates[0]][coordinates[1]];

                            if (currentCell.isThereARobot()) {
                                selectedRobot = currentCell.getCurrentRobot();
                                selectedRobot.setCurrentCell(currentCell);

                                move(Orientation.NORTH);
                            }

                            event.consume();
                        }
                    });

                    // Ajouter stackPane au board
                    this.board[i][j] = stackPane;
                    this.boardPane.add(stackPane, i, j);
                }
            }
        }

    }

    private String getRobotImageFilename(Color robotColor) {
        if (robotColor.equals(Color.RED)) {
            return "robotRed.png";
        } else if (robotColor.equals(Color.BLUE)) {
            return "robotBlue.png";
        } else if (robotColor.equals(Color.GREEN)) {
            return "robotGreen.png";
        } else {
            return "robotYellow.png";
        }
    }

    private void move(Orientation direction) {
        Cell currentCell = selectedRobot.getCurrentCell();

        if (selectedRobot != null) {
            while (game.isValidMove(currentCell, direction)) {
                Position oldPosition = currentCell.getPosition();
                Position newPosition = currentCell.getPosition().nextPosition(direction);

                System.out.print("Old tile: ");
                System.out.print(currentCell.getPosition().getRow() + "," + currentCell.getPosition().getColumn());
                System.out.print(" New tile: ");
                System.out.println(newPosition.getRow() + "," + newPosition.getColumn());

                game.move(currentCell, direction);
                updateRobotDisplay(oldPosition, newPosition);
                currentCell = game.getBoard().getCell(newPosition);
            }
        }
    }

    private void removeRobotFromCell(Position position) {
        int numberOfChildren = board[position.getRow()  - 1][position.getColumn() - 1].getChildren().size();
        board[position.getRow() - 1][position.getColumn() - 1].getChildren().remove(numberOfChildren - 1);
    }

    private void addRobotToCell(Position position) {
        Robot robot = selectedRobot;
        String filename = getRobotImageFilename(selectedRobot.getColor());

        // Get stackPane
        Image robotImage = new Image(new File("src/main/resources/com/example/ricochet_robot/robots/" + filename).toURI().toString() , 44, 44, false, false);
        ImageView robotImageView = new ImageView(robotImage);

        board[position.getRow() - 1][position.getColumn() - 1].getChildren().add(robotImageView);
    }

    private void updateRobotDisplay(Position oldPosition, Position newPosition) {
        removeRobotFromCell(oldPosition);
        addRobotToCell(newPosition);
    }

}
