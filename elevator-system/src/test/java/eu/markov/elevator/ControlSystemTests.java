package eu.markov.elevator;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by Gesh on 20.06.2016.
 */
public class ControlSystemTests {

    @Test
    public void testConstructor() {
        ControlSystem underTest = new ControlSystem(4);
        assertEquals(4, underTest.elevators.length);
    }

    @Test(expected = RuntimeException.class)
    public void testConstructorWithNegativeNumberOfElevators() {
        ControlSystem underTest = new ControlSystem(-4);
    }

    @Test(expected = RuntimeException.class)
    public void testConstructorWithTooManyElevators() {
        ControlSystem underTest = new ControlSystem(42);
    }

    @Test
    public void integrationTest() {
        // Setup test
        ControlSystem underTest = new ControlSystem(3);
        underTest.update(0, Arrays.asList(new Integer[]{-1}));
        underTest.pickup(3, 1);
        Elevator.Status[] statuses = underTest.status();

        // assert initial state
        assertEquals(3, statuses.length);
        // 0 going down to -1
        assertEquals(0, statuses[0].currentFloor);
        assertEquals(Elevator.Direction.DOWN, statuses[0].currentDirection);
        assertEquals(-1, statuses[0].nextStop);
        // 0 going up to 3
        assertEquals(0, statuses[1].currentFloor);
        assertEquals(Elevator.Direction.UP, statuses[1].currentDirection);
        assertEquals(3, statuses[1].nextStop);
        // 0 going up to 3
        assertEquals(0, statuses[2].currentFloor);
        assertEquals(Elevator.Direction.UP, statuses[2].currentDirection);
        assertEquals(3, statuses[2].nextStop);

        // move the simulation 1 step
        underTest.step();
        statuses = underTest.status();
        // -1 going up to 3
        assertEquals(-1, statuses[0].currentFloor);
        assertEquals(Elevator.Direction.UP, statuses[0].currentDirection);
        assertEquals(3, statuses[0].nextStop);
        // 1 going up to 3
        assertEquals(1, statuses[1].currentFloor);
        assertEquals(Elevator.Direction.UP, statuses[1].currentDirection);
        assertEquals(3, statuses[1].nextStop);
        // 1 going up to 3
        assertEquals(1, statuses[2].currentFloor);
        assertEquals(Elevator.Direction.UP, statuses[2].currentDirection);
        assertEquals(3, statuses[2].nextStop);

        // move the simulation another 2 steps
        underTest.step();
        underTest.step();
        statuses = underTest.status();
        // 1 resting
        assertEquals(1, statuses[0].currentFloor);
        assertEquals(Elevator.Direction.NEUTRAL, statuses[0].currentDirection);
        assertEquals(1, statuses[0].nextStop);
        // 3 resting
        assertEquals(3, statuses[1].currentFloor);
        assertEquals(Elevator.Direction.NEUTRAL, statuses[1].currentDirection);
        assertEquals(3, statuses[1].nextStop);
        // 3 resting
        assertEquals(3, statuses[2].currentFloor);
        assertEquals(Elevator.Direction.NEUTRAL, statuses[2].currentDirection);
        assertEquals(3, statuses[2].nextStop);


    }
}
