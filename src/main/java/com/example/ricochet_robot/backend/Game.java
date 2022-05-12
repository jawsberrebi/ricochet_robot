package com.example.ricochet_robot.backend;

import com.example.ricochet_robot.GameController;
import javafx.geometry.Pos;

public class Game {

    public static Game context;
    private Board board;
    private Symbol currentGoal;        //Jeton objectif à atteindre
    private int goalCursor;            //Variable à incrémenter pour sélectionner l'objectif actuel
    public static Status Status;       //État du jeu
    public Player playerOne = new Player("J1");
    public Player playerTwo = new Player("J2");;

    public Game(){
        this.board = new Board();
    }

    public void play() {
        if (Game.context != null) {
            throw new RuntimeException
                    ("Merci de ne pas exécuter plusieurs fois le jeu");
        }
        //this.board.constructBoardFromMiniBoards();
        //Création d'un jeu
        Game.context = new Game();
        //Création des joueurs
        Player playerTwo = new Player("J2");
        this.board.createBoard();                                       //Création du plateau
        this.board.addRobotsToBoard();                                  //Ajout des robots sur le plateau
        this.board.setGoalList();                                       //Définition de l'ordre d'apparition des jetons objectif à atteindre
        this.goalCursor = 0;                                            //Initialisation du curseur qui parcours la liste d'objectifs au fil de la partie
        this.currentGoal = this.board.getGoals().get(this.goalCursor);  //Définition du jeton objectif à atteindre
        this.board.setSymbolInGoalBox(this.currentGoal);                //Ajout du jeton objectif au centre de la boîte

    }

    //État du jeu
    public enum Status{
        LAUNCH_TIMER,
        PLAYTIME;
    }

    //Méthodes

    //Getters/Setters
    public Board getBoard() {
        return this.board;
    }

    public boolean isValidMove(Cell currentCell, Orientation direction) {
        Position nextCellPosition = currentCell.getPosition().nextPosition(direction);
        Cell nextCell = this.board.getCell(nextCellPosition);

        // Check if valid move
        if (currentCell.getIsThereWall()) {
            for (Wall wall : currentCell.getWalls()) {
                if (wall.getOrientation() == direction) { return false; }
            }
        }

        if (nextCell.getIsThereARobot()) { return false; }

        if (nextCell.getIsThereWall()) {
            for (Wall wall : nextCell.getWalls()) {
                switch (wall.getOrientation()) {
                    case NORTH:
                        if (direction == Orientation.SOUTH) { return false; }
                        break;
                    case SOUTH:
                        if (direction == Orientation.NORTH) { return false; }
                        break;
                    case EAST:
                        if (direction == Orientation.WEST) { return false; }
                        break;
                    case WEST:
                        if (direction == Orientation.EAST) { return false; }
                        break;
                }
            }
        }

        return true;
    }

    public void move (Cell currentCell, Orientation direction) {
        // Remove robot from board
        Robot robot = currentCell.getCurrentRobot();

        Position curentPosition = currentCell.getPosition();
        board.getCell(curentPosition).removeRobot();

        // Add robot to board at new position
        Position newPosition = currentCell.getPosition().nextPosition(direction);
        Cell newCell = board.getCell(newPosition);
        newCell.addRobot(robot);
    }
  
    public Symbol getCurrentGoal() {
        return currentGoal;
    }
}
