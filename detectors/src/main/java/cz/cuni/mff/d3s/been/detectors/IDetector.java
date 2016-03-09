package cz.cuni.mff.d3s.been.detectors;

import cz.cuni.mff.d3s.been.commons.nodeinfo.Filesystem;
import cz.cuni.mff.d3s.been.commons.nodeinfo.Hardware;
import cz.cuni.mff.d3s.been.commons.nodeinfo.Java;
import cz.cuni.mff.d3s.been.commons.nodeinfo.OperatingSystem;

import java.util.List;

public interface IDetector {
    Hardware detectHardware();

    OperatingSystem detectOperatingSystem();

    List<Filesystem> detectFilesystems();

    default Java detectJava() {
        Java java = new Java();

        java.setVersion(System.getProperty("java.version"));
        java.setVendor(System.getProperty("java.vendor"));
        java.setVersion(System.getProperty("java.version"));
        java.setRuntimeName(System.getProperty("java.runtime.name"));
        java.setVmVersion(System.getProperty("java.vm.version"));
        java.setVmVendor(System.getProperty("java.vm.vendor"));
        java.setRuntimeVersion(System.getProperty("java.runtime.version"));
        java.setSpecificationVersion(System.getProperty("java.specification.version"));

        return java;
    }
}
