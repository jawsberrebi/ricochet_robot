package com.example.ricochet_robot.backend;

public class Wall {
    private Orientation orientation;

    Wall(Orientation o){
        this.orientation = o;
    }

    //Getters/Setters
    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
}
