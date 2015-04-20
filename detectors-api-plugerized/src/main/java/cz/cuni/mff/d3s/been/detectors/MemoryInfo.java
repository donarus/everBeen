package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;

/**
 * Created by donarus on 18.4.15.
 */
public final class MemoryInfo implements Serializable {

    private static final long serialVersionUID = -4260123406001184158L;

    private long memorySize;

    private long swapSize;

    public long getMemorySize() {
        return memorySize;
    }

    public MemoryInfo setMemorySize(long memorySize) {
        this.memorySize = memorySize;
        return this;
    }

    public long getSwapSize() {
        return swapSize;
    }

    public MemoryInfo setSwapSize(long swapSize) {
        this.swapSize = swapSize;
        return this;
    }

}
