package com.example.ricochet_robot;

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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameController implements Initializable {

    private static final Game game = new Game();
    private final Pane[][] board = new Pane[16][16];
    private final String filePathRoot = "src/main/resources/com/example/ricochet_robot/";

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

    private void boardGeneration(){
        Image cellImage = new Image(new File(filePathRoot + "boards/Cell.PNG").toURI().toString() , 44, 44, false, false);
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                StackPane stackPane = new StackPane();

                ImageView cellImageView = new ImageView(cellImage);
                stackPane.getChildren().add(cellImageView);

                Cell currentCell = game.getBoard().getCells()[j + 1][i + 1];

                //Ajout des murs, s'il y en a
                String wallImageFilename = null;
                if (currentCell.isThereWall()) {
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

                //Ajout des symboles, s'il y en a
                String symbolImageFilename = null;
                if(currentCell.isThereASymbol()){
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

                //Placement des robots
                if (game.getBoard().getCells()[j + 1][i + 1].isThereARobot()) {
                    Robot robot = game.getBoard().getCells()[j + 1][i + 1].getCurrentRobot();
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

                //S'il s'agit pas de la boîte centrale, on posera les tuiles classiques avec ce qu'elles contiennent (mur, robot, jeton objectif) s'il y en a
                if((i != 8 && i != 7) || (j != 7 && j != 8)){
                    this.board[i][j] = stackPane;
                    this.boardPane.add(stackPane, i, j);
                //Sinon on posera une tuile grise avec l'objectif en cours
                }else{
                    stackPane = new StackPane();
                    Image goalBox = new Image(new File("src/main/resources/com/example/ricochet_robot/boards/GoalBox.png").toURI().toString() , 44, 44, false, false);
                    ImageView goalBoxView = new ImageView(goalBox);
                    stackPane.getChildren().add(goalBoxView);
                    //Coder affichage du jeton objectif dans la boîte centrale
                    this.board[i][j] = stackPane;
                    this.boardPane.add(stackPane, i, j);
                }
            }
        }
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

    /*
    private void updateImage(int row, int col){
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(new ImageView(new Image(new File("src/main/resources/com/example/ricochet_robot/robots/robotYellow.png").toURI().toString() , 44, 44, false, false)));
        this.boardPane.getChildren().remove(row*col);
        this.boardPane.add(stackPane, col-1, row-1);
    }

     */
}
