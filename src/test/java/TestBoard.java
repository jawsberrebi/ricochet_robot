import com.example.ricochet_robot.backend.Board;
import com.example.ricochet_robot.backend.Cell;
import com.example.ricochet_robot.backend.Orientation;
import com.example.ricochet_robot.backend.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class TestBoard {

    @Test
    // Test de la rotation d'un petit plateau
    public void testMiniBoardRotation() {
        Board board = new Board();
        board.initMiniBoards();

        // Add walls to test
        board.getMiniBoards()[0][1][1].addWalls(Orientation.EAST);
        board.rotateMiniBoardAtIndexRight(0, 4);

        assertEquals(board.getMiniBoards()[0][1][1].getWalls().get(0).getOrientation(), Orientation.EAST);
    }

    @Test
    // Test de chercher une cellule avec les coordonnées
    public void testGetCell() {
        Board board = new Board();
        board.constructBoardFromMiniBoards();

        Position testPosition = new Position(5, 5);

        assertEquals(board.getCell(testPosition).getPosition().getRow(), testPosition.getRow());
        assertEquals(board.getCell(testPosition).getPosition().getColumn(), testPosition.getColumn());
    }
}
