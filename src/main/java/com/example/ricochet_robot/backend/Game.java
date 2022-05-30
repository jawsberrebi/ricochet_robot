package com.example.ricochet_robot.backend;

public class Game {

    public static Game context;
    private Board board;
    private Symbol currentGoal;        //Jeton objectif à atteindre
    private int goalCursor;            //Variable à incrémenter pour sélectionner l'objectif actuel
    public static Status Status;       //État du jeu
    private Player playerOne = new Player("J1");
    private Player playerTwo = new Player("J2");

    private Position[] initialRobotPositions = new Position[4];

    public Game(){
        this.board = new Board();
    }

    public void play() {
        if (Game.context != null) {
            throw new RuntimeException
                    ("Merci de ne pas exécuter plusieurs fois le jeu");
        }
        //Création d'un jeu
        Game.context = new Game();
        //Création des joueurs
        Player playerTwo = new Player("J2");
        this.board.constructBoardFromMiniBoards();
        this.board.addRobotsToBoard();                                  //Ajout des robots sur le plateau
        this.board.setGoalList();                                       //Définition de l'ordre d'apparition des jetons objectif à atteindre
        this.goalCursor = 0;                                            //Initialisation du curseur qui parcours la liste d'objectifs au fil de la partie
        this.currentGoal = this.board.getGoals().get(this.goalCursor);  //Définition du jeton objectif à atteindre
        this.board.setSymbolInGoalBox(this.currentGoal);                //Ajout du jeton objectif au centre de la boîte
    }

    //Getters/Setters
    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public void setGoalCursor(int goalCursor) {
        this.goalCursor = goalCursor;
    }

    //État du jeu
    public enum Status{
        LAUNCH_TIMER,                                                                                                   //Statut activé quand le timer est lancé et que les joueurs doivent entrer le nombre de coups pour atteindre l'objectif
        PREPARE_ROUND,                                                                                                  //Statut intermédiaire pour préparer une manche (entre LAUNCH_TIMER et PLAYER_ONE_TURN/PLAYER_TWO_TURN)
        PLAYER_ONE_TURN,                                                                                                //Tour du joueur 1
        PLAYER_TWO_TURN,                                                                                                //Tour du joueur 2
        END_ROUND,                                                                                                      //Fin d'une manche
        GAME_OVER;                                                                                                      //Fin du jeu
    }



    //Getters/Setters
    public Board getBoard() {
        return this.board;
    }

    public void setFirstTurn(){
        if(this.playerOne.getIsIHaveTheNumberOfHitsFirst()){
            this.playerOne.setIsMyTurn(true);
            this.playerTwo.setIsMyTurn(false);
            Game.Status = Status.PLAYER_ONE_TURN;
        }else if(this.playerTwo.getIsIHaveTheNumberOfHitsFirst()){
            this.playerTwo.setIsMyTurn(true);
            this.playerOne.setIsMyTurn(false);
            Game.Status = Status.PLAYER_TWO_TURN;
        }
    }

    private void replaceRobots(){
        this.board.addRobotsToBoard();
    }           //À modifier

    public void setNextTurn(){
        if (this.playerOne.getIsMyTurn()){
            this.playerOne.setIsMyTurn(false);
            this.playerOne.setHaveAlreadyPlayed(true);
            this.playerTwo.setIsMyTurn(true);
            if (!this.playerTwo.isHaveAlreadyPlayed()){
                System.out.println("Déjà joué");
                Game.Status = Status.PLAYER_TWO_TURN;
                replaceRobots();
            }else {
                Game.Status = Status.END_ROUND;
            }
        }else if(this.playerTwo.getIsMyTurn()){
            this.playerTwo.setIsMyTurn(false);
            this.playerTwo.setHaveAlreadyPlayed(true);
            this.playerOne.setIsMyTurn(true);
            if (!this.playerOne.isHaveAlreadyPlayed()){
                Game.Status = Status.PLAYER_ONE_TURN;
                replaceRobots();
            }else {
                Game.Status = Status.END_ROUND;
            }
        }
    }

    //Obtention d'un nouveau goal dans la liste ou si la fin de la liste de jetons objectifs a été atteinte, fin du jeu
    public void nextGoalOrGameOver(){
        this.goalCursor++;                                                                                              //On incrémente l'index de la liste
        if(this.goalCursor >= 17){                                                                                      //Si l'index est supérieur ou égal à 17
            Game.Status = Status.GAME_OVER;                                                                             //C'est la fin du jeu
        }else{
            this.currentGoal = this.board.getGoals().get(this.goalCursor);                                              //Sinon on définit du nouvel objectif
            Game.Status = Game.Status.LAUNCH_TIMER;
        }
    }

    //Fonction vérifiant si l'objectif a bien été atteint
    public boolean itIsWin(Robot robot){
        if((robot.getCurrentCell().getSymbol() == this.currentGoal) && (robot.getCurrentCell().getSymbol().getColor() == robot.getColor()) && (this.currentGoal.getColor() == robot.getColor())){
            if(this.playerOne.getIsMyTurn()){
                this.playerOne.addAnotherWonRound();
                this.playerOne.setRoundWon(true);
                System.out.println("WIIIIIIIIIIIIIIIIN" + this.playerOne.getWonRounds());
            }else if(this.playerTwo.getIsMyTurn()){
                this.playerTwo.addAnotherWonRound();
                this.playerTwo.setRoundWon(true);
                System.out.println("WIIIIIIIIIIIIIIIIIIN" + this.playerTwo.getWonRounds());
            }

            Game.Status = Status.END_ROUND;
            return true;
        }
        else {
            return false;
        }
    }


    //Fonction remettant à zero les coups des joueurs
    public void reinitializePlayers(){
        this.playerOne.setRoundWon(false);
        this.playerTwo.setRoundWon(false);
        this.playerOne.setHaveAlreadyPlayed(false);
        this.playerTwo.setHaveAlreadyPlayed(false);
        this.playerOne.setIsMyTurn(false);
        this.playerTwo.setIsMyTurn(false);
        this.playerOne.setHitsNumberChoice(0);
        this.playerTwo.setHitsNumberChoice(0);
        this.playerOne.setHitsNumber(0);
        this.playerTwo.setHitsNumber(0);
    }

    public boolean isValidMove(Cell currentCell, Orientation direction) {
        Position nextCellPosition = currentCell.getPosition().nextPosition(direction);
        // If at the edge of board
        if (nextCellPosition.getColumn() > 16 || nextCellPosition.getRow() > 16) {
            return false;
        }

        Cell nextCell = this.board.getCell(nextCellPosition);
        // Check if valid move
        if (currentCell.getIsThereWall()) {
            for (Wall wall : currentCell.getWalls()) {
                if (wall.getOrientation() == direction) { return false; }
            }
        }

        if (nextCell.getIsThereARobot()) { return false; }

        if (nextCell.getIsThereWall()) {
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

    public void move(Cell currentCell, Orientation direction) {
        Robot robot = currentCell.getCurrentRobot();

        // Remove robot from board
        Position currentPosition = currentCell.getPosition();
        board.getCell(currentPosition).removeRobot();

        // Add robot to board at new position
        Position newPosition = currentCell.getPosition().nextPosition(direction);
        Cell newCell = board.getCell(newPosition);
        newCell.addRobot(robot);
    }

    public Symbol getCurrentGoal() {
        return currentGoal;
    }
}
