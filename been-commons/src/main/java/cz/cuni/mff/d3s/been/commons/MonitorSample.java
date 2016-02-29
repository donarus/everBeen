
package cz.cuni.mff.d3s.been.commons;

import java.io.Serializable;
import java.util.List;

public class MonitorSample implements Serializable {

    private long timestamp;
    private long freeMemory;
    private int processCount;
    private List<NetworkSample> interfaces;
    private List<FilesystemSample> filesystems;
    private double cpuUsage;
    private LoadAverage loadAverage;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public int getProcessCount() {
        return processCount;
    }

    public void setProcessCount(int processCount) {
        this.processCount = processCount;
    }

    public List<NetworkSample> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<NetworkSample> interfaces) {
        this.interfaces = interfaces;
    }

    public List<FilesystemSample> getFilesystems() {
        return filesystems;
    }

    public void setFilesystems(List<FilesystemSample> filesystems) {
        this.filesystems = filesystems;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public LoadAverage getLoadAverage() {
        return loadAverage;
    }

    public void setLoadAverage(LoadAverage loadAverage) {
        this.loadAverage = loadAverage;
    }
}
