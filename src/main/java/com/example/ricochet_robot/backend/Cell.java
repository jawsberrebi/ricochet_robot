package com.example.ricochet_robot.backend;

import javafx.geometry.Pos;

public class Cell {
    private Position position;
    private Wall[] walls;
    private boolean isThereWall;
    private boolean isThereARobot;

    //Construction d'une case simple
    Cell(Position p){
        this.position = p;
        this.isThereWall = false;
    }

    //Construction d'une case avec mur
    Cell(Position p, Wall[] w){
        this.position = p;
        this.walls = w;
        this.isThereWall = true;
    }


}
