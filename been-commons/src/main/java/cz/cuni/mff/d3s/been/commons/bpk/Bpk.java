package cz.cuni.mff.d3s.been.commons.bpk;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.cuni.mff.d3s.been.commons.bpk.BpkFile;
import cz.cuni.mff.d3s.been.commons.bpk.BpkId;
import cz.cuni.mff.d3s.been.commons.bpk.JavaBpk;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = JavaBpk.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = JavaBpk.class, name = "java")
})
public abstract class Bpk implements Serializable {

    private final BpkId bpkId;
    private BpkFile bpkFile;

    public Bpk(BpkId bpkId) {
        this.bpkId = bpkId;
    }

    public BpkId getBpkId() {
        return bpkId;
    }

    public void setBpkFile(BpkFile bpkFile) {
        this.bpkFile = bpkFile;
    }

    public BpkFile getBpkFile() {
        return bpkFile;
    }
}