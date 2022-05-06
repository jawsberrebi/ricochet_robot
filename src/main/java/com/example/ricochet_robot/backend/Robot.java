package com.example.ricochet_robot.backend;

import javafx.scene.paint.Color;

public class Robot {
    private Color color;
    private boolean isTheGoalRobot;
    private Cell currentCell;

    Robot(Color c){
        this.color = c;
    }

    //Getters/Setters

    public Color getColor() {
        return color;
    }

    public boolean isTheGoalRobot() {
        return isTheGoalRobot;
    }

    public void setTheGoalRobot(boolean theGoalRobot) {
        isTheGoalRobot = theGoalRobot;
    }

    //Méthodes
    public void move(Orientation orientation, Cell[][] cellLine) {
        //On étudie les différents cas de mouvement du robots, avec la rencontre des murs
        switch (orientation) {
            case NORTH:
                for (int i = this.currentCell.getPosition().getColumn(); i < cellLine.length; i++) {
                    if (cellLine[this.currentCell.getPosition().getRow()][i].isThereWall()) {
                        Orientation orientationWall = Orientation.WEST;    //Vérification de la présence de mur à chaque case
                        for (int u = 0; u < cellLine[this.currentCell.getPosition().getRow()][i].getWalls().size(); u++) {
                            if ((cellLine[this.currentCell.getPosition().getRow()][i].getWalls().get(i).getOrientation() == Orientation.NORTH) || (cellLine[this.currentCell.getPosition().getRow()][i].getWalls().get(i).getOrientation() == Orientation.SOUTH)) {
                                orientationWall = cellLine[this.currentCell.getPosition().getRow()][i].getWalls().get(i).getOrientation();      //Caractérisation de l'orientation du mur
                            }
                        }
                        switch (orientationWall) {
                            case NORTH:
                                this.currentCell = cellLine[this.currentCell.getPosition().getRow()][i];
                                break;
                            case SOUTH:
                                break;  //Le robot est stoppé dans la case qui précède la case avec le mur
                        }
                    } else {
                        this.currentCell = cellLine[this.currentCell.getPosition().getRow()][i]; //S'il y a pas de mur, on déplace le robot vers le nord
                    }
                }
                break;

            case SOUTH:
                for (int i = this.currentCell.getPosition().getColumn(); i > cellLine.length; i--) {
                    if (cellLine[this.currentCell.getPosition().getRow()][i].isThereWall()) {
                        Orientation orientationWall = Orientation.WEST;    //Vérification de la présence de mur à chaque case
                        for (int u = 0; u < cellLine[this.currentCell.getPosition().getRow()][i].getWalls().size(); u++) {
                            if ((cellLine[this.currentCell.getPosition().getRow()][i].getWalls().get(i).getOrientation() == Orientation.NORTH) || (cellLine[this.currentCell.getPosition().getRow()][i].getWalls().get(i).getOrientation() == Orientation.SOUTH)) {
                                orientationWall = cellLine[this.currentCell.getPosition().getRow()][i].getWalls().get(i).getOrientation();      //Caractérisation de l'orientation du mur
                            }
                        }
                        switch (orientationWall) {
                            case NORTH:
                                break;
                            case SOUTH:
                                this.currentCell = cellLine[this.currentCell.getPosition().getRow()][i];
                                break;  //Le robot est stoppé dans la case qui précède la case avec le mur
                        }
                    } else {
                        this.currentCell = cellLine[this.currentCell.getPosition().getRow()][i]; //S'il y a pas de mur, on déplace le robot vers le nord
                    }
                }
                break;

            case EAST:
                for (int i = this.currentCell.getPosition().getColumn(); i < cellLine.length; i++) {
                    if (cellLine[i][this.currentCell.getPosition().getRow()].isThereWall()) {
                        Orientation orientationWall = Orientation.NORTH;    //Vérification de la présence de mur à chaque case
                        for (int u = 0; u < cellLine[this.currentCell.getPosition().getRow()][i].getWalls().size(); u++) {
                            if ((cellLine[i][this.currentCell.getPosition().getRow()].getWalls().get(i).getOrientation() == Orientation.NORTH) || (cellLine[this.currentCell.getPosition().getRow()][i].getWalls().get(i).getOrientation() == Orientation.SOUTH)) {
                                orientationWall = cellLine[i][this.currentCell.getPosition().getRow()].getWalls().get(i).getOrientation();      //Caractérisation de l'orientation du mur
                            }
                        }
                        switch (orientationWall) {
                            case EAST:
                                this.currentCell = cellLine[i][this.currentCell.getPosition().getRow()];
                                break;
                            case WEST:
                                break;  //Le robot est stoppé dans la case qui précède la case avec le mur
                        }
                    } else {
                        this.currentCell = cellLine[i][this.currentCell.getPosition().getRow()]; //S'il y a pas de mur, on déplace le robot vers le nord
                    }
                }
                break;

            case WEST:
                for (int i = this.currentCell.getPosition().getColumn(); i > cellLine.length; i--) {
                    if (cellLine[i][this.currentCell.getPosition().getRow()].isThereWall()) {
                        Orientation orientationWall = Orientation.NORTH;    //Vérification de la présence de mur à chaque case
                        for (int u = 0; u < cellLine[this.currentCell.getPosition().getRow()][i].getWalls().size(); u++) {
                            if ((cellLine[i][this.currentCell.getPosition().getRow()].getWalls().get(i).getOrientation() == Orientation.WEST) || (cellLine[this.currentCell.getPosition().getRow()][i].getWalls().get(i).getOrientation() == Orientation.WEST)) {
                                orientationWall = cellLine[i][this.currentCell.getPosition().getRow()].getWalls().get(i).getOrientation();      //Caractérisatoon de l'orientation du mur
                            }
                        }
                        switch (orientationWall) {
                            case EAST:
                                break;
                            case WEST:
                                this.currentCell = cellLine[i][this.currentCell.getPosition().getRow()];
                                break;  //Le robot est stoppé dans la case qui précède la case avec le mur
                        }
                    } else {
                        this.currentCell = cellLine[i][this.currentCell.getPosition().getRow()]; //S'il y a pas de mur, on déplace le robot vers le nord
                    }
                }
                break;
        }
    }

    public Color getColor() {
        return color;
    }
}
