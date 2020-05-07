package SchedulingAlgorithms;

import SchedulingAlgorithms.Utilities.Helper;
import SchedulingAlgorithms.Utilities.ProcessRow;
import SchedulingAlgorithms.Utilities.ProcessSequence;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MultilevelFeedback extends Scheduler {
    @Override
    public void runAlgorithm() {
        this.getProcessRowList().sort(Comparator.comparing(ProcessRow::getArrivalTime));

        List<ProcessRow> tempList = Helper.deepCopy(this.getProcessRowList());
        int currentTime = tempList.get(0).getArrivalTime();
        int timeQuantum = this.getTimeQuantum();
        List<ProcessRow> tempList_residual = new ArrayList<>();

        while (!tempList.isEmpty()) {
            ProcessRow processRow = tempList.get(0);
            int usedServiceTime = Math.min(processRow.getServiceTime(), timeQuantum);
            this.getSequencesList().add(new ProcessSequence(processRow.getProcessName(), currentTime, currentTime + usedServiceTime));
            currentTime += usedServiceTime;
            tempList.remove(0);

            if (processRow.getServiceTime() > timeQuantum) {
                processRow.setServiceTime(processRow.getServiceTime() - timeQuantum);
                tempList_residual.add(processRow);
            }

            if (tempList.isEmpty() && !tempList_residual.isEmpty()) {
                tempList.addAll(tempList_residual);
                tempList_residual.clear();
            }
        }

        Helper.setTimeUsingMap(this.getProcessRowList(), this.getSequencesList());
    }
}
