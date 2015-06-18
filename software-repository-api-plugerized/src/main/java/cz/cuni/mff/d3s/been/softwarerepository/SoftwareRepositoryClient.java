package cz.cuni.mff.d3s.been.softwarerepository;

import java.util.Collection;
import java.util.List;

/**
 * Created by donarus on 22.4.15.
 */
public interface SoftwareRepositoryClient {

    boolean store(SoftwareRepositoryArtifact artifact);

    SoftwareRepositoryArtifact get(SoftwareRepositoryArtifactDescriptor descriptor);

    boolean delete(SoftwareRepositoryArtifactDescriptor descriptor);

    boolean contains(SoftwareRepositoryArtifactDescriptor descriptor);

    Collection<SoftwareRepositoryArtifactDescriptor> list();

}
