import com.example.ricochet_robot.backend.*;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestGame {

    @Test
    //Test pour voir si setNextTurn attribue le tour suivant au joueur qui n'a pas joué (après le joueur qui a joué
    public void testSetNextTurnForTheNextPlayer(){
        Game game = new Game();
        game.play();
        Player playerOne = new Player("J1");
        Player playerTwo = new Player("J2");
        playerOne.setIsMyTurn(true);
        playerOne.setHaveAlreadyPlayed(true);
        game.setPlayerOne(playerOne);
        game.setPlayerTwo(playerTwo);
        game.setNextTurn();
        assertTrue(game.getPlayerTwo().getIsMyTurn());
    }

    @Test
    //Test pour voir si setNextTurn attribue le tour suivant au joueur qui n'a pas joué (après le joueur qui a joué
    public void testSetNextTurnForTheLastPlayer(){
        Game game = new Game();
        game.play();
        Player playerOne = new Player("J1");
        Player playerTwo = new Player("J2");
        playerOne.setIsMyTurn(true);
        playerOne.setHaveAlreadyPlayed(true);
        game.setPlayerOne(playerOne);
        game.setPlayerTwo(playerTwo);
        game.setNextTurn();
        assertFalse(game.getPlayerOne().getIsMyTurn());
    }

    @Test
    //Test pour voir si setNextTurn passe à l'état de fin de manche si les deux joueurs ont déjà joué
    public void testSetNextTurnForEnd(){
        Game game = new Game();
        game.play();
        Player playerOne = new Player("J1");
        Player playerTwo = new Player("J2");
        playerOne.setIsMyTurn(true);
        playerOne.setHaveAlreadyPlayed(true);
        playerTwo.setHaveAlreadyPlayed(true);
        game.setPlayerOne(playerOne);
        game.setPlayerTwo(playerTwo);
        game.setNextTurn();
        assertEquals(Game.Status.END_ROUND, Game.Status);
    }

    @Test
    //Test pour voir si on passe à un autre jeton objectif différent du jeton objectif précédent à chaque appel de fonction
    public void testNextGoalOrGameOverForNextGoal(){
        Game game = new Game();
        game.play();
        Symbol goalBeforeSwap = game.getCurrentGoal();
        game.nextGoalOrGameOver();
        assertNotEquals(goalBeforeSwap, game.getCurrentGoal());
    }

    @Test
    //Test pour voir si on arrive en fin du jeu si on atteint la fin de la liste de jetons objectif (il y a 17 jetons et on commence la liste à 0)
    public void testNextGoalOrGameOverForGameOver(){
        Game game = new Game();
        game.play();
        game.setGoalCursor(17);
        game.nextGoalOrGameOver();
        assertEquals(Game.Status.GAME_OVER, Game.Status);
    }

    @Test
    //Test pour voir si le programme lance le timer si la liste n'est pas terminée
    public void testNextGoalOrGameOverForLaunchTimer(){
        Game game = new Game();
        game.play();
        game.nextGoalOrGameOver();
        assertEquals(Game.Status.LAUNCH_TIMER, Game.Status);
    }

    @Test
    //Test pour vérifier si le programme change le statut pour le tour du joueur 2
    public void testSetFirstTurnForPlayerTwo(){
        Game game = new Game();
        game.play();
        Player playerOne = new Player("J1");
        Player playerTwo = new Player("J2");
        playerOne.setiHaveTheNumberOfHitsFirst(false);
        playerTwo.setiHaveTheNumberOfHitsFirst(true);
        game.setPlayerOne(playerOne);
        game.setPlayerTwo(playerTwo);
        game.setFirstTurn();
        assertEquals(Game.Status.PLAYER_TWO_TURN, Game.Status);
    }

    @Test
    //Test pour vérifier si le programme change le statut pour le tour du joueur 2
    public void testSetFirstTurnForPlayerOne(){
        Game game = new Game();
        game.play();
        Player playerOne = new Player("J1");
        Player playerTwo = new Player("J2");
        playerOne.setiHaveTheNumberOfHitsFirst(true);
        playerTwo.setiHaveTheNumberOfHitsFirst(false);
        game.setPlayerOne(playerOne);
        game.setPlayerTwo(playerTwo);
        game.setFirstTurn();
        assertEquals(Game.Status.PLAYER_ONE_TURN, Game.Status);
    }

    @Test
    //Test pour vérifier si la fonction vérifiant si le joueur 1 a gagné marche dans le cas du joueur 1
    public void testItIsWinForPlayerOne(){
        Game game = new Game();
        game.play();
        Robot robot = new Robot(Color.BLACK);
        Cell cell = new Cell(new Position(0,0));
        robot.setColor(game.getCurrentGoal().getColor());
        cell.addSymbol(game.getCurrentGoal());
        robot.setCurrentCell(cell);

        Player playerOne = new Player("J1");
        Player playerTwo = new Player("J2");
        playerOne.setIsMyTurn(true);
        playerTwo.setIsMyTurn(false);

        game.setPlayerOne(playerOne);
        game.setPlayerTwo(playerTwo);

        game.itIsWin(robot);

        assertEquals(1, game.getPlayerOne().getWonRounds());
    }

    @Test
    //Test pour vérifier si la fonction vérifiant si le joueur 2 a gagné marche dans le cas du joueur 2
    public void testItIsWinForPlayerTwo(){
        Game game = new Game();
        game.play();
        Robot robot = new Robot(Color.BLACK);
        Cell cell = new Cell(new Position(0,0));
        robot.setColor(game.getCurrentGoal().getColor());
        cell.addSymbol(game.getCurrentGoal());
        robot.setCurrentCell(cell);

        Player playerOne = new Player("J1");
        Player playerTwo = new Player("J2");
        playerOne.setIsMyTurn(false);
        playerTwo.setIsMyTurn(true);

        game.setPlayerOne(playerOne);
        game.setPlayerTwo(playerTwo);

        game.itIsWin(robot);

        assertEquals(1, game.getPlayerTwo().getWonRounds());
    }

    @Test
    //Test pour vérifier si la fonction vérifiant si le fait de gagner fait passer le jeu à la fin du tour
    public void testItIsWinForEndRound(){
        Game game = new Game();
        game.play();
        Robot robot = new Robot(Color.BLACK);
        Cell cell = new Cell(new Position(0,0));
        robot.setColor(game.getCurrentGoal().getColor());
        cell.addSymbol(game.getCurrentGoal());
        robot.setCurrentCell(cell);

        Player playerOne = new Player("J1");
        Player playerTwo = new Player("J2");
        playerOne.setIsMyTurn(true);
        playerTwo.setIsMyTurn(false);

        game.setPlayerOne(playerOne);
        game.setPlayerTwo(playerTwo);

        game.itIsWin(robot);

        assertEquals(Game.Status.END_ROUND, Game.Status);
    }

    @Test
    //Test pour vérifier si la fonction vérifiant si le fait de gagner retourne true si on gagne
    public void testItIsWinForTrue(){
        Game game = new Game();
        game.play();
        Robot robot = new Robot(Color.BLACK);
        Cell cell = new Cell(new Position(0,0));
        robot.setColor(game.getCurrentGoal().getColor());
        cell.addSymbol(game.getCurrentGoal());
        robot.setCurrentCell(cell);

        Player playerOne = new Player("J1");
        Player playerTwo = new Player("J2");
        playerOne.setIsMyTurn(true);
        playerTwo.setIsMyTurn(false);

        game.setPlayerOne(playerOne);
        game.setPlayerTwo(playerTwo);
        assertTrue(game.itIsWin(robot));
    }

    @Test
    //Test pour vérifier si la fonction vérifiant si le fait de gagner retourne false si le robot n'est pas sur l'objectif qui lui ferait gagner
    public void testItIsWinForFalse(){
        Game game = new Game();
        game.play();
        Robot robot = new Robot(Color.BLACK);
        Cell cell = new Cell(new Position(0,0));
        robot.setCurrentCell(cell);

        Player playerOne = new Player("J1");
        Player playerTwo = new Player("J2");
        playerOne.setIsMyTurn(true);
        playerTwo.setIsMyTurn(false);

        game.setPlayerOne(playerOne);
        game.setPlayerTwo(playerTwo);
        assertFalse(game.itIsWin(robot));
    }
}