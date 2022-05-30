import com.example.ricochet_robot.backend.Board;
import com.example.ricochet_robot.backend.Cell;
import com.example.ricochet_robot.backend.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class TestBoard {

    @Test
    public void testCreateBoard(){
        Board board = new Board();
        board.createBoard();
        Cell[][] cells = new Cell[17][17];

        for (int i = 1; i < 17; i++){
            for (int n = 1; n < 17; n++){
                cells[i][n] = new Cell(new Position(i, n));
            }
        }

        assertEquals(cells, board.getCells());
    }
}
