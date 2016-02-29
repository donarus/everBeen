package cz.cuni.mff.d3s.been.commons.bpk;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JavaBpk extends Bpk implements Serializable {

    private String jarFileName;

    private Map<String, File> libraries = new HashMap<>();

    private File jarFile;

    public JavaBpk(
            @JsonProperty(value = "groupId", required = true) String groupId,
            @JsonProperty(value = "bpkId", required = true) String bpkId,
            @JsonProperty(value = "version", required = true) String version,
            @JsonProperty(value = "jarFile", required = true) String jarFileName) {
        super(new BpkId(groupId, bpkId, version));
        this.jarFileName = jarFileName;
    }

    public String getJarFileName() {
        return jarFileName;
    }

    public Map<String, File> getLibraries() {
        return libraries;
    }

    public void addLibraries(Map<String, File> libraries) {
        this.libraries.putAll(libraries);
    }

    public File getJarFile() {
        return jarFile;
    }

    public void setJarFile(File jarFile) {
        this.jarFile = jarFile;
    }
}
