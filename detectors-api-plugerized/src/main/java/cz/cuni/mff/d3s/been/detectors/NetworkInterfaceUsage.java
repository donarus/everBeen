package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;

/**
 * Created by donarus on 20.4.15.
 */
public final class NetworkInterfaceUsage implements Serializable {

    private static final long serialVersionUID = -2997054421029558493L;

    private String name;

    private long bytesOut;

    private long bytesIn;

    public String getName() {
        return name;
    }

    public NetworkInterfaceUsage setName(String name) {
        this.name = name;
        return this;
    }

    public long getBytesOut() {
        return bytesOut;
    }

    public NetworkInterfaceUsage setBytesOut(long bytesOut) {
        this.bytesOut = bytesOut;
        return this;
    }

    public long getBytesIn() {
        return bytesIn;
    }

    public NetworkInterfaceUsage setBytesIn(long bytesIn) {
        this.bytesIn = bytesIn;
        return this;
    }

}
