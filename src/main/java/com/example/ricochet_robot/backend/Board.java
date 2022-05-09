package com.example.ricochet_robot.backend;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

public class Board {

    private Cell[][] cells;     //Le plateau est une matrice 16x16 de cases --- Convention : on partirait de 1 pour le premier coeff de la matrice comme ça pas d'embrouille ?
    private List<Symbol> symbols = new ArrayList<>();
    private List<Goal> goals = new ArrayList<>();
    private Goal currentGoal;
    private Goal[][] goalBox;
    private List<Robot> robots = new ArrayList<>();
    private Robot currentRobot;
    private Robot goalRobot;
    private int goalsNumber;

    Board(){        //Test
    }

    Board(List<Symbol> s, List<Goal> g, List<Robot> r, int gN){
        this.symbols = s;
        this.goals = g;
        this.robots = r;
        this.goalsNumber = gN;
        this.goalBox = new Goal[4][4];
        this.cells = new Cell[17][17];
    }

    public Cell[][] getCells() {
        return this.cells;
    }

    public Cell getCell(Position position) {
        return this.cells[position.getRow()][position.getColumn()];
    }

    //Génération d'un nouveau plateau
    public void createBoard(){
        this.cells = new Cell[17][17];
        for (int i = 1; i < 17; i++){    //On commence à initialiser la matrice de case à 1-1 ou 0-0 ? J'ai choisi 1-1
            for (int n = 1; n < 17; n++){
                this.cells[i][n] = new Cell(new Position(i, n));
            }
        }

        makeCentralBox();
        addWallsOnBoard();
    }

    //Ajout de murs dans une case : on spécifie la position de cette case dans la matrice de cases puis on opère la changement
    public void addWallInACell(int row, int column, Orientation orientation){
        this.cells[row][column].addWalls(orientation);
    }

    //Génération de la boîte centrale avec l'objectif dedans
    public void makeCentralBox(){
        //Attribution de l'objectif pour la boîte
        this.goalBox = new Goal[4][4];
        for (int i = 0; i < 2; i++){
            for (int n = 0; n < 2; n++){
                this.goalBox[i][n] = this.currentGoal;
            }
        }

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
            this.cells[i][1].addWalls(Orientation.NORTH);
        }

        for(int i = 1; i < this.cells.length; i++){
            this.cells[1][i].addWalls(Orientation.WEST);
        }

        for(int i = 1; i < this.cells.length; i++){
            this.cells[16][i].addWalls(Orientation.EAST);
        }

        for(int i = 1; i < this.cells.length; i++){
            this.cells[i][16].addWalls(Orientation.SOUTH);
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

    public void addRobotsToBoard() {
        for (int i = 0; i < 4; i++) {
            int randomRow = (int)(Math.random() * 16) + 1;
            int randomColumn = (int)(Math.random() * 16) + 1;

            // Positionner aléatoirement les robots
            while ((randomRow == 8 || randomRow == 9) && (randomColumn == 8 || randomColumn == 9)) {
                randomRow = (int)(Math.random() * 16) + 1;
                randomColumn = (int)(Math.random() * 16) + 1;
            }

            Color robotColor = Color.RED;
            switch (i) {
                case 1 -> robotColor = Color.BLUE;
                case 2 -> robotColor = Color.GREEN;
                case 3 -> robotColor = Color.YELLOW;
            }

            Robot robot = new Robot(robotColor);
            this.cells[randomRow][randomColumn].addRobot(robot);

            System.out.println("Robot " + i + " : " + randomRow + "," + randomColumn);
        }
    }
}
