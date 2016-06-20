package eu.markov.elevator;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Gesh on 20.06.2016.
 */
public class Elevator {

    public enum Direction {
        UP(1), NEUTRAL(0), DOWN(-1);

        int floorsPerStep;

        Direction(int floorsPerStep) {
            this.floorsPerStep = floorsPerStep;
        }

        public static Direction fromInt(int direction) {
            if (direction > 0) {
                return UP;
            } else if (direction < 0) {
                return DOWN;
            } else {
                return NEUTRAL;
            }
        }

        public Direction reveresed() {
            switch (this) {
                case UP:
                    return DOWN;
                case DOWN:
                    return UP;
            }
            return NEUTRAL;
        }
    }

    public static class Status {
        public final int currentFloor;
        public final Direction currentDirection;
        public final int nextStop;

        private Status(int currentFloor, Direction currentDirection, int nextStop) {
            this.currentFloor = currentFloor;
            this.currentDirection = currentDirection;
            this.nextStop = nextStop;
        }

        @Override
        public String toString() {
            if (currentDirection == Direction.NEUTRAL) {
                return "currently resting on " + currentFloor;
            }
            return "currently on " + currentFloor + " moving " + currentDirection + " towards " + nextStop + ".";
        }
    }

    public static class Request implements Comparable<Request> {
        public final Direction direction;
        public final int floor;

        public Request(Direction direction, int floor) {
            this.direction = direction;
            this.floor = floor;
        }

        /**
         * Ignores direction since requests with different directions are stored in different set.
         *
         * @see Comparable#compareTo(Object)
         */
        public int compareTo(Request o) {
            return this.floor - o.floor;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Request request = (Request) o;

            if (floor != request.floor) return false;
            return direction == request.direction;

        }

        @Override
        public int hashCode() {
            int result = direction != null ? direction.hashCode() : 0;
            result = 31 * result + floor;
            return result;
        }
    }

    private final ControlSystem owner;
    private int currentFloor = 0;
    private Direction direction = Direction.NEUTRAL;
    private SortedSet<Request> upwardRequests = new TreeSet<Request>();
    private SortedSet<Request> downwardRequests = new TreeSet<Request>();
    private SortedSet<Request> disembarkmentRequests = new TreeSet<Request>();


    Elevator(ControlSystem owner) {
        this.owner = owner;
    }

    public Status status() {
        return new Status(currentFloor, direction, nextStop().floor);
    }

    public void update(List<Integer> requests) {
        if (requests == null) {
            return;
        }

        Integer highestRequestedFloor = null;
        for (int floor : requests) {
            addRequest(new Request(Direction.NEUTRAL, floor));
            if (highestRequestedFloor == null || highestRequestedFloor < floor) {
                highestRequestedFloor = floor;
            }
        }
        if (direction == Direction.NEUTRAL && highestRequestedFloor != null) {
            direction = Direction.fromInt(highestRequestedFloor - currentFloor);
        }
    }

    public void addPickupRequest(Request r) {
        addRequest(r);
        if (direction == Direction.NEUTRAL) {
            this.direction = Direction.fromInt(r.floor - currentFloor);
        }
    }

    void step() {
        if (this.direction == Direction.NEUTRAL) {
            return;
        }
        this.currentFloor += this.direction.floorsPerStep;
        this.disembarkmentRequests.remove(new Request(Direction.NEUTRAL, this.currentFloor));
        Request potentialPickupRequest = new Request(this.direction, this.currentFloor);
        if (this.upwardRequests.contains(potentialPickupRequest)
                || this.downwardRequests.contains(potentialPickupRequest)) {
            owner.pickupServiced(potentialPickupRequest);
        }
    }

    void updateDirection() {
        if (nextStop() == null) {
            this.direction = this.direction.reveresed();
            if (nextStop() == null) {
                this.direction = Direction.NEUTRAL;
            }
        }
    }

    void removeRequest(Request r) {
        disembarkmentRequests.remove(r);
        upwardRequests.remove(r);
        downwardRequests.remove(r);
    }

    private void addRequest(Request r) {
        if (r.floor == currentFloor) {
            return;
        }

        switch (r.direction) {
            case NEUTRAL:
                disembarkmentRequests.add(r);
                break;
            case UP:
                upwardRequests.add(r);
                break;
            case DOWN:
                downwardRequests.add(r);
                break;
        }
    }

    private Request nextStop() {
        Request currentStop = new Request(Direction.NEUTRAL, currentFloor);
        Request nextStop = null;
        switch (direction) {
            case NEUTRAL:
                nextStop = currentStop;
                break;
            case UP:
                SortedSet<Request> potentialUpperStops = new TreeSet<Request>();
                potentialUpperStops.addAll(disembarkmentRequests);
                potentialUpperStops.addAll(upwardRequests);
                potentialUpperStops = potentialUpperStops.tailSet(currentStop);
                if (potentialUpperStops.size() > 0) {
                    nextStop = potentialUpperStops.first();
                }
                break;
            case DOWN:
                SortedSet<Request> potentialLowerStops = new TreeSet<Request>();
                potentialLowerStops.addAll(disembarkmentRequests);
                potentialLowerStops.addAll(downwardRequests);
                potentialLowerStops = potentialLowerStops.headSet(currentStop);
                if (potentialLowerStops.size() > 0) {
                    nextStop = potentialLowerStops.last();
                }
                break;
        }
        return nextStop;
    }
}
