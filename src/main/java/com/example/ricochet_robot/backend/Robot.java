package com.example.ricochet_robot.backend;

import javafx.scene.paint.Color;

public class Robot {
    private Color color;
    private boolean isTheGoalRobot;
    private Cell currentCell;
    private Position oldPosition;

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

    public boolean isTheGoalRobot() {
        return isTheGoalRobot;
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    public void setTheGoalRobot(boolean theGoalRobot) {
        isTheGoalRobot = theGoalRobot;
    }

    public Position getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(Position oldPosition) {
        this.oldPosition = oldPosition;
    }
}
