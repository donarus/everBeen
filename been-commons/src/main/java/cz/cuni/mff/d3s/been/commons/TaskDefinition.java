package cz.cuni.mff.d3s.been.commons;

import java.io.Serializable;

public class TaskDefinition implements Serializable {
    private String name;

    private BpkPointer bpkPointer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BpkPointer getBpkPointer() {
        return bpkPointer;
    }

    public void setBpkPointer(BpkPointer bpkPointer) {
        this.bpkPointer = bpkPointer;
    }
}
