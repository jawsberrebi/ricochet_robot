package com.example.ricochet_robot.backend;

public class Game {

    public static Game context;
    private Board board;
    private Symbol currentGoal;        //Jeton objectif à atteindre
    private int goalCursor;            //Variable à incrémenter pour sélectionner l'objectif actuel
    public static Status Status;       //État du jeu

    public Game(){
        this.board = new Board();
    }

    public void play() {
        if (Game.context != null) {
            throw new RuntimeException
                    ("Merci de ne pas exécuter plusieurs fois le jeu");
        }
        Game.context = new Game();
        this.board.createBoard();                                       //Création du plateau
        this.board.addRobotsToBoard();                                  //Ajout des robots sur le plateau
        this.board.setGoalList();                                       //Définition de l'ordre d'apparition des jetons objectif à atteindre
        this.goalCursor = 0;                                            //Initialisation du curseur qui parcours la liste d'objectifs au fil de la partie
        this.currentGoal = this.board.getGoals().get(this.goalCursor);  //Définition du jeton objectif à atteindre
        this.board.setSymbolInGoalBox(this.currentGoal);                //Ajout du jeton objectif au centre de la boîte
        Status = Status.START_ROUND;

    }

    //État du jeu
    public enum Status{
        START_ROUND,                    //Nouvelle manche : nouveau jeton objectif, début de la manche
        LAUNCH_TIMER                    //Lancement du timer par un joueur : le joueur qui a lancé le timer peut faire bouger les robots
    }

    //Getters/Setters
    public Board getBoard() {
        return this.board;
    }
    public Symbol getCurrentGoal() {
        return currentGoal;
    }
}
