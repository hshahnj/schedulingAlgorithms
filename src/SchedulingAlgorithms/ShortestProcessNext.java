package SchedulingAlgorithms;

import SchedulingAlgorithms.Utilities.Helper;
import SchedulingAlgorithms.Utilities.ProcessRow;
import SchedulingAlgorithms.Utilities.ProcessSequence;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ShortestProcessNext extends Scheduler {
    @Override
    public void runAlgorithm() {
        this.getProcessRowList().sort(Comparator.comparing(ProcessRow::getArrivalTime));

        List<ProcessRow> tempList = Helper.deepCopy(this.getProcessRowList());
        int currentTime = tempList.get(0).getArrivalTime();

        while (!tempList.isEmpty()) {
            List<ProcessRow> avRowsList = new ArrayList<>();

            int finalCurrentTime = currentTime;
            tempList.stream().filter(row -> row.getArrivalTime() <= finalCurrentTime).forEach(avRowsList::add);
            tempList.sort(Comparator.comparing(ProcessRow::getServiceTime));

            ProcessRow currentRow = avRowsList.get(0);
            this.getSequencesList().add(new ProcessSequence(currentRow.getProcessName(), currentTime,
                    currentTime + currentRow.getServiceTime()));
            currentTime += currentRow.getServiceTime();

            tempList.stream().filter(row -> row.getProcessName().equals(currentRow.getProcessName())).findFirst().ifPresent(tempList::remove);


        }

        this.getProcessRowList().stream().forEach(row -> {
            row.setWaitTime(this.getSequence(row).getStartTime() - row.getArrivalTime());
            row.setTurnaroundTime(row.getWaitTime() + row.getServiceTime());
        });
    }
}
