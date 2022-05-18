package com.example.ricochet_robot;

import com.example.ricochet_robot.backend.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.example.ricochet_robot.backend.Cell;
import com.example.ricochet_robot.backend.Game;
import com.example.ricochet_robot.backend.Robot;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.text.BreakIterator;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class GameController implements Initializable {

    private static final Game game = new Game();
    private final Pane[][] board = new Pane[16][16];
    private final String filePathRoot = "src/main/resources/com/example/ricochet_robot/";
    public Robot selectedRobot;

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
    @FXML
    private Label scorePlayerOne;
    @FXML
    private Label scorePlayerTwo;
    @FXML
    private ImageView goalCenterImage;
    @FXML
    private Spinner<Integer> spinnerPlayerOne;
    @FXML
    private Spinner<Integer> spinnerPlayerTwo;
    @FXML
    private Label hitsNumberChoicePlayerOne;
    @FXML
    private Label hitsNumberChoicePlayerTwo;
    @FXML
    private RadioButton radioPlayerOne;
    @FXML
    private RadioButton radioPlayerTwo;
    @FXML
    private ToggleGroup radioGroup = new ToggleGroup();
    @FXML
    private Label dotPlayerOne;
    @FXML
    private Label dotPlayerTwo;
    @FXML
    private Text indicationNumberOfHits;
    private int launchTimer = 30;
    private boolean isTheTimerStopped;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Lancement du jeu
        game.play();
        scorePlayerOne.setText("");
        //Création du plateau en frontend
        Scene scene = boardPane.getScene();
        boardGeneration();
        this.spinnerPlayerOne.setVisible(false);
        this.spinnerPlayerTwo.setVisible(false);
        this.radioPlayerOne.setVisible(false);
        this.radioPlayerTwo.setVisible(false);
        this.dotPlayerOne.setVisible(false);
        this.dotPlayerTwo.setVisible(false);
        this.indicationNumberOfHits.setVisible(false);
        game.Status = Game.Status.LAUNCH_TIMER;
    }

    @FXML
    private void handleGameBtn(){
        switch (game.Status) {
            case LAUNCH_TIMER -> {
                this.spinnerPlayerOne.setVisible(true);
                this.spinnerPlayerTwo.setVisible(true);
                this.indicationNumberOfHits.setVisible(true);
                hitsNumberChoicePlayerOne.setVisible(false);
                hitsNumberChoicePlayerTwo.setVisible(false);
                this.gameBtn.setText("Confirmer le nombre de coups");
                game.Status = Game.Status.PREPARE_ROUND;
                launchSpinners();
                getFirstFinderPlayer();
                timer();
                movePlayer();
                displayGoal();
            }
            case PREPARE_ROUND -> {
                launchTimer = 0;
                this.spinnerPlayerOne.setVisible(false);
                this.spinnerPlayerTwo.setVisible(false);
                this.radioPlayerOne.setVisible(false);
                this.radioPlayerTwo.setVisible(false);
                this.indicationNumberOfHits.setVisible(false);
                if (game.getPlayerOne().getIsIHaveTheNumberOfHitsFirst()){
                    this.dotPlayerOne.setVisible(true);
                    this.dotPlayerTwo.setVisible(false);
                } else if (game.getPlayerTwo().getIsIHaveTheNumberOfHitsFirst()) {
                    this.dotPlayerTwo.setVisible(true);
                    this.dotPlayerOne.setVisible(false);
                }
                this.gameBtn.setVisible(false);
                this.hitsNumberChoicePlayerOne.setVisible(true);
                this.hitsNumberChoicePlayerTwo.setVisible(true);
                game.setFirstTurn();
                handleGameBtn();
            }
            case PLAYER_ONE_TURN -> {
                movePlayer();
            }case PLAYER_TWO_TURN -> {
                movePlayer();
            }
        }
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
                if (game.getBoard().getCells()[i + 1][j + 1].getIsThereARobot()) {
                    Robot robot = game.getBoard().getCells()[i + 1][j + 1].getCurrentRobot();

                    String filename = getRobotImageFilename(robot.getColor());

                    Image robotImage = new Image(new File("src/main/resources/com/example/ricochet_robot/robots/" + filename).toURI().toString() , 44, 44, false, false);
                    ImageView robotImageView = new ImageView(robotImage);
                    stackPane.getChildren().add(robotImageView);
                }

                if((i != 7 && i != 8) || (j != 7 && j != 8)){
                    // Rendre stackPane cliquable
                    /*
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
                     */

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

    public void move(Orientation direction) {
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
                selectedRobot.setCurrentCell(currentCell);
            }

            // Update selected robot
            selectedRobot = currentCell.getCurrentRobot();
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
        launchTimer = 30;
        isTheTimerStopped = false;
        timerText.setVisible(true);
        timerText.setText(String.valueOf(launchTimer));
        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), e ->{
            if (launchTimer <= 0) {
                timerText.setText("");
                launchTimer = 0;
                isTheTimerStopped = true;
                game.Status = Game.Status.PREPARE_ROUND;
                handleGameBtn();
            }else {
                launchTimer--;
                timerText.setText(String.valueOf(launchTimer));
            }



        }));
        timeline.setCycleCount(31);
        timeline.play();
    }

    private void movePlayer(){
        if(isTheTimerStopped){
            for (int i = 0; i < 16; i++){
                for (int j = 0; j < 16; j++){
                    this.board[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
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
                                if (game.getPlayerOne().getIsMyTurn() && game.getPlayerOne().getHitsNumber() < game.getPlayerOne().getHitsNumberChoice()){
                                    game.getPlayerOne().setHitsNumber(game.getPlayerOne().getHitsNumber() + 1);
                                    System.out.println(game.getPlayerOne().getHitsNumber());
                                    scorePlayerOne.setText(String.valueOf(game.getPlayerOne().getHitsNumber()));
                                    //game.itIsWin(selectedRobot); À tester
                                }else if(game.getPlayerTwo().getIsMyTurn() && game.getPlayerTwo().getHitsNumber() < game.getPlayerTwo().getHitsNumberChoice()){
                                    game.getPlayerTwo().setHitsNumber(game.getPlayerTwo().getHitsNumber() + 1);
                                    System.out.println(game.getPlayerTwo().getHitsNumber());
                                    scorePlayerTwo.setText(String.valueOf(game.getPlayerTwo().getHitsNumber()));
                                    //game.itIsWin(selectedRobot); À tester
                                }else{
                                    game.setNextTurn();
                                    handleGameBtn();
                                }

                            }
                            event.consume();
                        }
                    });
                }
            }
        }

    }

    private void launchSpinners(){
        SpinnerValueFactory<Integer> valueFactoryPlayerOne = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        valueFactoryPlayerOne.setValue(1);
        SpinnerValueFactory<Integer> valueFactoryPlayerTwo = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        valueFactoryPlayerTwo.setValue(1);
        spinnerPlayerOne.setValueFactory(valueFactoryPlayerOne);
        spinnerPlayerTwo.setValueFactory(valueFactoryPlayerTwo);
        game.getPlayerOne().setHitsNumberChoice(spinnerPlayerOne.getValue());
        game.getPlayerTwo().setHitsNumberChoice(spinnerPlayerTwo.getValue());
        hitsNumberChoicePlayerOne.setText(String.valueOf("Nombre de coups choisi : " + game.getPlayerOne().getHitsNumberChoice()));
        hitsNumberChoicePlayerTwo.setText(String.valueOf("Nombre de coups choisi : " + game.getPlayerTwo().getHitsNumberChoice()));
        this.spinnerPlayerOne.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                game.getPlayerOne().setHitsNumberChoice(spinnerPlayerOne.getValue());
                hitsNumberChoicePlayerOne.setText(String.valueOf("Nombre de coups choisi : " + game.getPlayerOne().getHitsNumberChoice()));
            }
        });
        this.spinnerPlayerTwo.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                game.getPlayerTwo().setHitsNumberChoice(spinnerPlayerTwo.getValue());
                hitsNumberChoicePlayerTwo.setText(String.valueOf("Nombre de coups choisi : " + game.getPlayerTwo().getHitsNumberChoice()));
            }
        });
    }

    public void getFirstFinderPlayer(){
        this.radioPlayerOne.setToggleGroup(this.radioGroup);
        this.radioPlayerTwo.setToggleGroup(this.radioGroup);
        this.radioPlayerOne.setVisible(true);
        this.radioPlayerTwo.setVisible(true);
        radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                if (radioGroup.getSelectedToggle() != null) {
                    RadioButton button = (RadioButton) radioGroup.getSelectedToggle();
                    if (button == radioPlayerOne){
                        game.getPlayerOne().setiHaveTheNumberOfHitsFirst(true);
                        game.getPlayerTwo().setiHaveTheNumberOfHitsFirst(false);
                    }else if(button == radioPlayerTwo){
                        game.getPlayerTwo().setiHaveTheNumberOfHitsFirst(true);
                        game.getPlayerOne().setiHaveTheNumberOfHitsFirst(false);
                    }
                }
            }
        });
    }

}
