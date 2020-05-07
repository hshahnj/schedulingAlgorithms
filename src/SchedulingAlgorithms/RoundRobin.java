package SchedulingAlgorithms;

import SchedulingAlgorithms.Utilities.Helper;
import SchedulingAlgorithms.Utilities.ProcessRow;
import SchedulingAlgorithms.Utilities.ProcessSequence;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoundRobin extends Scheduler {
    @Override
    public void runAlgorithm() {
        List<ProcessRow> processRowList = getProcessRowList();

        processRowList.sort(Comparator.comparing(ProcessRow::getArrivalTime));

        List<ProcessRow> tempList = Helper.deepCopy(processRowList);
        int currentTime = tempList.get(0).getArrivalTime();
        int timeQuantum = this.getTimeQuantum();

        while (!tempList.isEmpty()) {
            ProcessRow processRow = tempList.get(0);
            int usedServiceTime = Math.min(processRow.getServiceTime(), timeQuantum);
            this.getSequencesList().add(new ProcessSequence(processRow.getProcessName(), currentTime, currentTime + usedServiceTime));
            currentTime += usedServiceTime;
            tempList.remove(0);

            if (processRow.getServiceTime() > timeQuantum) {
                processRow.setServiceTime(processRow.getServiceTime() - timeQuantum);

                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getArrivalTime() > currentTime) {
                        tempList.add(i, processRow);
                        break;
                    } else if (i == tempList.size() - 1) {
                        tempList.add(processRow);
                        break;
                    }
                }
            }
        }

        Helper.setTimeUsingMap(this.getProcessRowList(), this.getSequencesList());
    }
}
