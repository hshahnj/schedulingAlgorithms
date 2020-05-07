package SchedulingAlgorithms;

import SchedulingAlgorithms.Utilities.Helper;
import SchedulingAlgorithms.Utilities.ProcessRow;
import SchedulingAlgorithms.Utilities.ProcessSequence;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ShortestRemainingTime extends Scheduler {

    @Override
    public void runAlgorithm() {
        this.getProcessRowList().sort(Comparator.comparing(ProcessRow::getArrivalTime));

        List<ProcessRow> tempList = Helper.deepCopy(this.getProcessRowList());
        int currentTime = tempList.get(0).getArrivalTime();

        while (!tempList.isEmpty()) {
            List<ProcessRow> avRowsList = new ArrayList<>();

            int finalCurrentTime = currentTime;
            tempList.stream().filter(row -> row.getArrivalTime() <= finalCurrentTime).forEach(avRowsList::add);
            avRowsList.sort(Comparator.comparing(ProcessRow::getServiceTime));

            ProcessRow currentRow = avRowsList.get(0);
            this.getSequencesList().add(new ProcessSequence(currentRow.getProcessName(), currentTime, ++currentTime));
            currentRow.setServiceTime(currentRow.getServiceTime() - 1);

            if (currentRow.getServiceTime() == 0) {
                tempList.stream().filter(row -> row.getProcessName().equals(currentRow.getProcessName())).findFirst().ifPresent(tempList::remove);
            }
        }

        for (int i = this.getSequencesList().size() - 1; i > 0; i--) {
            List<ProcessSequence> sequences = this.getSequencesList();

            if (sequences.get(i - 1).getProcessName().equals(sequences.get(i).getProcessName())) {
                sequences.get(i - 1).setCompletionTime(sequences.get(i).getCompletionTime());
                sequences.remove(i);
            }
        }

        Helper.setTimeUsingMap(this.getProcessRowList(), this.getSequencesList());

    }
}
