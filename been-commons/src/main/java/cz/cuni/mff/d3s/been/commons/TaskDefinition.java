package cz.cuni.mff.d3s.been.commons;

import cz.cuni.mff.d3s.been.commons.bpk.BpkFile;

import java.io.Serializable;

public class TaskDefinition implements Serializable {
    private String name;

    private BpkFile bpkFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BpkFile getBpkFile() {
        return bpkFile;
    }

    public void setBpkFile(BpkFile bpkFile) {
        this.bpkFile = bpkFile;
    }
}
