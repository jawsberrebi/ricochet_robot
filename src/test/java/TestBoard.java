import com.example.ricochet_robot.backend.Board;
import com.example.ricochet_robot.backend.Cell;
import com.example.ricochet_robot.backend.Orientation;
import com.example.ricochet_robot.backend.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class TestBoard {

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
