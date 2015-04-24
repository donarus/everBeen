package cz.cuni.mff.d3s.been.softwarerepository;

import java.io.Serializable;

/**
 * Created by donarus on 22.4.15.
 */
public class SoftwareRepositoryArtifactDescriptor implements Serializable {

    private static final long serialVersionUID = 6601907729971471292L;

    private String artifactId;

    private String groupId;

    private String version;

    private String repositoryId;

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }
}
