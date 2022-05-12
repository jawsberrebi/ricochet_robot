package com.example.ricochet_robot.backend;

import com.example.ricochet_robot.GameController;
import javafx.geometry.Pos;

public class Game {

    private Board board;

    public Game(){
        this.board = new Board();
    }

    public void play() {
        this.board.constructBoardFromMiniBoards();
        //this.board.createBoard();
        this.board.addRobotsToBoard();
    }

    public Board getBoard() {
        return this.board;
    }

    public boolean isValidMove(Cell currentCell, Orientation direction) {
        Position nextCellPosition = currentCell.getPosition().nextPosition(direction);
        Cell nextCell = this.board.getCell(nextCellPosition);

        // Check if valid move
        if (currentCell.isThereWall()) {
            for (Wall wall : currentCell.getWalls()) {
                if (wall.getOrientation() == direction) { return false; }
            }
        }

        if (nextCell.isThereARobot()) { return false; }

        if (nextCell.isThereWall()) {
            for (Wall wall : nextCell.getWalls()) {
                switch (wall.getOrientation()) {
                    case NORTH:
                        if (direction == Orientation.SOUTH) { return false; }
                        break;
                    case SOUTH:
                        if (direction == Orientation.NORTH) { return false; }
                        break;
                    case EAST:
                        if (direction == Orientation.WEST) { return false; }
                        break;
                    case WEST:
                        if (direction == Orientation.EAST) { return false; }
                        break;
                }
            }
        }

        return true;
    }

    public void move (Cell currentCell, Orientation direction) {
        // Remove robot from board
        Robot robot = currentCell.getCurrentRobot();

        Position curentPosition = currentCell.getPosition();
        board.getCell(curentPosition).removeRobot();

        // Add robot to board at new position
        Position newPosition = currentCell.getPosition().nextPosition(direction);
        Cell newCell = board.getCell(newPosition);
        newCell.addRobot(robot);
    }
}
