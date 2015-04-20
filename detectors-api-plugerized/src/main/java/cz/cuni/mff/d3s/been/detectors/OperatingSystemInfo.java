package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;

/**
 * Created by donarus on 18.4.15.
 */
public final class OperatingSystemInfo implements Serializable {

    private static final long serialVersionUID = 1748604333352005310L;

    private String name;

    private String version;

    private String architecture;

    private String vendor;

    private String vendorVersion;

    private String dataModel;

    private String endian;

    public String getName() {
        return name;
    }

    public OperatingSystemInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public OperatingSystemInfo setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getArchitecture() {
        return architecture;
    }

    public OperatingSystemInfo setArchitecture(String architecture) {
        this.architecture = architecture;
        return this;
    }

    public String getVendor() {
        return vendor;
    }

    public OperatingSystemInfo setVendor(String vendor) {
        this.vendor = vendor;
        return this;
    }

    public String getVendorVersion() {
        return vendorVersion;
    }

    public OperatingSystemInfo setVendorVersion(String vendorVersion) {
        this.vendorVersion = vendorVersion;
        return this;
    }

    public String getDataModel() {
        return dataModel;
    }

    public OperatingSystemInfo setDataModel(String dataModel) {
        this.dataModel = dataModel;
        return this;
    }

    public String getEndian() {
        return endian;
    }

    public OperatingSystemInfo setEndian(String endian) {
        this.endian = endian;
        return this;
    }

}
