package cz.cuni.mff.d3s.been.hostruntime;

import cz.cuni.mff.d3s.been.detectors.MachineInfo;

import java.io.Serializable;

/**
 * Created by donarus on 22.3.15.
 */
final class HostRuntimeInfo implements Serializable {

    private static final long serialVersionUID = -882022325318690194L;

    private String uuid;

    private MachineInfo machineInfo;

    public String getUuid() {
        return uuid;
    }

    public HostRuntimeInfo setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public MachineInfo getMachineInfo() {
        return machineInfo;
    }

    public HostRuntimeInfo setMachineInfo(MachineInfo machineInfo) {
        this.machineInfo = machineInfo;
        return this;
    }
}
