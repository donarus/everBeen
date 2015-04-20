package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;

/**
 * Created by donarus on 18.4.15.
 */
public final class FileSystemInfo implements Serializable {

    private static final long serialVersionUID = 4490399452719125373L;

    private String deviceName;

    private String directory;

    private String type;

    private long total;

    private long free;

    public String getDeviceName() {
        return deviceName;
    }

    public FileSystemInfo setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public String getDirectory() {
        return directory;
    }

    public FileSystemInfo setDirectory(String directory) {
        this.directory = directory;
        return this;
    }

    public String getType() {
        return type;
    }

    public FileSystemInfo setType(String type) {
        this.type = type;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public FileSystemInfo setTotal(long total) {
        this.total = total;
        return this;
    }

    public long getFree() {
        return free;
    }

    public FileSystemInfo setFree(long free) {
        this.free = free;
        return this;
    }

}
