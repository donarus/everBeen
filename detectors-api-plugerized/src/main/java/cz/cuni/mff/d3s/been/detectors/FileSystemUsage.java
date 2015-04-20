package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;

/**
 * Created by donarus on 20.4.15.
 */
public final class FileSystemUsage implements Serializable {

    private static final long serialVersionUID = -288396699437501645L;

    private String deviceName;

    private String directory;

    private long readBytes;

    private long reads;

    private long writeBytes;

    private long writes;

    public String getDeviceName() {
        return deviceName;
    }

    public FileSystemUsage setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public String getDirectory() {
        return directory;
    }

    public FileSystemUsage setDirectory(String directory) {
        this.directory = directory;
        return this;
    }

    public long getReadBytes() {
        return readBytes;
    }

    public FileSystemUsage setReadBytes(long readBytes) {
        this.readBytes = readBytes;
        return this;
    }

    public long getReads() {
        return reads;
    }

    public FileSystemUsage setReads(long reads) {
        this.reads = reads;
        return this;
    }

    public long getWriteBytes() {
        return writeBytes;
    }

    public FileSystemUsage setWriteBytes(long writeBytes) {
        this.writeBytes = writeBytes;
        return this;
    }

    public long getWrites() {
        return writes;
    }

    public FileSystemUsage setWrites(long writes) {
        this.writes = writes;
        return this;
    }

}
