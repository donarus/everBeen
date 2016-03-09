package cz.cuni.mff.d3s.been.commons.nodeinfo;

import java.io.Serializable;

public class Memory implements Serializable {

    private long ram;
    private long swap;

    public long getSwap() {
        return swap;
    }

    public void setSwap(long swap) {
        this.swap = swap;
    }

    public long getRam() {
        return ram;
    }

    public void setRam(long ram) {
        this.ram = ram;
    }
}
