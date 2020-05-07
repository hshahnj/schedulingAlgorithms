package SchedulingAlgorithms.Utilities;

public class ProcessSequence {
    //Class to handle a sequence of events in order and hold information necessary to move time units forward based on time quantums.

    //set processName and startTime to final so can't be altered.
    private final String processName;
    private final int startTime;
    private int completionTime;

    public ProcessSequence(String processName, int startTime, int completionTime) {
        this.processName = processName;
        this.startTime = startTime;
        this.completionTime = completionTime;
    }

    public String getProcessName() {
        return processName;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }
}
