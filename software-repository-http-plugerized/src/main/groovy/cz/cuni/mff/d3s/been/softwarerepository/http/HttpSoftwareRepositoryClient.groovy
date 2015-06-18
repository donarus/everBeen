package cz.cuni.mff.d3s.been.softwarerepository.http

import cz.cuni.mff.d3s.been.databus.hazelcast.IDataBus
import cz.cuni.mff.d3s.been.pluger.InjectService
import cz.cuni.mff.d3s.been.softwarerepository.SoftwareRepositoryArtifact
import cz.cuni.mff.d3s.been.softwarerepository.SoftwareRepositoryArtifactDescriptor
import cz.cuni.mff.d3s.been.softwarerepository.SoftwareRepositoryClient

/**
 * Created by donarus on 22.4.15.
 */
class HttpSoftwareRepositoryClient implements SoftwareRepositoryClient {

    @InjectService
    private IDataBus databus

    @InjectService(serviceName = HttpSoftwareRepository.SERVICE_NAME)
    HttpSoftwareRepository localHttpSoftwareRepository

    private Map<String, HttpSoftwareRepositoryInfo> swRepositoriesMap

    void start() {
        swRepositoriesMap = databus.getMap(HttpSoftwareRepository.SW_REPOS_MAP_NAME)
    }

    @Override
    boolean store(SoftwareRepositoryArtifact artifact) {
        def resultLocal = storeLocal(artifact)
        def resultRemote = storeRemote(artifact)
        return resultLocal && resultRemote
    }

    private boolean storeLocal(SoftwareRepositoryArtifact artifact) {
        if(localHttpSoftwareRepository) {
            localHttpSoftwareRepository.store(artifact)
        } else {
            // LOG INFO .. local repository absent
        }
    }

    private boolean storeRemote(SoftwareRepositoryArtifact artifact) {
        def remoteRepos
        if (localHttpSoftwareRepository) {
            remoteRepos = swRepositoriesMap.subMap(swRepositoriesMap.keySet().remove(localHttpSoftwareRepository.uuid))
        } else {
            remoteRepos = swRepositoriesMap.values()
        }

        remoteRepos.each { HttpSoftwareRepositoryInfo info ->

        }
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
