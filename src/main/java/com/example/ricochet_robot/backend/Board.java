package com.example.ricochet_robot.backend;

import java.util.ArrayList;
import java.util.List;

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

    //Génération d'un nouveau plateau
    public void createBoard(){
        this.cells = new Cell[17][17];
        for (int i = 1; i < 17; i++){    //On commence à initialiser la matrice de case à 1-1 ou 0-0 ? J'ai choisi 1-1
            for (int n = 1; n < 17; n++){
                this.cells[i][n] = new Cell(new Position(i, n));
            }
        }

        makeCentralBox();
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


}
