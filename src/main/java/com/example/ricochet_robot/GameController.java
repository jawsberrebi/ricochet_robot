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
import com.example.ricochet_robot.backend.Cell;
import com.example.ricochet_robot.backend.Game;
import com.example.ricochet_robot.backend.Robot;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class GameController implements Initializable {

    private static final Game game = new Game();
    private final Pane[][] board = new Pane[16][16];
    private final String filePathRoot = "src/main/resources/com/example/ricochet_robot/";
    private Robot selectedRobot;

    private Label timerLabel = new Label(), splitTimerLabel = new Label();
    private DoubleProperty timeSeconds = new SimpleDoubleProperty(), splitTimeSeconds = new SimpleDoubleProperty();
    private Duration time = Duration.ZERO, splitTime = Duration.ZERO;

    @FXML
    private GridPane boardPane;
    @FXML
    private Label indication;
    @FXML
    private ImageView currentImageGoal;
    @FXML
    private Button gameBtn;
    @FXML
    private Label timerText;
    private int i = 30;
    private boolean isTheTimerStopped;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Lancement du jeu
        game.play();
        //Création du plateau en frontend
        Scene scene = boardPane.getScene();
        initKeyListeners(scene);
        boardGeneration();
        //Affichage du jeton objectif à atteindre
        displayGoal();
        Game.Status = Game.Status.START_ROUND;
    }

    @FXML
    private void handleGameBtn(){
        switch (game.Status) {
            case START_ROUND -> {
                this.gameBtn.setVisible(false);
                timer();
                Game.Status = Game.Status.LAUNCH_TIMER;
            }
            case LAUNCH_TIMER -> {
                timerText.setVisible(true);

            }
        }
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

    private void boardGeneration(){
        Image cellImage = new Image(new File(filePathRoot + "boards/Cell.PNG").toURI().toString() , 44, 44, false, false);

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                StackPane stackPane = new StackPane();

                // Ajouter un identifiant
                stackPane.setId(Integer.toString(i + 1) + "," + Integer.toString(j + 1));

                // Ajouter image de cellule
                ImageView cellImageView = new ImageView(cellImage);
                stackPane.getChildren().add(cellImageView);

                Cell currentCell = this.game.getBoard().getCells()[i + 1][j + 1];

                //Ajout des murs, s'il y en a
                String wallImageFilename = null;
                if (currentCell.getIsThereWall()) {
                    for (int w = 0; w < currentCell.getWalls().size(); w++) {
                        switch (currentCell.getWalls().get(w).getOrientation()) {
                            case NORTH -> wallImageFilename = "NorthWall.png";
                            case SOUTH -> wallImageFilename = "SouthWall.png";
                            case EAST -> wallImageFilename = "EastWall.png";
                            case WEST -> wallImageFilename = "WestWall.png";
                        }

                        // Add wall image to cell
                        Image wallImage = new Image(new File(filePathRoot + "boards/" + wallImageFilename).toURI().toString() , 44, 44, false, false);
                        ImageView wallImageView = new ImageView(wallImage);
                        stackPane.getChildren().add(wallImageView);
                    }
                }

                /* Potentiel bug */
                //Ajout des symboles, s'il y en a
                String symbolImageFilename = null;
                if(currentCell.getIsThereASymbol()){
                    if (Color.BLUE.equals(currentCell.getSymbol().getColor())) {
                        switch (currentCell.getSymbol().getTheShape()) {
                            case GEAR -> symbolImageFilename = "BlueGear.png";
                            case MOON -> symbolImageFilename = "BlueMoon.png";
                            case PLANET -> symbolImageFilename = "BluePlanet.png";
                            case STAR -> symbolImageFilename = "BlueStar.png";
                        }
                    } else if (Color.GREEN.equals(currentCell.getSymbol().getColor())) {
                        switch (currentCell.getSymbol().getTheShape()) {
                            case GEAR -> symbolImageFilename = "GreenGear.png";
                            case MOON -> symbolImageFilename = "GreenMoon.png";
                            case PLANET -> symbolImageFilename = "GreenPlanet.png";
                            case STAR -> symbolImageFilename = "GreenStar.png";
                        }
                    } else if (Color.YELLOW.equals(currentCell.getSymbol().getColor())) {
                        switch (currentCell.getSymbol().getTheShape()) {
                            case GEAR -> symbolImageFilename = "YellowGear.png";
                            case MOON -> symbolImageFilename = "YellowMoon.png";
                            case PLANET -> symbolImageFilename = "YellowPlanet.png";
                            case STAR -> symbolImageFilename = "YellowStar.png";
                        }
                    } else if (Color.RED.equals(currentCell.getSymbol().getColor())) {
                        switch (currentCell.getSymbol().getTheShape()) {
                            case GEAR -> symbolImageFilename = "RedGear.png";
                            case MOON -> symbolImageFilename = "RedMoon.png";
                            case PLANET -> symbolImageFilename = "RedPlanet.png";
                            case STAR -> symbolImageFilename = "RedStar.png";
                        }
                    } else if (Color.BLACK.equals(currentCell.getSymbol().getColor())) {
                        symbolImageFilename = "Vortex.png";
                    }
                }

                Image symbolImage = new Image(new File(filePathRoot + "goals/" + symbolImageFilename).toURI().toString() , 35, 35, false, false);
                ImageView symbolImageView = new ImageView(symbolImage);
                stackPane.getChildren().add(symbolImageView);
                
                /* Potentiel bug */
              
                // Afficher robots
                if (this.game.getBoard().getCells()[i + 1][j + 1].getIsThereARobot()) {
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

                            if (currentCell.getIsThereARobot()) {
                                selectedRobot = currentCell.getCurrentRobot();
                                selectedRobot.setCurrentCell(currentCell);

                                move(Orientation.NORTH);
                            }

                            event.consume();
                        }
                    });
                }else{
                    stackPane = new StackPane();
                    Image goalBox = new Image(new File("src/main/resources/com/example/ricochet_robot/boards/GoalBox.png").toURI().toString() , 44, 44, false, false);
                    ImageView goalBoxView = new ImageView(goalBox);
                    stackPane.getChildren().add(goalBoxView);
                    //Coder affichage du jeton objectif dans la boîte centrale
                  


                }
                // Ajouter stackPane au board
                this.board[i][j] = stackPane;
                this.boardPane.add(stackPane, i, j);

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

    private void displayGoal(){
        String symbolImageFilename = null;
        if (Color.BLUE.equals(game.getCurrentGoal().getColor())){
            switch (game.getCurrentGoal().getTheShape()) {
                case GEAR -> symbolImageFilename = "BlueGear.png";
                case MOON -> symbolImageFilename = "BlueMoon.png";
                case PLANET -> symbolImageFilename = "BluePlanet.png";
                case STAR -> symbolImageFilename = "BlueStar.png";
            }
        } else if (Color.RED.equals(game.getCurrentGoal().getColor())) {
            switch (game.getCurrentGoal().getTheShape()) {
                case GEAR -> symbolImageFilename = "RedGear.png";
                case MOON -> symbolImageFilename = "RedMoon.png";
                case PLANET -> symbolImageFilename = "RedPlanet.png";
                case STAR -> symbolImageFilename = "RedStar.png";
            }
        } else if (Color.GREEN.equals(game.getCurrentGoal().getColor())) {
            switch (game.getCurrentGoal().getTheShape()) {
                case GEAR -> symbolImageFilename = "GreenGear.png";
                case MOON -> symbolImageFilename = "GreenMoon.png";
                case PLANET -> symbolImageFilename = "GreenPlanet.png";
                case STAR -> symbolImageFilename = "GreenStar.png";
            }
        } else if (Color.YELLOW.equals(game.getCurrentGoal().getColor())) {
            switch (game.getCurrentGoal().getTheShape()) {
                case GEAR -> symbolImageFilename = "YellowGear.png";
                case MOON -> symbolImageFilename = "YellowMoon.png";
                case PLANET -> symbolImageFilename = "YellowPlanet.png";
                case STAR -> symbolImageFilename = "YellowStar.png";
            }
        } else if (Color.BLACK.equals(game.getCurrentGoal().getColor())) {
            symbolImageFilename = "Vortex.png";
        }
        Image symbolImage = new Image(new File(filePathRoot + "goals/" + symbolImageFilename).toURI().toString() , 40, 40, false, false);
        this.currentImageGoal.setImage(symbolImage);
        this.currentImageGoal.setVisible(true);
    }

    //Sablier activé après clic sur le bouton de jeu
    private void timer(){
        isTheTimerStopped = false;
        timerText.setVisible(true);
        timerText.setText(String.valueOf(i));
        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), e ->{
            i--;
            timerText.setText(String.valueOf(i));

            if (i == 0) {
                timerText.setText("");
                i = 0;
                isTheTimerStopped = true;
                System.out.println(isTheTimerStopped);
            }
        }));
        timeline.setCycleCount(30);
        timeline.play();
    }
}
