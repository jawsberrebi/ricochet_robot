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
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class GameController implements Initializable {

    private static final Game game = new Game();                                                                        //Variable statique de jeu
    private final Pane[][] board = new Pane[16][16];                                                                    //Grille du plateau pour accéder aux éléments
    private final String filePathRoot = "src/main/resources/com/example/ricochet_robot/";                               //Chemin d'accès aux ressources
    public Robot selectedRobot;                                                                                         //Robot sélectionné par le joueur

    private Label timerLabel = new Label(), splitTimerLabel = new Label();
    private DoubleProperty timeSeconds = new SimpleDoubleProperty(), splitTimeSeconds = new SimpleDoubleProperty();
    private Duration time = Duration.ZERO, splitTime = Duration.ZERO;
    private Timeline timeline = new Timeline();                                                                         //Timer

    @FXML
    private GridPane boardPane;                                                                                         //Grille du plateau
    @FXML
    private Label indication;                                                                                           //Indication textuelle au joueur
    @FXML
    private ImageView currentImageGoal;                                                                                 //Image du jeton objectif actuel à atteindre
    @FXML
    private Button gameBtn;                                                                                             //Bouton principal de jeu
    @FXML
    private Label timerText;                                                                                            //Texte d'affichage du timer
    @FXML
    private Label scorePlayerOne;                                                                                       //Score du joueur 1
    @FXML
    private Label scorePlayerTwo;                                                                                       //Score du joueur 2
    @FXML
    private ImageView goalCenterImage;
    @FXML
    private Spinner<Integer> spinnerPlayerOne;                                                                          //Boîte d'entrée du nombre de coups pour le joueur 1
    @FXML
    private Spinner<Integer> spinnerPlayerTwo;                                                                          //Boîte d'entrée du nombre de coups pour le joueur 2
    @FXML
    private Label hitsNumberChoicePlayerOne;                                                                            //Texte affichant le nombre de coups choisi par le joueur 1
    @FXML
    private Label hitsNumberChoicePlayerTwo;                                                                            //Texte affichant le nombre de coups choisi par le joueur 2
    @FXML
    private RadioButton radioPlayerOne;                                                                                 //Bouton radio de sélection du joueur 1 s'il a eu le nombre de coups en premier
    @FXML
    private RadioButton radioPlayerTwo;                                                                                 //Bouton radio de sélection du joueur 2 s'il a eu le nombre de coups en premier
    @FXML
    private ToggleGroup radioGroup = new ToggleGroup();                                                                 //Groupe de boutons radio
    @FXML
    private Label dotPlayerOne;                                                                                         //Point signalant que le joueur 1 a trouvé en premier le nombre de coups
    @FXML
    private Label dotPlayerTwo;                                                                                         //Point signalant que le joueur 2 a trouvé en premier le nombre de coups
    @FXML
    private Text indicationNumberOfHits;                                                                                //Indication du nombre de coups effectués
    @FXML
    private Text stateRound;                                                                                            //Indication textuelle sur l'état de la partie
    @FXML
    private Label namePlayerOne;                                                                                        //Nom du joueur 1
    @FXML
    private Label namePlayerTwo;                                                                                        //Nom du joueur 2
    @FXML
    private Label roundsWonPlayerOne;                                                                                   //Affichage du nombre de manches gagnées joueur 1
    @FXML
    private Label roundsWonPlayerTwo;                                                                                   //Affichage du nombre de manches gagnées joueur 2
    @FXML
    private Label titlePlayerOne;                                                                                       //Titre principal du joueur 1
    @FXML
    private Label titlePlayerTwo;                                                                                       //Titre principal du joueur 2
    private int launchTimer = 30;                                                                                       //Temps de départ du compte à rebours du sablier
    private boolean isTheTimerStopped;                                                                                  //Indication sur l'état du timer (stoppé ou non)
    private boolean itIsWin;                                                                                            //Indication si le jeton objectif correspondant a été atteint
    private Map<Robot, Integer> currentColum = new HashMap<>();
    private Map<Robot, Integer> currentRow = new HashMap<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Lancement du jeu
        game.play();
        this.scorePlayerOne.setText("");
        this.scorePlayerTwo.setText("");
        this.namePlayerOne.setText(game.getPlayerOne().getName());
        this.namePlayerTwo.setText(game.getPlayerTwo().getName());
        this.roundsWonPlayerOne.setText(String.valueOf(game.getPlayerOne().getWonRounds()));
        this.roundsWonPlayerTwo.setText(String.valueOf(game.getPlayerTwo().getWonRounds()));
        itIsWin = false;
        //Création du plateau en frontend
        boardGeneration();
        Scene scene = boardPane.getScene();
        this.radioPlayerOne.setSelected(true);
        this.stateRound.setVisible(false);
        this.spinnerPlayerOne.setVisible(false);
        this.spinnerPlayerTwo.setVisible(false);
        this.radioPlayerOne.setVisible(false);
        this.radioPlayerTwo.setVisible(false);
        this.dotPlayerOne.setVisible(false);
        this.dotPlayerTwo.setVisible(false);
        this.indicationNumberOfHits.setVisible(false);
        game.Status = Game.Status.LAUNCH_TIMER;
    }

    public Label getScorePlayerOne() {
        return scorePlayerOne;
    }

    public Label getScorePlayerTwo() {
        return scorePlayerTwo;
    }

    public boolean getItIsWin(){return itIsWin;}

    @FXML
    public void handleGameBtn(){
        switch (game.Status) {
            case LAUNCH_TIMER -> {
                getPositionRobots();
                timeline.stop();                                                                                        //Réinitialisation du timer
                game.reinitializePlayers();                                                                             //Remet à 0 le nombre de coups fait précédéments, le nombre de coups choisis etc.
                //reinitializeRobot();
                this.stateRound.setText("Entrez le plus petit nombre de coups");
                this.stateRound.setVisible(true);
                this.spinnerPlayerOne.setVisible(true);
                this.spinnerPlayerTwo.setVisible(true);
                this.indicationNumberOfHits.setVisible(false);
                hitsNumberChoicePlayerOne.setVisible(false);
                hitsNumberChoicePlayerTwo.setVisible(false);
                this.scorePlayerOne.setVisible(false);
                this.scorePlayerTwo.setVisible(false);
                this.gameBtn.setText("Confirmer le nombre de coups");
                game.Status = Game.Status.PREPARE_ROUND;
                launchSpinners();                                                                                       //Affichage et lancement des spinners pour le nombre de coups
                getFirstFinderPlayer();                                                                                 //Lancement des radio boxes pour déterminer le premier joueur qui jouera
                timer();                                                                                                //Lancement du timer
                movePlayer();
                displayGoal();                                                                                          //Affichage de l'image du jeton objectif à atteindre
            }
            case PREPARE_ROUND -> {
                this.scorePlayerOne.setText("");
                this.scorePlayerTwo.setText("");

                launchTimer = 0;
                this.spinnerPlayerOne.setVisible(false);
                this.spinnerPlayerTwo.setVisible(false);
                this.radioPlayerOne.setVisible(false);
                this.radioPlayerTwo.setVisible(false);
                this.indicationNumberOfHits.setVisible(false);
                if (game.getPlayerOne().getIsIHaveTheNumberOfHitsFirst()){                                              //Affichage du point pour montrer qui jouera en premier
                    this.dotPlayerOne.setVisible(true);
                    this.dotPlayerTwo.setVisible(false);
                } else if (game.getPlayerTwo().getIsIHaveTheNumberOfHitsFirst()) {
                    this.dotPlayerTwo.setVisible(true);
                    this.dotPlayerOne.setVisible(false);
                }
                this.gameBtn.setVisible(false);
                this.hitsNumberChoicePlayerOne.setVisible(true);
                this.hitsNumberChoicePlayerTwo.setVisible(true);
                game.setFirstTurn();                                                                                    //Détermine le premier joueur qui jouera
                handleGameBtn();
            }
            case PLAYER_ONE_TURN -> {
                this.scorePlayerOne.setVisible(true);
                this.scorePlayerTwo.setVisible(true);
                this.stateRound.setVisible(true);
                this.stateRound.setText("Tour du joueur 1");
                movePlayer();                                                                                           //Possibilité de déplacer les robots par le joueur
            }case PLAYER_TWO_TURN -> {
                this.scorePlayerOne.setVisible(true);
                this.scorePlayerTwo.setVisible(true);
                this.stateRound.setVisible(true);
                this.stateRound.setText("Tour du joueur 2");
                movePlayer();                                                                                           //Possibilité de déplacer les robots par le joueur
            }case END_ROUND -> {
                this.stateRound.setVisible(true);
                System.out.println("finii");
                this.gameBtn.setText("Nouvelle manche");
                if(itIsWin && game.getPlayerOne().isRoundWon()){
                    this.stateRound.setText(game.getPlayerOne().getName() + " a gagné la manche");
                }else if(itIsWin && game.getPlayerTwo().isRoundWon()){
                    this.stateRound.setText(game.getPlayerTwo().getName() + " a gagné la manche");
                }
                else {
                    this.stateRound.setText("Personne n'a gagné");
                }
                itIsWin = false;
                game.reinitializePlayers();
                this.roundsWonPlayerOne.setText(String.valueOf(game.getPlayerOne().getWonRounds()));
                this.roundsWonPlayerTwo.setText(String.valueOf(game.getPlayerTwo().getWonRounds()));
                this.gameBtn.setVisible(true);
                game.nextGoalOrGameOver();
            }case GAME_OVER -> {
                timeline.stop();
                this.gameBtn.setVisible(false);
                this.boardPane.setVisible(false);
                this.scorePlayerOne.setVisible(false);
                this.scorePlayerTwo.setVisible(false);
                this.indication.setVisible(false);
                this.currentImageGoal.setVisible(false);
                this.titlePlayerOne.setVisible(false);
                this.titlePlayerTwo.setVisible(false);
                this.hitsNumberChoicePlayerOne.setVisible(false);
                this.hitsNumberChoicePlayerTwo.setVisible(false);
                this.dotPlayerOne.setVisible(false);
                this.dotPlayerTwo.setVisible(false);
                if(game.getPlayerOne().getWonRounds() > game.getPlayerTwo().getWonRounds()){
                    this.stateRound.setText(game.getPlayerOne().getName() + " a gagné");
                } else if (game.getPlayerOne().getWonRounds() == game.getPlayerTwo().getWonRounds()) {
                    this.stateRound.setText("Personne n'a gagné");
                }else {
                    this.stateRound.setText(game.getPlayerTwo().getName() + " a gagné");
                }
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

                Cell currentCell = game.getBoard().getCells()[i + 1][j + 1];

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
            getPositionRobots();

        }
    }

    private void getPositionRobots() {
        this.currentRow.clear();
        this.currentColum.clear();
        for (int i = 1; i < 17; i++) {
            for (int j = 1; j < 17; j++) {
                if(game.getBoard().getCells()[i][j].getIsThereARobot()){
                    Robot robot = game.getBoard().getCells()[i][j].getCurrentRobot();
                    int column = game.getBoard().getCells()[i][j].getPosition().getColumn();
                    int row = game.getBoard().getCells()[i][j].getPosition().getRow();
                    this.currentColum.put(robot, column);
                    this.currentRow.put(robot, row);
                }
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
        launchTimer = 30;
        isTheTimerStopped = false;
        timerText.setVisible(true);
        timerText.setText(String.valueOf(launchTimer));
        timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), e ->{
            if (launchTimer == 0) {
                if(!isTheTimerStopped) {
                    timerText.setText("");
                    launchTimer = 0;
                    isTheTimerStopped = true;
                    game.Status = Game.Status.PREPARE_ROUND;
                    System.out.println("ça refait");
                    handleGameBtn();
                }
            }else {
                launchTimer--;
                timerText.setText(String.valueOf(launchTimer));
            }
            e.consume();
        }));
        timeline.setCycleCount(31);
        timeline.play();
    }

    private void movePlayer(){
        if (isTheTimerStopped) {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
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
                                /*
                                if (game.getPlayerOne().getIsMyTurn() && game.getPlayerOne().getHitsNumber() < game.getPlayerOne().getHitsNumberChoice()){
                                    game.getPlayerOne().setHitsNumber(game.getPlayerOne().getHitsNumber() + 1);
                                    System.out.println(game.getPlayerOne().getHitsNumber());
                                    scorePlayerOne.setText(String.valueOf(game.getPlayerOne().getHitsNumber()));
                                    game.itIsWin(selectedRobot);
                                }else if(game.getPlayerTwo().getIsMyTurn() && game.getPlayerTwo().getHitsNumber() < game.getPlayerTwo().getHitsNumberChoice()){
                                    game.getPlayerTwo().setHitsNumber(game.getPlayerTwo().getHitsNumber() + 1);
                                    System.out.println(game.getPlayerTwo().getHitsNumber());
                                    scorePlayerTwo.setText(String.valueOf(game.getPlayerTwo().getHitsNumber()));
                                    game.itIsWin(selectedRobot);
                                }else{
                                    game.setNextTurn();
                                    Game.Status = Game.Status.END_ROUND;
                                    handleGameBtn();
                                }

                                 */
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
        game.getPlayerOne().setiHaveTheNumberOfHitsFirst(true);
        game.getPlayerTwo().setiHaveTheNumberOfHitsFirst(false);
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
                }else {
                    game.getPlayerOne().setiHaveTheNumberOfHitsFirst(true);
                    game.getPlayerTwo().setiHaveTheNumberOfHitsFirst(false);
                }
            }
        });
    }

    public void setHits(){

        if (game.getPlayerOne().getIsMyTurn() && (game.getPlayerOne().getHitsNumber() >= game.getPlayerOne().getHitsNumberChoice())){
            game.setNextTurn();
            reinitializeRobot();
            handleGameBtn();
        } else if (game.getPlayerTwo().getIsMyTurn() && (game.getPlayerTwo().getHitsNumber() >= game.getPlayerTwo().getHitsNumberChoice())) {
            game.setNextTurn();
            reinitializeRobot();
            handleGameBtn();
        }

        if (game.getPlayerOne().getIsMyTurn() && game.getPlayerOne().getHitsNumber() < game.getPlayerOne().getHitsNumberChoice()){
            game.getPlayerOne().setHitsNumber(game.getPlayerOne().getHitsNumber() + 1);
            System.out.println(game.getPlayerOne().getHitsNumber());
            scorePlayerOne.setText(String.valueOf(game.getPlayerOne().getHitsNumber()));
            itIsWin = game.itIsWin(selectedRobot);
        }else if(game.getPlayerTwo().getIsMyTurn() && game.getPlayerTwo().getHitsNumber() < game.getPlayerTwo().getHitsNumberChoice()) {
            game.getPlayerTwo().setHitsNumber(game.getPlayerTwo().getHitsNumber() + 1);
            System.out.println(game.getPlayerTwo().getHitsNumber());
            scorePlayerTwo.setText(String.valueOf(game.getPlayerTwo().getHitsNumber()));
            itIsWin = game.itIsWin(selectedRobot);
        }else {
            System.out.println("CACTUS");
        }
    }

    public boolean itIsFinished(){
        if((Game.Status != Game.Status.PLAYER_ONE_TURN &&  Game.Status != Game.Status.PLAYER_TWO_TURN) || itIsWin || (game.getPlayerOne().getHitsNumber() + game.getPlayerTwo().getHitsNumber() == game.getPlayerOne().getHitsNumberChoice() + game.getPlayerTwo().getHitsNumberChoice())){
            System.out.println("oui");
            Game.Status = Game.Status.END_ROUND;
            handleGameBtn();
            return true;
        }else {
            return false;
        }
    }

    public void reinitializeRobot(){
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if(game.getBoard().getCells()[i + 1][j + 1].getIsThereARobot()){
                    Position oldPosition = game.getBoard().getCells()[i + 1][j + 1].getPosition();
                    //removeRobotFromCell(position);
                    Robot robot = game.getBoard().getCells()[i + 1][j + 1].getCurrentRobot();
                    Position newPosition = game.getInitialRobotPositions().get(robot.getColor());
                    //System.out.println(newPosition.getRow());
                    //newPosition = new Position(1,1);
                    updateRobotDisplay(oldPosition, newPosition);

                    //String filename = getRobotImageFilename(game.getBoard().getCells()[i + 1][j + 1].getCurrentRobot().getColor());

                    // Get stackPane
                    //Image robotImage = new Image(new File("src/main/resources/com/example/ricochet_robot/robots/" + filename).toURI().toString() , 44, 44, false, false);
                    //ImageView robotImageView = new ImageView(robotImage);

                    //board[position.getRow() - 1][position.getColumn() - 1].getChildren().add(robotImageView);
                }
            }
        }
    }
}