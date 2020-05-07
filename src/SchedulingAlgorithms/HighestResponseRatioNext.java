package SchedulingAlgorithms;

import SchedulingAlgorithms.Utilities.Helper;
import SchedulingAlgorithms.Utilities.ProcessRow;
import SchedulingAlgorithms.Utilities.ProcessSequence;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HighestResponseRatioNext extends Scheduler {
    @Override
    public void runAlgorithm() {

        this.getProcessRowList().sort(Comparator.comparing(ProcessRow::getArrivalTime));

        //get first process
        List<ProcessRow> tempList = Helper.deepCopy(this.getProcessRowList());
        int currentTime = tempList.get(0).getArrivalTime();

        while (!tempList.isEmpty()) {
            List<ProcessRow> avRowsList = new ArrayList<>();
            int finalCurrentTime = currentTime;
            tempList.stream().filter(row -> row.getArrivalTime() <= finalCurrentTime).forEach(avRowsList::add);

            ProcessRow currentRow = avRowsList.size() == 1 ? avRowsList.get(0) : calcBestRatio(avRowsList, finalCurrentTime);
            this.getSequencesList().add(new ProcessSequence(currentRow.getProcessName(), currentTime, currentTime + currentRow.getServiceTime()));
            currentTime += currentRow.getServiceTime();
            tempList.remove(currentRow);
        }

        this.getProcessRowList().stream().forEach(row -> {
            row.setWaitTime(this.getSequence(row).getStartTime() - row.getArrivalTime());
            row.setTurnaroundTime(row.getWaitTime() + row.getServiceTime());
        });
    }

    private ProcessRow calcBestRatio(List<ProcessRow> avRowsList, int currentTime) {

        ProcessRow best = null;
        double maxRatio = 0.0;

        for (ProcessRow eachRow : avRowsList) {
            double currentRatio = ((currentTime - eachRow.getArrivalTime() + 0.0) + eachRow.getServiceTime()) / eachRow.getServiceTime();
            if (currentRatio > maxRatio) {
                maxRatio = currentRatio;
                best = eachRow;
            }
        }
        return best;
    }
}
