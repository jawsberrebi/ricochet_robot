import com.example.ricochet_robot.backend.*;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCell {
    @Test
    //Test de l'ajout du symbole sur une case
    public void testAddSymbol(){
        Position position = new Position(1, 1);
        Cell cell = new Cell(position);
        Symbol symbol = new Symbol(Color.RED, Shape.GEAR, position);
        cell.addSymbol(symbol);
        assertTrue(cell.getIsThereASymbol());
    }

    @Test
    //Test de la présence de l'ajout d'un mur sur une case
    public void testAddWallsIsTrue(){
        Position position = new Position(1, 1);
        Cell cell = new Cell(position);
        cell.addWalls(Orientation.NORTH);
        assertTrue(cell.getIsThereWall());
    }
}
