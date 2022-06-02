package com.example.ricochet_robot.backend;

/**
 * Murs à insérer sur certaines cases
 */

public class Wall {
    private Orientation orientation;

    public Wall(Orientation o){
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
