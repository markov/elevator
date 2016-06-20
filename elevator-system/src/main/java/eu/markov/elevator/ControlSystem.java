package eu.markov.elevator;

import java.util.List;

/**
 * Created by Gesh on 20.06.2016.
 */
public class ControlSystem {

    final Elevator[] elevators;

    public ControlSystem(int numberOfElevators) {
        if (numberOfElevators <= 0 || numberOfElevators > 16) {
            throw new RuntimeException("Number of elevators must be a integer between 1 and 16.");
        }
        elevators = new Elevator[numberOfElevators];
        for (int i = 0; i < numberOfElevators; i++) {
            elevators[i] = new Elevator(this);
        }
    }

    public Elevator.Status[] status() {
        Elevator.Status[] statusOfElevators = new Elevator.Status[elevators.length];
        for (int i = 0; i < elevators.length; i++) {
            statusOfElevators[i] = elevators[i].status();
        }
        return statusOfElevators;
    }


    public void update(int elevatorIndex, List<Integer> additionalDisembarkmentRequests) {
        elevators[elevatorIndex].update(additionalDisembarkmentRequests);
    }

    public void pickup(int pickupFloor, int directionInt) {
        Elevator.Direction direction = Elevator.Direction.fromInt(directionInt);
        Elevator.Request request = new Elevator.Request(direction, pickupFloor);
        for (Elevator e : elevators) {
            e.addPickupRequest(request);
        }
    }

    public void step() {
        for (Elevator e : elevators) {
            e.step();
        }
        for (Elevator e : elevators) {
            e.updateDirection();
        }
    }

    void pickupServiced(Elevator.Request r) {
        for (Elevator e : elevators) {
            e.removeRequest(r);
        }
    }

}
