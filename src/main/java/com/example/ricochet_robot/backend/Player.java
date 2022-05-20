package com.example.ricochet_robot.backend;

public class Player {

    private String name;
    private int hitsNumber;
    private int hitsNumberChoice;
    private boolean isMyTurn;
    private boolean haveAlreadyPlayed;
    private boolean iHaveTheNumberOfHitsFirst;
    private int wonRounds;

    Player(String n){
        this.name = n;
        this.isMyTurn = false;
        this.iHaveTheNumberOfHitsFirst = false;
        this.haveAlreadyPlayed = false;
        this.hitsNumber = 0;
        this.wonRounds = 0;
    }

    //Méthodes
    public void addAnotherWonRound(){
        this.wonRounds++;
    }


    //Getters/Setters
    public boolean getIsMyTurn() {
        return isMyTurn;
    }

    public void setIsMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public int getHitsNumber() {
        return hitsNumber;
    }

    public void setHitsNumber(int hitsNumber) {
        this.hitsNumber = hitsNumber;
    }

    public int getHitsNumberChoice() {
        return hitsNumberChoice;
    }

    public void setHitsNumberChoice(int hitsNumberChoice) {
        this.hitsNumberChoice = hitsNumberChoice;
    }

    public boolean getIsIHaveTheNumberOfHitsFirst() {
        return iHaveTheNumberOfHitsFirst;
    }

    public void setiHaveTheNumberOfHitsFirst(boolean iHaveTheNumberOfHitsFirst) {
        this.iHaveTheNumberOfHitsFirst = iHaveTheNumberOfHitsFirst;
    }

    public int getWonRounds() {
        return wonRounds;
    }

    public void setWonRounds(int wonRounds) {
        this.wonRounds = wonRounds;
    }

    public boolean isHaveAlreadyPlayed() {
        return haveAlreadyPlayed;
    }

    public void setHaveAlreadyPlayed(boolean haveAlreadyPlayed) {
        this.haveAlreadyPlayed = haveAlreadyPlayed;
    }
}
