package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;

/**
 * Created by donarus on 18.4.15.
 */
public final class CpuInfo implements Serializable {

    private static final long serialVersionUID = 8740649384958327270L;

    private String vendor;

    private String model;

    private int mhz;

    private long cacheSize;

    public String getVendor() {
        return vendor;
    }

    public CpuInfo setVendor(String vendor) {
        this.vendor = vendor;
        return this;
    }

    public String getModel() {
        return model;
    }

    public CpuInfo setModel(String model) {
        this.model = model;
        return this;
    }

    public int getMhz() {
        return mhz;
    }

    public CpuInfo setMhz(int mhz) {
        this.mhz = mhz;
        return this;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public CpuInfo setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
        return this;
    }

}
