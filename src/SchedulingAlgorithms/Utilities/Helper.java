package SchedulingAlgorithms.Utilities;

import SchedulingAlgorithms.Scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Helper {

    public static List<ProcessRow> deepCopy(List<ProcessRow> oldList) {
        List<ProcessRow> newList = new ArrayList();

        for (ProcessRow row : oldList) {
            newList.add(new ProcessRow(row.getProcessName(), row.getArrivalTime(), row.getServiceTime()));
        }

        return newList;
    }

    public static List<ProcessRow> inputHandler() {
        System.out.println("Enter the number of processes: ");
        int processCount = IO.readInt();
        System.out.println("Enter the arrival time of processes in order (separated by space): ");
        String processArrivalTime = IO.readString();
        System.out.println("Enter the service time of processes in order (separated by space): ");
        String processServiceTime = IO.readString();

        int[] arrTimeArray = adjustInput(processArrivalTime);
        int[] serTimeArray = adjustInput(processServiceTime);

        if (arrTimeArray.length != processCount || serTimeArray.length != processCount) {
            System.out.println("Length counts not matched correctly. Make sure arrival / service times are same number as the number of processes.");
            System.out.println("Expected Processes: " + processCount);
            System.out.println("Received Arrival Times: " + arrTimeArray.length);
            System.out.println("Received Service Times: " + serTimeArray.length);
            System.out.println("Please restart program and fix.");
            throw new RuntimeException();
        }

        int i = 0;
        List<ProcessRow> processRows = new ArrayList<>();

        while (i < processCount) {
            ProcessRow temp = new ProcessRow(Character.toString((char) i + 65), arrTimeArray[i], serTimeArray[i]);
            processRows.add(temp);
            i++;
        }

        return processRows;
    }

    public static int[] adjustInput(String input) {
        String[] tempString = input.split(" ");
        int[] intArray = new int[tempString.length];
        int i = 0;
        for (String string : tempString) {
            intArray[i] = Integer.parseInt(string);
            i++;
        }
        return intArray;
    }

    public static void display(Scheduler object) {
        System.out.println("\n--------------------" + object.getClass().getSimpleName() + "--------------------");
        System.out.println("\nPrcs\tArrT\tSvcT\tWaitT\tTurnArT");

        for (ProcessRow row : object.getProcessRowList()) {
            System.out.println(row.getProcessName() + "\t\t" + row.getArrivalTime() + "\t\t" + row.getServiceTime() + "\t\t" + row.getWaitTime() + "\t\t" + row.getTurnaroundTime());
        }

        System.out.println();
        System.out.println("Format: Time(Process)");
        for (int i = 0; i < object.getSequencesList().size(); i++) {
            List<ProcessSequence> timeline = object.getSequencesList();
            System.out.print(timeline.get(i).getStartTime() + "(" + timeline.get(i).getProcessName() + ")");

            if (i == object.getSequencesList().size() - 1) {
                System.out.print(timeline.get(i).getCompletionTime());
            }
        }

        System.out.println("\n\nAverage WT: " + object.getAverageWaitTime() + "\nAverage TAT: " + object.getAverageTurnaroundTime());
    }

    public static void setTimeUsingMap(List<ProcessRow> processRowList, List<ProcessSequence> processSequenceList) {
        Map<String, Integer> map = new HashMap();

        for (ProcessRow row : processRowList) {
            map.clear();
            for (ProcessSequence sequence : processSequenceList) {
                if (sequence.getProcessName().equals(row.getProcessName())) {
                    if (map.containsKey(sequence.getProcessName())) {
                        int w = sequence.getStartTime() - map.get(sequence.getProcessName());
                        row.setWaitTime(row.getWaitTime() + w);
                    } else {
                        row.setWaitTime(sequence.getStartTime() - row.getArrivalTime());
                    }
                    map.put(sequence.getProcessName(), sequence.getCompletionTime());
                }
            }

            row.setTurnaroundTime(row.getWaitTime() + row.getServiceTime());
        }
    }
}
