package cz.cuni.mff.d3s.been.commons;

import java.io.Serializable;

public class NetworkSample implements Serializable {

    private String name;
    private long bytesIn;
    private long bytesOut;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBytesIn() {
        return bytesIn;
    }

    public void setBytesIn(long bytesIn) {
        this.bytesIn = bytesIn;
    }

    public long getBytesOut() {
        return bytesOut;
    }

    public void setBytesOut(long bytesOut) {
        this.bytesOut = bytesOut;
    }
}
