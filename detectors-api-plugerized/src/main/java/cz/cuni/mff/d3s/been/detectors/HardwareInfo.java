package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;

/**
 * Created by donarus on 18.4.15.
 */
public final class HardwareInfo implements Serializable {

    private static final long serialVersionUID = -8108595968760317851L;

    private CpusInfo cpusInfo;

    private MemoryInfo memoryInfo;

    private NetworkInfo networkInfo;

    public CpusInfo getCpusInfo() {
        return cpusInfo;
    }

    public HardwareInfo setCpusInfo(CpusInfo cpusInfo) {
        this.cpusInfo = cpusInfo;
        return this;
    }

    public MemoryInfo getMemoryInfo() {
        return memoryInfo;
    }

    public HardwareInfo setMemoryInfo(MemoryInfo memoryInfo) {
        this.memoryInfo = memoryInfo;
        return this;
    }

    public NetworkInfo getNetworkInfo() {
        return networkInfo;
    }

    public HardwareInfo setNetworkInfo(NetworkInfo networkInfo) {
        this.networkInfo = networkInfo;
        return this;
    }

}
