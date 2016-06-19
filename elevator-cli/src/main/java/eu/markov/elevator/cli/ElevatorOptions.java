package eu.markov.elevator.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Created by Gesh on 19.06.2016.
 */
public final class ElevatorOptions {
    private static final Options CLI_OPTIONS = new Options();

    public static final Options cliOptions() {
        if (CLI_OPTIONS.hasOption("h")) {
            return CLI_OPTIONS;
        }

        // Otherwise create them.
        CLI_OPTIONS.addOption("h", "print this help message.");
        CLI_OPTIONS.addOption("e", "elevators", true, "the number of elevators. 1-16. Using this option will reset " +
                "the simulation with the requested number of elevators and they will be at ground level.");
        CLI_OPTIONS.addOption("n", "next", true, "move the simulation by <arg> steps. Each of the used elevators will move by " +
                "that many floors.");
        CLI_OPTIONS.addOption("s", "status", false, "prints the current status of all elevators");

        Option update = Option.builder("u")
                .longOpt("update")
                .hasArgs()
                .desc("updates an elevator. Accepts unlimited number of arguments, where the first 2 are required. " +
                        "They are the elevator ID and the floor it is at. From the 3rd arguments onwards are the " +
                        "list of floors the elevator is requested to visit.\nExample: -u 0 0 1 -1 3 will put the " +
                        "first elevator on the ground floor and it will visit subterranean floor 1 and above ground " +
                        "floors 1 and 3.")
                .build();
        CLI_OPTIONS.addOption(update);

        return CLI_OPTIONS;
    }
}
