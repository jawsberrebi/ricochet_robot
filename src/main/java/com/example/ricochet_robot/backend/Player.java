package com.example.ricochet_robot.backend;

public class Player {

    private String name;
    private int hitsNumber;
    private boolean isMyTurn;
    private int wonRounds;

    Player(String n){
        this.name = n;
        this.isMyTurn = false;
        this.hitsNumber = 0;
    }


    //Getters/Setters
    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public int getHitsNumber() {
        return hitsNumber;
    }

    public void setHitsNumber(int hitsNumber) {
        this.hitsNumber = hitsNumber;
    }
}
