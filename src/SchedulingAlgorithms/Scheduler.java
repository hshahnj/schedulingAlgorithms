package SchedulingAlgorithms;

import SchedulingAlgorithms.Utilities.ProcessRow;
import SchedulingAlgorithms.Utilities.ProcessSequence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

//simple class to handle all base calculations and/or information and move forward events
//Can make it into abstract for algorithms to absorb. This can be a general structure
//goal is to make it a general structure.

public abstract class Scheduler {
    private final List<ProcessSequence> sequencesList;
    private final List<ProcessRow> processRowList;
    private int timeQuantum;

    private static ConcurrentHashMap<String, Class<? extends Scheduler>> algoToClassMap = null;

    public Scheduler() {
        sequencesList = new ArrayList<>();
        processRowList = new ArrayList<>();
        timeQuantum = 1;

    }

    public abstract void runAlgorithm();

    public boolean addRow(ProcessRow row) {
        return processRowList.add(row);
    }

    public void addListOfRows(List<ProcessRow> listRows) {
        listRows.forEach(this::addRow);
    }

    public double getAverageWaitTime() {
        return processRowList.stream().mapToDouble(ProcessRow::getWaitTime).sum() / processRowList.size();
    }

    public double getAverageTurnaroundTime() {
        return processRowList.stream().mapToDouble(ProcessRow::getTurnaroundTime).sum() / processRowList.size();
    }

    public ProcessRow getProcessRow(String processName) {
        boolean isPresent = processRowList.stream().anyMatch(x -> x.getProcessName().equals(processName));
        return isPresent ? processRowList.stream().filter(x -> x.getProcessName().equals(processName)).findFirst().get() : null;
    }

    public ProcessSequence getSequence(ProcessRow processRow) {
        for (ProcessSequence sequence : sequencesList) {
            if (processRow.getProcessName().equals(sequence.getProcessName())) return sequence;
        }
        return null;
    }

    //getter setter
    public List<ProcessSequence> getSequencesList() {
        return sequencesList;
    }

    public List<ProcessRow> getProcessRowList() {
        return processRowList;
    }

    public int getTimeQuantum() {

        return timeQuantum;
    }

    public void setTimeQuantum(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

    public static ConcurrentHashMap<String, Class<? extends Scheduler>> getAlgoToClassMap() {
        if (algoToClassMap == null) {
            Scheduler.algoToClassMap = new ConcurrentHashMap<String, Class<? extends Scheduler>>();
            algoToClassMap.put("FCFS", FirstComeFirstServe.class);
            algoToClassMap.put("RR", RoundRobin.class);
            algoToClassMap.put("HRRN", HighestResponseRatioNext.class);
            algoToClassMap.put("SPN", ShortestProcessNext.class);
            algoToClassMap.put("SRT", ShortestRemainingTime.class);
            algoToClassMap.put("MLF", MultilevelFeedback.class);
        }

        return algoToClassMap;
    }

}
