package cz.cuni.mff.d3s.been.softwarerepository.http

import cz.cuni.mff.d3s.been.softwarerepository.SoftwareRepositoryArtifact
import cz.cuni.mff.d3s.been.softwarerepository.SoftwareRepositoryArtifactDescriptor
import cz.cuni.mff.d3s.been.softwarerepository.SoftwareRepositoryClient

/**
 * Created by donarus on 22.4.15.
 */
class HttpSoftwareRepositoryClient implements SoftwareRepositoryClient {

    HttpSoftwareRepository httpSoftwareRepository

    @Override
    boolean put(SoftwareRepositoryArtifact artifact) {
        return false
    }

    @Override
    SoftwareRepositoryArtifact get(SoftwareRepositoryArtifactDescriptor descriptor) {
        return null
    }

    @Override
    boolean delete(SoftwareRepositoryArtifactDescriptor descriptor) {
        return false
    }

    @Override
    boolean contains(SoftwareRepositoryArtifactDescriptor descriptor) {
        return false
    }

    @Override
    Collection<SoftwareRepositoryArtifactDescriptor> list() {
        return null
    }
}
