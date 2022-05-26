package com.example.ricochet_robot.backend;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Symbol extends StackPane {

    private Color color;
    private Shape shape;
    private Position position;
    private boolean isItAGoal;

    public Symbol(Color c, Shape s, Position p){
        this.color = c;
        this.shape = s;
        this.position = p;
    }


    //Getters/Setters
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Shape getTheShape() {
        return this.shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public boolean isItAGoal() {
        return isItAGoal;
    }

    public void setItAGoal(boolean itAGoal) {
        isItAGoal = itAGoal;
    }

    //À supprimer ?
    public void display(){
        System.out.println(this.shape + " " + this.color);
    }
}
