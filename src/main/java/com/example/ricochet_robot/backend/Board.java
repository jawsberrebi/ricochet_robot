package com.example.ricochet_robot.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;

public class Board {

    private Cell[][] cells;     //Le plateau est une matrice 16x16 de cases --- Convention : on partirait de 1 pour le premier coeff de la matrice comme ça pas d'embrouille ?
    private List<Symbol> symbols = new ArrayList<>();
    private List<Symbol> goals = new ArrayList<>();
    private Cell[][] goalBox;
    private List<Robot> robots = new ArrayList<>();
    private Robot currentRobot;
    private Robot goalRobot;
    private Symbol currentGoal;
    private int goalsNumber;

    Board(){        //Test

    }

    Board(List<Goal> g, int gN){
        this.symbols = new ArrayList<>();
        this.symbols.add(new Symbol(Color.GREEN, Shape.GEAR, new Position(2, 4)));
        this.symbols.add(new Symbol(Color.RED, Shape.GEAR, new Position(2, 13)));
        this.symbols.add(new Symbol(Color.YELLOW, Shape.STAR, new Position(4, 7)));
        this.symbols.add(new Symbol(Color.BLUE, Shape.STAR, new Position(4, 10)));
        this.symbols.add(new Symbol(Color.RED, Shape.MOON, new Position(5, 2)));
        this.symbols.add(new Symbol(Color.GREEN, Shape.MOON, new Position(5, 15)));
        this.symbols.add(new Symbol(Color.YELLOW, Shape.PLANET, new Position(6, 11)));
        this.symbols.add(new Symbol(Color.BLUE, Shape.PLANET, new Position(7, 5)));
        this.symbols.add(new Symbol(Color.BLACK, Shape.VORTEX, new Position(9, 13)));
        this.symbols.add(new Symbol(Color.BLUE, Shape.MOON, new Position(10, 11)));
        this.symbols.add(new Symbol(Color.BLUE, Shape.GEAR, new Position(10, 4)));
        this.symbols.add(new Symbol(Color.RED, Shape.PLANET, new Position(12, 6)));
        this.symbols.add(new Symbol(Color.YELLOW, Shape.GEAR, new Position(12, 10)));
        this.symbols.add(new Symbol(Color.GREEN, Shape.PLANET, new Position(13, 15)));
        this.symbols.add(new Symbol(Color.YELLOW, Shape.MOON, new Position(14, 2)));
        this.symbols.add(new Symbol(Color.GREEN, Shape.STAR, new Position(15, 7)));
        this.symbols.add(new Symbol(Color.RED, Shape.STAR, new Position(15, 14)));
        //this.goalBox = new Goal[4][4];
        this.cells = new Cell[17][17];
    }

    //Getters/Setters
    public Cell[][] getCells() {
        return this.cells;
    }
    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }
    public List<Symbol> getGoals() {
        return goals;
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }

    public void setCurrentGoal(Goal currentGoal) {
        this.currentGoal = currentGoal;
    }
    public Symbol getCurrentGoal() {
        return currentGoal;
    }

    //Génération d'un nouveau plateau
    public void createBoard(){
        this.cells = new Cell[17][17];
        for (int i = 1; i < 17; i++){    //On commence à initialiser la matrice de case à 1-1 ou 0-0 ? J'ai choisi 1-1
            for (int n = 1; n < 17; n++){
                this.cells[i][n] = new Cell(new Position(i, n));
            }
        }

        makeCentralBox();       //Création de la boîte centrale
        addWallsOnBoard();      //Ajout des murs
        setSymbols();
        setSymbolsOnCell();           //Placement des objectifs sur les cases

        Cell[][] celltest = new Cell[9][9];

        for(int i = 1; i <= 8; i++){
            for(int n = 1; n <= 8; n++){
                celltest[i][n] = this.cells[n][i];
            }
        }

        for(int i = 1; i <= 8; i++){
            for(int n = 1; n <= 8; n++){

            }
        }

        for(int i = 1; i <= 8; i++){
            for(int n = 1; n <= 8; n++){

            }
        }

        for(int i = 1; i <= 8; i++){
            for(int n = 1; n <= 8; n++){

            }
        }

    }

    public void setSymbols() {
        this.symbols.add(new Symbol(Color.GREEN, Shape.GEAR, new Position(2, 4)));
        this.symbols.add(new Symbol(Color.RED, Shape.GEAR, new Position(2, 13)));
        this.symbols.add(new Symbol(Color.YELLOW, Shape.STAR, new Position(4, 7)));
        this.symbols.add(new Symbol(Color.BLUE, Shape.STAR, new Position(4, 10)));
        this.symbols.add(new Symbol(Color.RED, Shape.MOON, new Position(5, 2)));
        this.symbols.add(new Symbol(Color.GREEN, Shape.MOON, new Position(5, 15)));
        this.symbols.add(new Symbol(Color.YELLOW, Shape.PLANET, new Position(6, 11)));
        this.symbols.add(new Symbol(Color.BLUE, Shape.PLANET, new Position(7, 5)));
        this.symbols.add(new Symbol(Color.BLACK, Shape.VORTEX, new Position(9, 13)));
        this.symbols.add(new Symbol(Color.BLUE, Shape.MOON, new Position(10, 11)));
        this.symbols.add(new Symbol(Color.BLUE, Shape.GEAR, new Position(10, 4)));
        this.symbols.add(new Symbol(Color.RED, Shape.PLANET, new Position(12, 6)));
        this.symbols.add(new Symbol(Color.YELLOW, Shape.GEAR, new Position(12, 10)));
        this.symbols.add(new Symbol(Color.GREEN, Shape.PLANET, new Position(13, 15)));
        this.symbols.add(new Symbol(Color.YELLOW, Shape.MOON, new Position(14, 2)));
        this.symbols.add(new Symbol(Color.GREEN, Shape.STAR, new Position(15, 7)));
        this.symbols.add(new Symbol(Color.RED, Shape.STAR, new Position(15, 14)));
    }

    //Ajout de murs dans une case : on spécifie la position de cette case dans la matrice de cases puis on opère la changement
    public void addWallInACell(int row, int column, Orientation orientation){
        this.cells[row][column].addWalls(orientation);
    }

    //Génération de la boîte centrale avec l'objectif dedans
    public void makeCentralBox(){
        //Attribution de l'objectif pour la boîte
        /*
        this.goalBox = new Cell[4][4];
        for (int i = 0; i < 2; i++){
            for (int n = 0; n < 2; n++){
                this.goalBox[i][n].addSymbol(this.currentGoal);
            }
        }

         */

        //Création de murs pour encadrer la boîte de l'objectif
        this.cells[9][7].addWalls(Orientation.EAST);
        this.cells[9][10].addWalls(Orientation.WEST);
        this.cells[10][9].addWalls(Orientation.NORTH);
        this.cells[7][9].addWalls(Orientation.SOUTH);
        this.cells[7][8].addWalls(Orientation.SOUTH);
        this.cells[8][10].addWalls(Orientation.WEST);
        this.cells[8][7].addWalls(Orientation.EAST);
        this.cells[10][8].addWalls(Orientation.NORTH);

        //Création des murs de détourage du plateau
        for(int i = 1; i < this.cells.length; i++){
            this.cells[1][i].addWalls(Orientation.NORTH);
        }

        for(int i = 1; i < this.cells.length; i++){
            this.cells[i][1].addWalls(Orientation.WEST);
        }

        for(int i = 1; i < this.cells.length; i++){
            this.cells[i][16].addWalls(Orientation.EAST);
        }

        for(int i = 1; i < this.cells.length; i++){
            this.cells[16][i].addWalls(Orientation.SOUTH);
        }
    }

    public void addWallsOnBoard(){
        this.cells[1][2].addWalls(Orientation.EAST);
        this.cells[1][10].addWalls(Orientation.EAST);
        this.cells[2][4].addWalls(Orientation.NORTH);
        this.cells[2][4].addWalls(Orientation.WEST);
        this.cells[5][2].addWalls(Orientation.WEST);
        this.cells[5][2].addWalls(Orientation.SOUTH);
        this.cells[7][5].addWalls(Orientation.NORTH);
        this.cells[7][1].addWalls(Orientation.SOUTH);
        this.cells[7][5].addWalls(Orientation.EAST);
        this.cells[10][4].addWalls(Orientation.SOUTH);
        this.cells[10][4].addWalls(Orientation.EAST);
        this.cells[11][1].addWalls(Orientation.SOUTH);
        this.cells[14][2].addWalls(Orientation.WEST);
        this.cells[14][2].addWalls(Orientation.SOUTH);
        this.cells[16][5].addWalls(Orientation.EAST);
        this.cells[16][11].addWalls(Orientation.EAST);
    }

    //Ajout des robots sur le plateau
    public void addRobotsToBoard() {
        for (int i = 0; i < 4; i++) {
            int randomRow = (int)(Math.random() * 16) + 1;
            int randomColumn = (int)(Math.random() * 16) + 1;
            // Positionner aléatoirement les robots
            while ((randomRow == 8 || randomRow == 9) && (randomColumn == 8 || randomColumn == 9)) {
                randomRow = (int)(Math.random() * 16);
                randomColumn = (int)(Math.random() * 16);
            }

            Color robotColor = Color.RED;
            switch (i) {
                case 1 -> robotColor = Color.BLUE;
                case 2 -> robotColor = Color.GREEN;
                case 3 -> robotColor = Color.YELLOW;
            }

            Robot robot = new Robot(robotColor);
            this.robots.add(robot);
            this.cells[randomRow][randomColumn].addRobot(this.robots.get(i));
        }
    }

    //Ajout des symboles sur les cases
    public void setSymbolsOnCell(){
        this.cells[2][4].addSymbol(this.symbols.get(0));
        this.cells[2][13].addSymbol(this.symbols.get(1));
        this.cells[4][7].addSymbol(this.symbols.get(2));
        this.cells[4][10].addSymbol(this.symbols.get(3));
        this.cells[5][2].addSymbol(this.symbols.get(4));
        this.cells[5][15].addSymbol(this.symbols.get(5));
        this.cells[6][11].addSymbol(this.symbols.get(6));
        this.cells[7][5].addSymbol(this.symbols.get(7));
        this.cells[9][13].addSymbol(this.symbols.get(8));
        this.cells[10][11].addSymbol(this.symbols.get(9));
        this.cells[10][4].addSymbol(this.symbols.get(10));
        this.cells[12][6].addSymbol(this.symbols.get(11));
        this.cells[12][10].addSymbol(this.symbols.get(12));
        this.cells[13][15].addSymbol(this.symbols.get(13));
        this.cells[14][2].addSymbol(this.symbols.get(14));
        this.cells[15][7].addSymbol(this.symbols.get(15));
        this.cells[15][14].addSymbol(this.symbols.get(16));
    }

    //Création de la liste de jetons objectifs pour chaque manche : les jetons sont mélangés à chaque nouveau jeu
    public void setGoalList(){
        this.goals = this.symbols;
        Collections.shuffle(this.goals);
    }

    public void setSymbolInGoalBox(Symbol symbol){
        this.cells[8][8].addSymbol(symbol);
        this.cells[9][8].addSymbol(symbol);
        this.cells[8][9].addSymbol(symbol);
        this.cells[9][9].addSymbol(symbol);
    }
}
