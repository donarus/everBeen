package cz.cuni.mff.d3s.been.commons;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = JavaBpk.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = JavaBpk.class, name = "java")
})
public abstract class Bpk {

    private final String groupId;
    private final String bpkId;
    private final String version;

    public Bpk(
            String groupId,
            String bpkId,
            String version
    ) {
        this.groupId = groupId;
        this.bpkId = bpkId;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getBpkId() {
        return bpkId;
    }

    public String getVersion() {
        return version;
    }

}