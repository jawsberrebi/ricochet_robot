import com.example.ricochet_robot.backend.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPosition {
    @Test
    //Test d'atteinte de la position suivante (de 1 en 1)
    public void testNextPosition(){
        Position position = new Position(1,1);
        Position referencePosition = new Position(1,2);
        position = position.nextPosition(Orientation.SOUTH);
        assertEquals(referencePosition.getRow(), position.getRow());
        assertEquals(referencePosition.getColumn(), position.getColumn());
    }
}
