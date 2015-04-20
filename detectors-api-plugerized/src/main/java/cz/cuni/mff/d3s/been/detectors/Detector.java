package cz.cuni.mff.d3s.been.detectors;

/**
 * Created by donarus on 18.4.15.
 */
public interface Detector {

    MachineInfo detectMachineInfo();

    MonitorSample generateSample();

}
