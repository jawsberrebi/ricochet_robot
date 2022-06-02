import com.example.ricochet_robot.backend.*;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    //Test de la présence de l'ajout d'un robot sur une case
    public void testAddRobotIsTrue(){
        Position position = new Position(1, 1);
        Cell cell = new Cell(position);
        Robot robot = new Robot(Color.RED);
        cell.addRobot(robot);
        assertTrue(cell.getIsThereARobot());
    }

    @Test
    //Test de la suppression d'un robot sur une case
    public void testRemoveRobot(){
        Position position = new Position(1, 1);
        Cell cell = new Cell(position);
        Robot robot = new Robot(Color.RED);
        cell.addRobot(robot);
        cell.removeRobot();
        assertFalse(cell.getIsThereARobot());
    }

    @Test
    //Test de la rotation (dans le sens horaire à 90°) de la case pour la génération aléatoire de plateaux
    public void testRotateWallsRight(){
        Position position = new Position(2, 2);
        Cell cell = new Cell(position);
        cell.addWalls(Orientation.NORTH);
        cell.rotateWallsRight(1);
        assertEquals(Orientation.EAST, cell.getWalls().get(0).getOrientation());
    }
}
