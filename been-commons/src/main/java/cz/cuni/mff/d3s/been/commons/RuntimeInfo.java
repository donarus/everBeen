package cz.cuni.mff.d3s.been.commons;

import cz.cuni.mff.d3s.been.commons.monitorsample.MonitorSample;
import cz.cuni.mff.d3s.been.commons.nodeinfo.Filesystem;
import cz.cuni.mff.d3s.been.commons.nodeinfo.Hardware;
import cz.cuni.mff.d3s.been.commons.nodeinfo.Java;
import cz.cuni.mff.d3s.been.commons.nodeinfo.OperatingSystem;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RuntimeInfo implements Serializable {
    private String id;
    private int port;
    private String host;
    private String workingDirectory;
    private String tasksWorkingDirectory;
    private String type;
    private String exclusivity;
    private String exclusiveId;
    private int taskCount;
    private int maxTasks;
    private int memoryThreshold;
    private Hardware hardware;
    private Date startUpTime;
    private MonitorSample monitorSample;
    private Java java;
    private OperatingSystem operatingSystem;
    private List<Filesystem> filesystem;
    private List<String> taskDirs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public String getTasksWorkingDirectory() {
        return tasksWorkingDirectory;
    }

    public void setTasksWorkingDirectory(String tasksWorkingDirectory) {
        this.tasksWorkingDirectory = tasksWorkingDirectory;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExclusivity() {
        return exclusivity;
    }

    public void setExclusivity(String exclusivity) {
        this.exclusivity = exclusivity;
    }

    public String getExclusiveId() {
        return exclusiveId;
    }

    public void setExclusiveId(String exclusiveId) {
        this.exclusiveId = exclusiveId;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public int getMaxTasks() {
        return maxTasks;
    }

    public void setMaxTasks(int maxTasks) {
        this.maxTasks = maxTasks;
    }

    public int getMemoryThreshold() {
        return memoryThreshold;
    }

    public void setMemoryThreshold(int memoryThreshold) {
        this.memoryThreshold = memoryThreshold;
    }

    public Hardware getHardware() {
        return hardware;
    }

    public void setHardware(Hardware hardware) {
        this.hardware = hardware;
    }

    public Date getStartUpTime() {
        return startUpTime;
    }

    public void setStartUpTime(Date startUpTime) {
        this.startUpTime = startUpTime;
    }

    public MonitorSample getMonitorSample() {
        return monitorSample;
    }

    public void setMonitorSample(MonitorSample monitorSample) {
        this.monitorSample = monitorSample;
    }

    public Java getJava() {
        return java;
    }

    public void setJava(Java java) {
        this.java = java;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public List<Filesystem> getFilesystem() {
        return filesystem;
    }

    public void setFilesystem(List<Filesystem> filesystem) {
        this.filesystem = filesystem;
    }

    public List<String> getTaskDirs() {
        return taskDirs;
    }

    public void setTaskDirs(List<String> taskDirs) {
        this.taskDirs = taskDirs;
    }
}
