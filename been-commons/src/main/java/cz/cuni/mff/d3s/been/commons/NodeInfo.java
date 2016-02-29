package cz.cuni.mff.d3s.been.commons;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class NodeInfo implements Serializable {

    private String id;
    private String beenWorkingDirectory;
    private NodeType nodeType;
    private Hardware hardware;
    private Date startUpTime;
    private Java java;
    private OperatingSystem operatingSystem;
    private List<Filesystem> filesystem;

    public String getId() {
        return id;
    }

    public NodeInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getBeenWorkingDirectory() {
        return beenWorkingDirectory;
    }

    public NodeInfo setBeenWorkingDirectory(String beenWorkingDirectory) {
        this.beenWorkingDirectory = beenWorkingDirectory;
        return this;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public NodeInfo setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
        return this;
    }

    public Hardware getHardware() {
        return hardware;
    }

    public NodeInfo setHardware(Hardware hardware) {
        this.hardware = hardware;
        return this;
    }

    public Date getStartUpTime() {
        return startUpTime;
    }

    public long getUptime() { // in milliseconds
        return new Date().getTime() - startUpTime.getTime();
    }

    public NodeInfo setStartUpTime(Date startUpTime) {
        this.startUpTime = startUpTime;
        return this;
    }

    public Java getJava() {
        return java;
    }

    public NodeInfo setJava(Java java) {
        this.java = java;
        return this;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public NodeInfo setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
        return this;
    }

    public List<Filesystem> getFilesystem() {
        return filesystem;
    }

    public NodeInfo setFilesystem(List<Filesystem> filesystem) {
        this.filesystem = filesystem;
        return this;
    }
}
