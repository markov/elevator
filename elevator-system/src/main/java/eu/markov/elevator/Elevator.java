package eu.markov.elevator;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Gesh on 20.06.2016.
 */
public class Elevator {

    public static class Status {
        public final int currentFloor;
        public final Direction currentDirection;
        public final int nextPickup;

        private Status(int currentFloor, Direction currentDirection, int nextPickup) {
            this.currentFloor = currentFloor;
            this.currentDirection = currentDirection;
            this.nextPickup = nextPickup;
        }

        @Override
        public String toString() {
            if (currentDirection == Direction.RESTING) {
                return "currently resting on " + currentFloor;
            }
            return "currently on " + currentFloor + " moving " + currentDirection + " towards " + nextPickup + ".";
        }
    }

    public enum Direction {
        UP, RESTING, DOWN;

        public Direction fromInt(int direction) {
            if (direction > 0) {
                return UP;
            } else if (direction < 0) {
                return DOWN;
            } else {
                return RESTING;
            }
        }
    }

    private int currentFloor = 0;
    private Direction direction = Direction.RESTING;
    private SortedSet<Integer> pickupRequests = new TreeSet<Integer>();

    public Status status() {
        Integer nextPickup = null;
        switch (direction) {
            case RESTING:
                nextPickup = currentFloor;
                break;
            case UP:
                SortedSet<Integer> upperFloors = pickupRequests.tailSet(currentFloor);
                if (upperFloors.size() > 0) {
                    nextPickup = upperFloors.first();
                }
                break;
            case DOWN:
                SortedSet<Integer> lowerFloors = pickupRequests.headSet(currentFloor);
                if (lowerFloors.size() > 0) {
                    nextPickup = lowerFloors.last();
                }
                break;
        }

        if (nextPickup == null) {
            throw new RuntimeException("Elevator going in a direction with no more pickup floors...");
        }

        return new Status(currentFloor, direction, nextPickup);
    }

}
