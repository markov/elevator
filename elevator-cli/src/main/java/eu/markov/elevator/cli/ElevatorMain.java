package eu.markov.elevator.cli;

import eu.markov.elevator.ControlSystem;
import eu.markov.elevator.Elevator;
import org.apache.commons.cli.*;

/**
 * Created by Gesh on 19.06.2016.
 */
public class ElevatorMain {

    private static ControlSystem elevatorControlSystem;

    public static void main(String[] args) {

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(ElevatorOptions.cliOptions(), args);
        } catch (ParseException e) {
            System.err.println("Parsing failed.  Reason: " + e.getMessage());
            printHelp();
        }

        if (cmd == null) {
            return;
        }

        if (cmd.hasOption("h")) {
            printHelp();
        }

        if (cmd.hasOption("e")) {
            int numOfElevators = Integer.valueOf(cmd.getOptionValue("e"));
            elevatorControlSystem = new ControlSystem(numOfElevators);
        }

        if (cmd.hasOption("s")) {
            printStatus();
        }
    }

    private static void printStatus() {
        Elevator.Status[] statuses = elevatorControlSystem.status();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println("Elevator " + i + " is " + statuses[i]);
        }
    }

    private static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("mvn exec:java", ElevatorOptions.cliOptions());
    }
}
