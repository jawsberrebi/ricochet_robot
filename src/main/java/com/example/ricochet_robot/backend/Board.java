package com.example.ricochet_robot.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.scene.paint.Color;

/**
 * Plateau du jeu
 */
public class Board {

    private Cell[][] cells;                 // Le plateau est une matrice 16x16 de cases --- Convention : on partirait de 1 pour le premier coeff de la matrice comme ça pas d'embrouille ?
    private Cell[][][] miniBoards;          // Un plateau est constitué de 4 petits plateaux ayant une taille de 8x8
    private final List<Symbol> symbols = new ArrayList<>();
    private List<Symbol> goals = new ArrayList<>();

    /**
     * Constructeur du plateau
     */
    public Board() {}

    /**
     * Générer la boîte centrale avec l'objectif dedans
     */
    public void makeCentralBox(){
        //Création de murs pour encadrer la boîte de l'objectif
        this.cells[9][7].addWalls(Orientation.SOUTH);
        this.cells[9][10].addWalls(Orientation.NORTH);
        this.cells[10][8].addWalls(Orientation.WEST);
        this.cells[10][9].addWalls(Orientation.WEST);
        this.cells[7][8].addWalls(Orientation.EAST);
        this.cells[7][9].addWalls(Orientation.EAST);
        this.cells[8][7].addWalls(Orientation.SOUTH);
        this.cells[8][10].addWalls(Orientation.NORTH);

        //Création des murs de détourage du plateau
        // Murs en haut du plateau
        for(int i = 1; i < this.cells.length; i++){
            this.cells[i][1].addWalls(Orientation.NORTH);
        }

        // Murs à gauche du plateau
        for(int i = 1; i < this.cells.length; i++){
            this.cells[1][i].addWalls(Orientation.WEST);
        }

        // Murs à droite du plateau
        for(int i = 1; i < this.cells.length; i++){
            this.cells[16][i].addWalls(Orientation.EAST);
        }

        // Murs en bas du plateau
        for(int i = 1; i < this.cells.length; i++){
            this.cells[i][16].addWalls(Orientation.SOUTH);
        }
    }

    /**
     * Ajouter les robots sur le plateau
     */
    public void addRobotsToBoard() {
        for (int i = 0; i < 4; i++) {
            int randomRow = (int)(Math.random() * 16) + 1;
            int randomColumn = (int)(Math.random() * 16) + 1;

            // Positionner aléatoirement les robots
            while ((randomRow == 8 || randomRow == 9) && (randomColumn == 8 || randomColumn == 9)) {
                randomRow = (int)(Math.random() * 16) + 1;
                randomColumn = (int)(Math.random() * 16) + 1;
            }

            // Déterminer la couleur du robot
            Color robotColor = Color.RED;
            switch (i) {
                case 1 -> robotColor = Color.BLUE;
                case 2 -> robotColor = Color.GREEN;
                case 3 -> robotColor = Color.YELLOW;
            }

            Robot robot = new Robot(robotColor);
            this.cells[randomRow][randomColumn].addRobot(robot);
            robot.setOldPosition(new Position(randomRow, randomColumn));
            // Record robot initial position
            //Game.context.setInitialRobotPositionAtIndex(new Position(randomRow, randomColumn), robot.getColor());

            System.out.println("Robot " + i + " : " + randomRow + "," + randomColumn);
        }
    }

    /**
     * Initialiser la liste des petits plateaux
     */
    public void initMiniBoards() {
        this.miniBoards = new Cell[4][8][8];

        for (int b = 0; b < 4; b++) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    this.miniBoards[b][i][j] = new Cell(new Position(i, j));
                }
            }
        }
    }

    /**
     * Ajouter des murs aux petits plateaux
     */
    private void addWallsToMiniBoards() {
        // Add walls to first mini board
        this.miniBoards[0][3][0].addWalls(Orientation.EAST);
        this.miniBoards[0][5][2].addWalls(Orientation.EAST);
        this.miniBoards[0][5][2].addWalls(Orientation.SOUTH);
        this.miniBoards[0][0][4].addWalls(Orientation.SOUTH);
        this.miniBoards[0][2][4].addWalls(Orientation.EAST);
        this.miniBoards[0][2][4].addWalls(Orientation.NORTH);
        this.miniBoards[0][7][5].addWalls(Orientation.SOUTH);
        this.miniBoards[0][7][5].addWalls(Orientation.WEST);
        this.miniBoards[0][1][6].addWalls(Orientation.NORTH);
        this.miniBoards[0][1][6].addWalls(Orientation.WEST);

        // Add walls to second mini board
        this.miniBoards[1][4][1].addWalls(Orientation.SOUTH);
        this.miniBoards[1][4][1].addWalls(Orientation.WEST);
        this.miniBoards[1][0][2].addWalls(Orientation.SOUTH);
        this.miniBoards[1][6][2].addWalls(Orientation.NORTH);
        this.miniBoards[1][6][2].addWalls(Orientation.WEST);
        this.miniBoards[1][7][4].addWalls(Orientation.NORTH);
        this.miniBoards[1][7][4].addWalls(Orientation.EAST);
        this.miniBoards[1][1][5].addWalls(Orientation.NORTH);
        this.miniBoards[1][1][5].addWalls(Orientation.EAST);
        this.miniBoards[1][3][6].addWalls(Orientation.EAST);
        this.miniBoards[1][3][6].addWalls(Orientation.SOUTH);
        this.miniBoards[1][4][7].addWalls(Orientation.EAST);

        // Add walls to third mini board
        this.miniBoards[2][1][0].addWalls(Orientation.EAST);
        this.miniBoards[2][5][1].addWalls(Orientation.EAST);
        this.miniBoards[2][5][1].addWalls(Orientation.SOUTH);
        this.miniBoards[2][7][1].addWalls(Orientation.SOUTH);
        this.miniBoards[2][1][3].addWalls(Orientation.NORTH);
        this.miniBoards[2][1][3].addWalls(Orientation.WEST);
        this.miniBoards[2][6][4].addWalls(Orientation.NORTH);
        this.miniBoards[2][6][4].addWalls(Orientation.EAST);
        this.miniBoards[2][4][6].addWalls(Orientation.SOUTH);
        this.miniBoards[2][4][6].addWalls(Orientation.WEST);

        // Add walls to the fourth mini board
        this.miniBoards[3][5][1].addWalls(Orientation.SOUTH);
        this.miniBoards[3][5][1].addWalls(Orientation.WEST);
        this.miniBoards[3][1][3].addWalls(Orientation.EAST);
        this.miniBoards[3][1][3].addWalls(Orientation.SOUTH);
        this.miniBoards[3][7][3].addWalls(Orientation.SOUTH);
        this.miniBoards[3][6][5].addWalls(Orientation.NORTH);
        this.miniBoards[3][6][5].addWalls(Orientation.EAST);
        this.miniBoards[3][2][6].addWalls(Orientation.NORTH);
        this.miniBoards[3][2][6].addWalls(Orientation.WEST);
        this.miniBoards[3][3][7].addWalls(Orientation.EAST);
    }

    /**
     * Faire numberOfRotations rotations de 90 degrés vers la droite de la matrice du petit plateau à l'indexe index de la liste des petits plateaux
     * @param index Indexe du petit plateau dans miniBoards
     * @param numberOfRotations Nombre de rotations de 90 degrés vers la droite du petit plateau
     */
    public void rotateMiniBoardAtIndexRight(int index, int numberOfRotations) {
        Cell[][] miniBoard = miniBoards[index];

        for (int n = 0; n < numberOfRotations; n++) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {

                    // get rotated  board cell
                    Cell tempCell = miniBoard[i][j];

                    miniBoard[i][j] = miniBoard[j][i];
                    miniBoard[j][i] = tempCell;

                    // rotate cell content
                    miniBoard[j][i].rotateWallsRight(1);
                }
            }

            // swap columns
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    Cell tempCell = miniBoard[i][j];
                    miniBoard[i][j] = miniBoard[i][8 - j - 1];
                    miniBoard[i][8 - j - 1] = tempCell;
                }
            }
        }

        // Update miniboard
        miniBoards[index] = miniBoard;
    }

    /**
     * Composer plateau à partir des petits plateau avec une rotation aléatoire et position aléatoire pour chaque petit plateau
     */
    public void constructBoardFromMiniBoards() {
        initMiniBoards();
        addWallsToMiniBoards();
        setSymbols();
        setSymbolsOnCell();           // Placement des objectifs sur les cases

        // Randomly order each board
        List<Integer> randomIndexes = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(randomIndexes);

        // Randomly rotate each board 90 degrees each time

        for (Integer index : randomIndexes) {
            int numberOfRotations = (int) (Math.random() * 4);
            rotateMiniBoardAtIndexRight(index, numberOfRotations);
        }

        // Initialize cells in board board
        this.cells = new Cell[17][17];

        for (int i = 1; i < 17; i++) {
            for (int j = 1; j < 17; j++) {
                this.cells[i][j] = new Cell(new Position(i, j));
            }

        }

        // Combine boards
        for (int r = 1; r < 17; r++) {
            for (int c = 1; c < 17; c++) {

                // Get mini board index
                int index;

                if (r <= 8) {
                    index = c <= 8 ? 0 : 1;
                } else {
                    index = c <= 8 ? 2 : 3;
                }

                // Get cell walls of cell in mini board
                Cell[][] miniBoard = miniBoards[randomIndexes.get(index)];
                Cell cell = miniBoard[r - 1 - (index < 2 ? 0 : 8)][c - 1 - (index % 2) * 8];
                List<Wall> walls = cell.getWalls();

                if (cell.getIsThereWall()) {
                    // Set cell walls in cell of mini board
                    this.cells[r][c].setWalls(walls);
                }

                if (cell.getIsThereASymbol()) {
                    // Set new cell position
                    Symbol symbol = cell.getSymbol();
                    cell.setPosition(new Position(r, c));
                    this.cells[r][c].addSymbol(symbol);
                }
            }
        }

        // Add borders
        makeCentralBox();
    }

    /**
     * Ajouter des symboles à une liste de symboles à des position prédeéfinies
     */
    public void setSymbols() {
        // Add symbols to board
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

    /**
     * Ajouter des symboles sur les cases
     */
    public void setSymbolsOnCell(){
        // Add symbols to first mini board
        miniBoards[0][5][2].addSymbol(symbols.get(0));
        miniBoards[0][2][4].addSymbol(symbols.get(1));
        miniBoards[0][7][5].addSymbol(symbols.get(2));
        miniBoards[0][1][6].addSymbol(symbols.get(3));

        // Add symbols to second mini board
        miniBoards[1][4][1].addSymbol(symbols.get(8));
        miniBoards[1][6][2].addSymbol(symbols.get(9));
        miniBoards[1][7][4].addSymbol(symbols.get(10));
        miniBoards[1][1][5].addSymbol(symbols.get(11));
        miniBoards[1][3][6].addSymbol(symbols.get(12));

        // Add symbols to third mini board
        miniBoards[2][5][1].addSymbol(symbols.get(4));
        miniBoards[2][1][3].addSymbol(symbols.get(5));
        miniBoards[2][6][4].addSymbol(symbols.get(6));
        miniBoards[2][4][6].addSymbol(symbols.get(7));

        // Add symbols to fourth mini board
        miniBoards[3][5][1].addSymbol(symbols.get(13));
        miniBoards[3][1][3].addSymbol(symbols.get(14));
        miniBoards[3][6][5].addSymbol(symbols.get(15));
        miniBoards[3][2][6].addSymbol(symbols.get(16));
    }

    /**
     * Créer la liste de jetons objectifs pour chaque manche : les jetons sont mélangés à chaque nouveau jeu
     */
    public void setGoalList(){
        this.goals = this.symbols;
        Collections.shuffle(this.goals);
    }

    //Getters/Setters

    /**
     * Getter retournant les cellules du plateau
     * @return Liste 2D des cellules du plateau
     */
    public Cell[][] getCells() {
        return this.cells;
    }

    /**
     * Getter retournant les objectifs du plateau
     * @return Liste des objectifs du plateau
     */
    public List<Symbol> getGoals() {
        return goals;
    }

    /**
     * Getter retournant la liste des petits plateaux
     * @return Liste de matrice des petits plateaux
     */
    public Cell[][][] getMiniBoards() {
        return miniBoards;
    }

    /**
     * Getter retournant une cellule du plateau à la position entrée en paramètre
     * @param position Position de la cellule à chercher
     * @return Cellule du plateau
     */
    public Cell getCell(Position position) {
        return this.cells[position.getRow()][position.getColumn()];
    }
}
