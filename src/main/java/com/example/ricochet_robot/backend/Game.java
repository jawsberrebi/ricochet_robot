package com.example.ricochet_robot.backend;

public class Game {

    private Board board;

    public Game(){
        this.board = new Board();
    }

    public void play(){
        this.board.createBoard();
    }
}
