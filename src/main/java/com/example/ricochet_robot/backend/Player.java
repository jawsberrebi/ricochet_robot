package com.example.ricochet_robot.backend;

public class Player {

    private String name;
    private int hitsNumber;
    private boolean isMyTurn;
    private int wonRounds;

    Player(String n){
        this.name = n;
        this.isMyTurn = false;
    }


}
