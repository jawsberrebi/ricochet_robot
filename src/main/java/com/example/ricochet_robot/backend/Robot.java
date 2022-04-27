package com.example.ricochet_robot.backend;

import javafx.scene.paint.Color;

public class Robot {
    private Color color;
    private boolean isTheGoalRobot;
    private Cell currentCell;

    Robot(Color c){
        this.color = c;
    }
}
