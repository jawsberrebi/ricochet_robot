package com.example.ricochet_robot.backend;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private Position position;
    private Robot currentRobot;
    private List<Wall> walls = new ArrayList<>();
    private boolean isThereWall;
    private boolean isThereARobot;

    //Construction d'une case simple
    Cell(Position p){
        this.position = p;
        this.isThereWall = false;
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

    public List<Wall> getWalls() {
        return this.walls;
    }

    public void addWalls(Orientation orientation){
        this.walls.add(new Wall(orientation));
        this.isThereWall = true;
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


    public boolean isThereWall() {
        return this.isThereWall;
    }
    public boolean isThereARobot() {
        return isThereARobot;
    }
}
