package com.example.ricochet_robot.backend;

public class Position {
    private int row;
    private int column;

    public Position(int r, int c){
        this.row = r;
        this.column = c;
    }


    //Getters/Setters

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    // Methods
    public Position nextPosition(Orientation direction) {
        Position nextCellPosition = new Position(this.row, this.column);

        switch (direction) {
            case NORTH -> nextCellPosition.setColumn(nextCellPosition.getColumn() - 1);
            case SOUTH -> nextCellPosition.setColumn(nextCellPosition.getColumn() + 1);
            case EAST -> nextCellPosition.setRow(nextCellPosition.getRow() + 1);
            case WEST -> nextCellPosition.setRow(nextCellPosition.getRow() - 1);
        }

        return nextCellPosition;
    }

}
