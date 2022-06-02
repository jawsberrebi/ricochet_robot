package com.example.ricochet_robot.backend;

import javafx.scene.paint.Color;

public class Robot {
    private Color color;
    private Cell currentCell;
    private Position oldPosition;                                                                                       //Position initiale du robot lors de la génération du plateau en début de partie

    public Robot(Color c){
        this.color = c;
    }

    //Getters/Setters
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public Cell getCurrentCell() {
        return currentCell;
    }
    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }
    public Position getOldPosition() {
        return oldPosition;
    }
    public void setOldPosition(Position oldPosition) {
        this.oldPosition = oldPosition;
    }
}