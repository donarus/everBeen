package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;

/**
 * Created by donarus on 18.4.15.
 */
public final class MachineInfo implements Serializable {

    private static final long serialVersionUID = -101344339908602558L;

    private HardwareInfo hardwareInfo;

    private OperatingSystemInfo operatingSystemInfo;

    private FileSystemsInfo fileSystemsInfo;

    public HardwareInfo getHardwareInfo() {
        return hardwareInfo;
    }

    public MachineInfo setHardwareInfo(HardwareInfo hardwareInfo) {
        this.hardwareInfo = hardwareInfo;
        return this;
    }

    public OperatingSystemInfo getOperatingSystemInfo() {
        return operatingSystemInfo;
    }

    public MachineInfo setOperatingSystemInfo(OperatingSystemInfo operatingSystemInfo) {
        this.operatingSystemInfo = operatingSystemInfo;
        return this;
    }

    public FileSystemsInfo getFileSystemsInfo() {
        return fileSystemsInfo;
    }

    public MachineInfo setFileSystemsInfo(FileSystemsInfo fileSystemsInfo) {
        this.fileSystemsInfo = fileSystemsInfo;
        return this;
    }

}
