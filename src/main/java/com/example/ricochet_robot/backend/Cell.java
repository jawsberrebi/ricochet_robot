package com.example.ricochet_robot.backend;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private Position position;
    private Robot currentRobot;
    private List<Wall> walls = new ArrayList<>();
    private boolean isThereWall;
    private boolean isThereARobot;
    private boolean isThereASymbol;
    private Symbol symbol;

    //Construction d'une case simple
    public Cell(Position p){
        this.position = p;
        this.isThereWall = false;
        this.isThereASymbol = false;
    }

    //Construction d'une case avec mur
    Cell(Position p, List<Wall> w){
        this.position = p;
        this.walls = w;
        this.isThereWall = true;
    }

    //Getters/Setters
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<Wall> getWalls() {
        return this.walls;
    }

    public void setWalls(List<Wall> walls) {
        this.walls = walls;
    }

    public void addWalls(Orientation orientation) {
        this.walls.add(new Wall(orientation));
        this.isThereWall = true;
    }

    public void rotateWallsRight(int numberOfRotations) {
        for (Wall wall : walls) {
            for (int n = 0; n < numberOfRotations; n++) {
                switch (wall.getOrientation()) {
                    case NORTH -> wall.setOrientation(Orientation.EAST);
                    case SOUTH -> wall.setOrientation(Orientation.WEST);
                    case EAST -> wall.setOrientation(Orientation.SOUTH);
                    case WEST -> wall.setOrientation(Orientation.NORTH);
                }
            }
        }
    }

    //Ajout d'un jeton objectif sur la case
    public void addSymbol(Symbol symbol){
        this.isThereASymbol = true;
        this.symbol = symbol;
    }

    public Robot getCurrentRobot() {
        return currentRobot;
    }

    public void addRobot(Robot robot) {
        this.currentRobot = robot;
        this.isThereARobot = true;
    }

    public void removeRobot() {
        this.currentRobot = null;
        this.isThereARobot = false;
    }



    //Getters/Setters
    public boolean getIsThereWall() {
        return walls.size() > 0;
    }



    public boolean getIsThereARobot() {
        return isThereARobot;
    }
    public boolean getIsThereASymbol() {
        return isThereASymbol;
    }
    public Symbol getSymbol() {
        return symbol;
    }


}




