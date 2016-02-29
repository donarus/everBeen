package cz.cuni.mff.d3s.been.commons;

import java.io.Serializable;
import java.util.List;

public class Hardware implements Serializable {

    private List<Cpu> cpu;
    private List<NetworkInterface> networkInterface;
    private Memory memory;

    public List<Cpu> getCpu() {
        return cpu;
    }

    public void setCpu(List<Cpu> cpu) {
        this.cpu = cpu;
    }

    public List<NetworkInterface> getNetworkInterface() {
        return networkInterface;
    }

    public void setNetworkInterface(List<NetworkInterface> networkInterface) {
        this.networkInterface = networkInterface;
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }
}
