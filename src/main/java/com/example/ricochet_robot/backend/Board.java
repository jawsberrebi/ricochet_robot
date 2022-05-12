package com.example.ricochet_robot.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.scene.paint.Color;

public class Board {

    private Cell[][] cells;     //Le plateau est une matrice 16x16 de cases --- Convention : on partirait de 1 pour le premier coeff de la matrice comme ça pas d'embrouille ?
    private Cell[][][] miniBoards;
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
        this.miniBoards = new Cell[4][8][8];
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

    private void initMiniBoards() {
        this.miniBoards = new Cell[4][16][16];

        for (int b = 0; b < 4; b++) {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    this.miniBoards[b][i][j] = new Cell(new Position(i, j));
                }
            }
        }
    }

    private void addWallsToMiniBoards() {
        this.miniBoards[0][2][2].addWalls(Orientation.WEST);
        this.miniBoards[0][2][2].addWalls(Orientation.NORTH);
        this.miniBoards[0][2][2].addWalls(Orientation.SOUTH);

    }

    private void rotateMiniBoardRight(int index, int numberOfRotations) {
        Cell[][] miniBoard = miniBoards[index];
        Cell[][] rotatedMiniBoard = new Cell[16][16];

        for (int n = 0; n < numberOfRotations; n++) {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    // get rotated  board cell
                    Cell cell = miniBoard[16 - j - 1][i];

                    // rotate cell
                    cell.rotateWallsRight(1);

                    // update rotated board
                    rotatedMiniBoard[i][j] = miniBoard[j][i];
                }
            }
        }

        miniBoards[index] = rotatedMiniBoard;
    }

    public void constructBoardFromMiniBoards() {
        initMiniBoards();
        addWallsToMiniBoards();

        // Randomly order each board
        List<Integer> indexes = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(indexes);

        // Randomly rotate each board
        for (Integer index : indexes) {
            int numberOfRotations = (int) (Math.random() * 4);
            rotateMiniBoardRight(index, numberOfRotations);
        }

        // Combine miniboards
        this.cells = new Cell[17][17];

        for (int row = 0; row < 8; row++) {
            Cell[] newRow = new Cell[17];
            for (int i = 1; i < miniBoards[0].length; i++) {
                newRow[i] = miniBoards[indexes.get(0)][row][i - 1];
            }
            for (int i = miniBoards.length; i < newRow.length; i++) {
                newRow[i] = miniBoards[indexes.get(1)][row][i - 1];
            }
            this.cells[row + 1] = newRow;
        }

        for (int row = 8; row < 16; row++) {
            Cell[] newRow = new Cell[17];
            for (int i = 1; i < miniBoards[0].length; i++) {
                newRow[i] = miniBoards[indexes.get(2)][row][i - 1];
            }
            for (int i = miniBoards.length; i < newRow.length; i++) {
                newRow[i] = miniBoards[indexes.get(3)][row][i - 1];
            }
            this.cells[row + 1] = newRow;
        }

        // Construct board
        for (int i = 1; i < 17; i++) {
            for (int j = 1; j < 17; j++) {
                this.cells[i][j].setPosition(new Position(i, j));
            }
        }

        // Add borders
        makeCentralBox();
        addWallsOnBoard();
    }
}
