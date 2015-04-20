package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;

/**
 * Created by donarus on 18.4.15.
 */
public final class MonitorSample implements Serializable {

    private static final long serialVersionUID = 3047516257124952747L;

    private LoadAverage loadAverage;

    private CpusUsage cpusUsage;

    private MemoryUsage memoryUsage;

    private Processes processes;

    private NetworkUsage networkUsage;

    private FileSystemsUsage fileSystemsUsage;

    public LoadAverage getLoadAverage() {
        return loadAverage;
    }

    public MonitorSample setLoadAverage(LoadAverage loadAverage) {
        this.loadAverage = loadAverage;
        return this;
    }

    public CpusUsage getCpusUsage() {
        return cpusUsage;
    }

    public MonitorSample setCpusUsage(CpusUsage cpusUsage) {
        this.cpusUsage = cpusUsage;
        return this;
    }

    public MemoryUsage getMemoryUsage() {
        return memoryUsage;
    }

    public MonitorSample setMemoryUsage(MemoryUsage memoryUsage) {
        this.memoryUsage = memoryUsage;
        return this;
    }

    public Processes getProcesses() {
        return processes;
    }

    public MonitorSample setProcesses(Processes processes) {
        this.processes = processes;
        return this;
    }

    public NetworkUsage getNetworkUsage() {
        return networkUsage;
    }

    public MonitorSample setNetworkUsage(NetworkUsage networkUsage) {
        this.networkUsage = networkUsage;
        return this;
    }

    public FileSystemsUsage getFileSystemsUsage() {
        return fileSystemsUsage;
    }

    public MonitorSample setFileSystemsUsage(FileSystemsUsage fileSystemsUsage) {
        this.fileSystemsUsage = fileSystemsUsage;
        return this;
    }

}
