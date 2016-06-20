package eu.markov.elevator.cli;

import eu.markov.elevator.ControlSystem;
import eu.markov.elevator.Elevator;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gesh on 19.06.2016.
 */
public class ElevatorMain {

    private static ControlSystem elevatorControlSystem;
    private static CommandLineParser parser = new DefaultParser();

    public static void main(String[] args) throws IOException {

        CommandLine cmd = parseInput(args);

        if (cmd == null) {
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));
        boolean keepReading = handleInput(cmd);
        while (keepReading) {
            System.out.println("What next (-h for help):");
            String line = reader.readLine();
            args = line.split("\\s");
            cmd = parseInput(args);
            keepReading = handleInput(cmd);
        }

    }

    private static CommandLine parseInput(String[] args) {
        try {
            return parser.parse(ElevatorOptions.cliOptions(), args);
        } catch (ParseException e) {
            printHelp();
            throw new RuntimeException(e);
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

    private static boolean handleInput(CommandLine cmd) {
        Option[] options = cmd.getOptions();
        for (Option o : options) {
            if (!handleOption(o)) {
                return false;
            }
        }
        return true;
    }


    private static boolean handleOption(Option option) {
        switch (option.getId()) {
            case 'q':
                return false;
            case 'h':
                printHelp();
                break;
            case 'e':
                int numOfElevators = Integer.valueOf(option.getValue());
                elevatorControlSystem = new ControlSystem(numOfElevators);
                break;
            case 's':
                printStatus();
                break;
            case 'u':
                String[] updateArgs = option.getValues();
                if (updateArgs.length > 1) {
                    int elevatorId = Integer.valueOf(updateArgs[0]);
                    List<Integer> additionalDisembarkmentRequests = new ArrayList<Integer>(updateArgs.length - 1);
                    for (int i = 1; i < updateArgs.length; i++) {
                        additionalDisembarkmentRequests.add(Integer.valueOf(updateArgs[i]));
                    }
                    elevatorControlSystem.update(elevatorId, additionalDisembarkmentRequests);
                }
                break;
            case 'n':
                int numOfSteps = Integer.valueOf(option.getValue());
                for (int i = 0; i < numOfSteps; i++) {
                    elevatorControlSystem.step();
                }
                break;
            case 'p':
                String[] pickupArgs = option.getValues();
                int pickupFloor = Integer.valueOf(pickupArgs[0]);
                int directrion = Integer.valueOf(pickupArgs[1]);
                elevatorControlSystem.pickup(pickupFloor, directrion);
                break;

        }
        return true;
    }
}
