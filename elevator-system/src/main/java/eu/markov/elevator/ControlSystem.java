package eu.markov.elevator;

/**
 * Created by Gesh on 20.06.2016.
 */
public class ControlSystem {

    private final Elevator[] elevators;

    public ControlSystem(int numberOfElevators) {
        if (numberOfElevators <= 0) {
            throw new RuntimeException("Number of elevators must be a positive integer.");
        }
        elevators = new Elevator[numberOfElevators];
        for (int i = 0; i < numberOfElevators; i++) {
            elevators[i] = new Elevator();
        }
    }

    public Elevator.Status[] status() {
        Elevator.Status[] statusOfElevators = new Elevator.Status[elevators.length];
        for (int i = 0; i < elevators.length; i++) {
            statusOfElevators[i] = elevators[i].status();
        }
        return statusOfElevators;
    }
}
