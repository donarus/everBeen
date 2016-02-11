package cz.cuni.mff.d3s.been.commons;

import java.util.List;

public class NetworkInterface {

    private String name;
    private String hwaddr;
    private String type;
    private long mtu;
    private String netmask;
    private String broadcast;
    private List<String> address;

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

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }
}
