package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;

/**
 * Created by donarus on 18.4.15.
 */
public final class NetworkInterfaceInfo implements Serializable {

    private static final long serialVersionUID = -8531961721375724667L;

    private String name;

    private String hwaddr;

    private String type;

    private long mtu;

    private String address;

    private String netmask;

    private String broadcast;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHwaddr() {
        return hwaddr;
    }

    public void setHwaddr(String hwaddr) {
        this.hwaddr = hwaddr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getMtu() {
        return mtu;
    }

    public void setMtu(long mtu) {
        this.mtu = mtu;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }

}
