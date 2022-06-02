package com.example.ricochet_robot.backend;

/**
 * Position d'une cellule
 */
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

    //Méthodes
    /**
     * Déplacement de la position actuelle vers une position suivante (de 1 en 1)
     * Par exemple, si on a une position de (1, 1), si on se dirige vers le sud, notre nouvelle position sera de (2, 1)
     * @param direction Direction vers laquelle on souhaite s'engager (nord, sud, est ou ouest)
     * @return Retourne la nouvelle case sur laquelle on se trouvera après le mouvement
     */
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
