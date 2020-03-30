package TTSA;

import java.util.Arrays;

public class main {

    public static void main(String[] args) throws Exception {

        SimulatedAnnealing.Schedule TTSA = new SimulatedAnnealing.Schedule(14);
        System.out.println("Initial Schedule: ");
        TTSA.printTable();
        System.out.println();
        System.out.println("Cost of the schedule: "+TTSA.cost(TTSA.getScheduleMap()));
        System.out.println();
        TTSA.simulatedAnnealing();

    }



}
