package cz.cuni.mff.d3s.been.softwarerepository.http

import cz.cuni.mff.d3s.been.databus.hazelcast.IDataBus
import cz.cuni.mff.d3s.been.pluger.InjectService
import cz.cuni.mff.d3s.been.softwarerepository.SoftwareRepositoryArtifact

/**
 * Created by donarus on 22.4.15.
 */
class HttpSoftwareRepository {

    @InjectService
    private IDataBus databus

    public static final String SERVICE_NAME = "HttpSoftwareRepository"

    private static final String SW_REPOS_MAP_NAME = "${SERVICE_NAME}.registeredRepositories"

    private String uuid

    private Map<String, HttpSoftwareRepositoryInfo> swRepositoriesMap

    void start() {
        this.uuid = databus.generateClusterWideUUID(SERVICE_NAME)
        // FIXME ... find difference between replicated and distributed map structures in HC 3.0
        swRepositoriesMap = databus.getMap(SW_REPOS_MAP_NAME)
        // FIXME todo .... prepare data structure
        // FIXME           list existing packages
        // FIXME           ,,,
    }

    boolean store(SoftwareRepositoryArtifact softwareRepositoryArtifact) {
        false
    }
}
