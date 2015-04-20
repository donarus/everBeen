package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;

/**
 * Created by donarus on 18.4.15.
 */
public final class MemoryUsage implements Serializable {

    private static final long serialVersionUID = 4738182035322941244L;

    private long memTotal;

    private long memTotalUsed;

    private long memTotalFree;

    private long memActualUsed;

    private long memActualFree;

    private long swapTotal;

    private long swapUsed;

    private long swapFree;

    private double memTotalUsedPercentage;

    private double memTotalFreePercentage;

    private double swapUsedPercentage;

    public long getMemTotal() {
        return memTotal;
    }

    public MemoryUsage setMemTotal(long memTotal) {
        this.memTotal = memTotal;
        return this;
    }

    public long getMemTotalUsed() {
        return memTotalUsed;
    }

    public MemoryUsage setMemTotalUsed(long memTotalUsed) {
        this.memTotalUsed = memTotalUsed;
        return this;
    }

    public long getMemTotalFree() {
        return memTotalFree;
    }

    public MemoryUsage setMemTotalFree(long memTotalFree) {
        this.memTotalFree = memTotalFree;
        return this;
    }

    public long getMemActualUsed() {
        return memActualUsed;
    }

    public MemoryUsage setMemActualUsed(long memActualUsed) {
        this.memActualUsed = memActualUsed;
        return this;
    }

    public long getMemActualFree() {
        return memActualFree;
    }

    public MemoryUsage setMemActualFree(long memActualFree) {
        this.memActualFree = memActualFree;
        return this;
    }

    public long getSwapTotal() {
        return swapTotal;
    }

    public MemoryUsage setSwapTotal(long swapTotal) {
        this.swapTotal = swapTotal;
        return this;
    }

    public long getSwapUsed() {
        return swapUsed;
    }

    public MemoryUsage setSwapUsed(long swapUsed) {
        this.swapUsed = swapUsed;
        return this;
    }

    public long getSwapFree() {
        return swapFree;
    }

    public MemoryUsage setSwapFree(long swapFree) {
        this.swapFree = swapFree;
        return this;
    }

    public double getMemTotalUsedPercentage() {
        return memTotalUsedPercentage;
    }

    public MemoryUsage setMemTotalUsedPercentage(double memTotalUsedPercentage) {
        this.memTotalUsedPercentage = memTotalUsedPercentage;
        return this;
    }

    public double getMemTotalFreePercentage() {
        return memTotalFreePercentage;
    }

    public MemoryUsage setMemTotalFreePercentage(double memTotalFreePercentage) {
        this.memTotalFreePercentage = memTotalFreePercentage;
        return this;
    }

    public double getSwapUsedPercentage() {
        return swapUsedPercentage;
    }

    public MemoryUsage setSwapUsedPercentage(double swapUsedPercentage) {
        this.swapUsedPercentage = swapUsedPercentage;
        return this;
    }
}
