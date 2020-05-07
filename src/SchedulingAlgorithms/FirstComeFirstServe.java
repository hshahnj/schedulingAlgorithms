package SchedulingAlgorithms;

import SchedulingAlgorithms.Utilities.ProcessRow;
import SchedulingAlgorithms.Utilities.ProcessSequence;

import java.util.Comparator;
import java.util.List;

public class FirstComeFirstServe extends Scheduler {
    @Override
    public void runAlgorithm() {
        List<ProcessSequence> sequences = this.getSequencesList();
        this.getProcessRowList().sort(Comparator.comparing(ProcessRow::getArrivalTime));

        this.getProcessRowList().forEach(currentRow -> {
            if (sequences.isEmpty())
                sequences.add(new ProcessSequence(currentRow.getProcessName(), currentRow.getArrivalTime(),
                        currentRow.getArrivalTime() + currentRow.getServiceTime()));
            else {
                ProcessSequence earlierSequence = sequences.get(sequences.size() - 1);
                sequences.add(new ProcessSequence(currentRow.getProcessName(), earlierSequence.getCompletionTime(),
                        earlierSequence.getCompletionTime() + currentRow.getServiceTime()));
            }
        });

        this.getProcessRowList().forEach(processRow -> {
            processRow.setWaitTime(this.getSequence(processRow).getStartTime() - processRow.getArrivalTime());
            processRow.setTurnaroundTime(processRow.getWaitTime() + processRow.getServiceTime());
        });

    }
}
