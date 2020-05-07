package SchedulingAlgorithms.Utilities;

//basic Process skeleton with necessary information
public class ProcessRow {
    private String processName;
    private int arrivalTime;
    private int serviceTime;
    private int waitTime;
    private int turnaroundTime;

    private ProcessRow(String processName, int arrivalTime, int serviceTime, int waitTime, int turnaroundTime) {
        this.processName = processName;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.waitTime = waitTime;
        this.turnaroundTime = turnaroundTime;
    }

    public ProcessRow(String processName, int arrivalTime, int serviceTime, int waitTime) {
        this(processName, arrivalTime, serviceTime, waitTime, 0);
    }

    public ProcessRow(String processName, int arrivalTime, int serviceTime) {
        this(processName, arrivalTime, serviceTime, 0, 0);
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }
}
