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
        CLI_OPTIONS.addOption("q", "quit the simulation.");
        CLI_OPTIONS.addOption("e", "elevators", true, "the number of elevators. 1-16. Using this option will reset " +
                "the simulation with the requested number of elevators and they will be at ground level.");
        CLI_OPTIONS.addOption("n", "next", true, "move the simulation by <arg> steps. Each of the used elevators " +
                "will move by that many floors.");
        CLI_OPTIONS.addOption("s", "status", false, "prints the current status of all elevators");

        Option pickup = Option.builder("p")
                .longOpt("pickup")
                .numberOfArgs(2)
                .desc("adds a pickup request to the system. Accepts 2 arguments. " +
                        "First is the floor to be picked up from. Second is the desired direction to travel - negative down, " +
                        "positive up.")
                .build();
        CLI_OPTIONS.addOption(pickup);

        Option update = Option.builder("u")
                .longOpt("update")
                .hasArgs()
                .desc("updates an elevator. Accepts unlimited number of arguments, where the first is required. " +
                        "It is the elevator ID. From the 2nd arguments onwards are the list of floors the elevator " +
                        "is requested to visit for drop-offs.\nExample: -u 0 1 -1 3 will make the " +
                        "first elevator visit subterranean floor 1 and above ground floors 1 and 3.")
                .build();
        CLI_OPTIONS.addOption(update);

        return CLI_OPTIONS;
    }
}
