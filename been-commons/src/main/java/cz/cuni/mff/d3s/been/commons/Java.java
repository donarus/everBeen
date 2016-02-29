package cz.cuni.mff.d3s.been.commons;

import java.io.Serializable;

public class Java implements Serializable {

    private String version;
    private String vendor;
    private String runtimeName;
    private String vmVersion;
    private String vmVendor;
    private String runtimeVersion;
    private String specificationVersion;

    public String getVersion() {
        return version;
    }

    public Java setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getVendor() {
        return vendor;
    }

    public Java setVendor(String vendor) {
        this.vendor = vendor;
        return this;
    }

    public String getRuntimeName() {
        return runtimeName;
    }

    public Java setRuntimeName(String runtimeName) {
        this.runtimeName = runtimeName;
        return this;
    }

    public String getVmVersion() {
        return vmVersion;
    }

    public Java setVmVersion(String vmVersion) {
        this.vmVersion = vmVersion;
        return this;
    }

    public String getVmVendor() {
        return vmVendor;
    }

    public Java setVmVendor(String vmVendor) {
        this.vmVendor = vmVendor;
        return this;
    }

    public String getRuntimeVersion() {
        return runtimeVersion;
    }

    public Java setRuntimeVersion(String runtimeVersion) {
        this.runtimeVersion = runtimeVersion;
        return this;
    }

    public String getSpecificationVersion() {
        return specificationVersion;
    }

    public Java setSpecificationVersion(String specificationVersion) {
        this.specificationVersion = specificationVersion;
        return this;
    }
}
