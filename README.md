# elevator

To build the project you need at least JDK 6 and maven 3+. Just run `mvn clean install`.

To start the simulation run `mvn exec:java -pl elevator-cli` from the root module or `mvn exec:java` from the
elevator-cli module.

    mvn clean install
    mvn exec:java -pl elevator-cli
        usage: mvn exec:java
         -e,--elevators <arg>   the number of elevators. 1-16. Using this option
                                will reset the simulation with the requested
                                number of elevators and they will be at ground
                                level.
         -h                     print this help message.
         -n,--next <arg>        move the simulation by <arg> steps. Each of the
                                used elevators will move by that many floors.
         -p,--pickup <arg>      adds a pickup request to the system. Accepts 2
                                arguments. First is the floor to be picked up
                                from. Second is the desired direction to travel -
                                negative down, positive up.
         -q                     quit the simulation.
         -s,--status            prints the current status of all elevators
         -u,--update <arg>      updates an elevator. Accepts unlimited number of
                                arguments, where the first is required. It is the
                                elevator ID. From the 2nd arguments onwards are
                                the list of floors the elevator is requested to
                                visit for drop-offs.
                                Example: -u 0 1 -1 3 will make the first elevator
                                visit subterranean floor 1 and above ground floors
                                1 and 3.
