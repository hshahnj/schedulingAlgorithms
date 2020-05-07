import SchedulingAlgorithms.Scheduler;
import SchedulingAlgorithms.Simulator;
import SchedulingAlgorithms.Utilities.Helper;
import SchedulingAlgorithms.Utilities.IO;
import SchedulingAlgorithms.Utilities.ProcessRow;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) {

        //GUI Run check.
        System.out.println("Would you like to run the GUI or proceed with regular Java code implementation?");
        System.out.println("Type Y for GUI, any other key for Regular: ");
        String method = IO.readString();

        if (method.toUpperCase().equals("Y")) {
            System.out.println("----------PLEASE READ!----------");
            System.out.println("The Simulator will open in a different window!");
            System.out.println("For RR and MLF, once the processes are placed in, click compute and it will ask you for TIME QUANTUM.");
            System.out.println("Simulator only handles one algorithm at a time.");
            System.out.println("You can keep WT and TAT Columns empty initially. The algorithms will fill them in.");
            Simulator.main(null);
        } else {
            //Input Handler gets the number of processes and their arrival and wait times.
            System.out.println("\nRegular way chosen!\n");
            List<ProcessRow> processRows = Helper.inputHandler();
            System.out.println("Which algorithm do you want to choose?");
            System.out.println("Options: FCFS, RR, SPN, SRT, HRRN, MLF or ALL");
            System.out.println("Enter Algorithms (space separated): ");
            String processInput = IO.readString();

            String[] algoStrings = processInput.split(" ");

            //Error + empty check
            if (algoStrings[0].equals("")) {
                System.out.println("No algorithms inputted!");
                System.out.println("Please restart with proper algorithms!");
                throw new RuntimeException();
            }

            //Initialize flags for future use.
            AtomicBoolean runAll = new AtomicBoolean(false);
            AtomicBoolean requireTimeQuantum = new AtomicBoolean(false);
            int timeQuantum = 5;

            Arrays.stream(algoStrings).filter(x -> x.toUpperCase().equals("ALL")).findFirst().ifPresent(s -> runAll.set(true));
            Arrays.stream(algoStrings).filter(x -> x.toUpperCase().equals("RR") || x.toUpperCase().equals("MLF") || x.toUpperCase().equals("ALL"))
                    .findFirst().ifPresent(s -> requireTimeQuantum.set(true));

            if (requireTimeQuantum.get()) {
                System.out.println("One or more of your algorithms chosen requires Time Quantum Value.");
                System.out.println("Please enter Time Quantum value:");
                timeQuantum = IO.readInt();
            }

            //Simplifying for condensing algorithms method into one.
            if (runAll.get()) {
                algoStrings = new String[1];
                algoStrings[0] = ("ALL");
            }

            algorithms(processRows, algoStrings, timeQuantum);
            System.exit(0);
        }

        /* TEST CASE RUN SCENARIOS BELOW
            fcfs();
            hrrn();
            rr();
            mlf();
            spn();
            srt();
         */
    }

    public static void algorithms(List<ProcessRow> listRows, String[] listOfAlgorithms, int timeQuantum) {
        List<Class<? extends Scheduler>> algorithms = new ArrayList<Class<? extends Scheduler>>();

        for (String algoName : listOfAlgorithms) {
            if (algoName.toUpperCase().equals("ALL")) {
                algorithms.addAll(Scheduler.getAlgoToClassMap().values());
            } else {
                if (Scheduler.getAlgoToClassMap().containsKey(algoName.toUpperCase())) {
                    algorithms.add(Scheduler.getAlgoToClassMap().get(algoName.toUpperCase()));
                }
            }
        }

        algorithms.forEach(algo -> {
            try {
                Scheduler instance = algo.getDeclaredConstructor().newInstance();
                instance.addListOfRows(listRows);
                instance.runAlgorithm();
                instance.setTimeQuantum(timeQuantum);
                Helper.display(instance);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        System.out.println("FINISHED RUNNING ALGORITHMS!");
    }


    // ------------------------------------INDIVIDUAL TEST CASE CODE BELOW-----------------------------------------

    /*
    public static void fcfs() {
        Scheduler fcfs = new FirstComeFirstServe();
        fcfs.addRow(new ProcessRow("A", 0, 3));
        fcfs.addRow(new ProcessRow("B", 2, 6));
        fcfs.addRow(new ProcessRow("C", 4, 4));
        fcfs.addRow(new ProcessRow("D", 6, 5));
        fcfs.addRow(new ProcessRow("E", 8, 2));
        fcfs.runAlgorithm();
        Helper.display(fcfs);

    }

    public static void hrrn() {
        Scheduler hrrn = new HighestResponseRatioNext();
        hrrn.addRow(new ProcessRow("A", 0, 3));
        hrrn.addRow(new ProcessRow("B", 2, 6));
        hrrn.addRow(new ProcessRow("C", 4, 4));
        hrrn.addRow(new ProcessRow("D", 6, 5));
        hrrn.addRow(new ProcessRow("E", 8, 2));
        hrrn.runAlgorithm();
        Helper.display(hrrn);
    }


    public static void rr() {
        Scheduler rr = new RoundRobin();
        rr.setTimeQuantum(2);
        rr.addRow(new ProcessRow("P1", 0, 4));
        rr.addRow(new ProcessRow("P2", 1, 5));
        rr.addRow(new ProcessRow("P3", 2, 6));
        rr.addRow(new ProcessRow("P4", 4, 1));
        rr.addRow(new ProcessRow("P5", 6, 3));
        rr.addRow(new ProcessRow("P6", 7, 2));
        rr.runAlgorithm();
        Helper.display(rr);

    }

    public static void mlf() {
        Scheduler mlf = new MultilevelFeedback();
        mlf.setTimeQuantum(4);
        mlf.addRow(new ProcessRow("A", 0, 3));
        mlf.addRow(new ProcessRow("B", 2, 6));
        mlf.addRow(new ProcessRow("C", 4, 4));
        mlf.addRow(new ProcessRow("D", 6, 5));
        mlf.addRow(new ProcessRow("E", 8, 2));
        mlf.runAlgorithm();
        Helper.display(mlf);

    }

    public static void spn() {
        Scheduler spn = new ShortestProcessNext();
        spn.addRow(new ProcessRow("P1", 0, 5));
        spn.addRow(new ProcessRow("P2", 2, 3));
        spn.addRow(new ProcessRow("P3", 4, 2));
        spn.addRow(new ProcessRow("P4", 6, 4));
        spn.addRow(new ProcessRow("P5", 7, 1));
        spn.runAlgorithm();
        Helper.display(spn);
    }

    public static void srt() {
        Scheduler srt = new ShortestRemainingTime();
        srt.addRow(new ProcessRow("A", 0, 3));
        srt.addRow(new ProcessRow("B", 2, 6));
        srt.addRow(new ProcessRow("C", 4, 4));
        srt.addRow(new ProcessRow("D", 6, 5));
        srt.addRow(new ProcessRow("E", 8, 2));
        srt.runAlgorithm();
        Helper.display(srt);
    }
    */
}
