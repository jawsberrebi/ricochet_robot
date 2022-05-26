import com.example.ricochet_robot.backend.Board;
import com.example.ricochet_robot.backend.Game;
import com.example.ricochet_robot.backend.Player;
import com.example.ricochet_robot.backend.Symbol;
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
}
