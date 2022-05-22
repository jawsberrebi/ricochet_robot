package com.example.ricochet_robot.backend;

public class Player {

    private String name;                                                                                                //Nom du joueur
    private int hitsNumber;                                                                                             //Nombre de coups faits dans une partie
    private int hitsNumberChoice;                                                                                       //Nombre de coups trouvés durant le décompte
    private boolean isMyTurn;                                                                                           //Définit si c'est au tour du joueur
    private boolean haveAlreadyPlayed;                                                                                  //Dit si le joueur a déjà joué
    private boolean iHaveTheNumberOfHitsFirst;                                                                          //Dit si le joueur est le premier a avoir trouvé le nombre de coups
    private int wonRounds;                                                                                              //Nombre de manches remportées
    private boolean roundWon;                                                                                           //Indique si la manche en cours a été gagnée par le joueur
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

    public boolean isRoundWon() {
        return roundWon;
    }

    public void setRoundWon(boolean roundWon) {
        this.roundWon = roundWon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
