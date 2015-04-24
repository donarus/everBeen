package cz.cuni.mff.d3s.been.softwarerepository.http

import cz.cuni.mff.d3s.been.pluger.IPluginActivator
import cz.cuni.mff.d3s.been.pluger.IServiceRegistrator
import cz.cuni.mff.d3s.been.softwarerepository.SoftwareRepositoryClient

/**
 * Created by donarus on 22.4.15.
 */
class HttpSoftwareRepositoryActivator implements IPluginActivator {

    private static final String CONFIG_KEY_PREFIX = "software-repository-http"

    private static final String START_SERVER_KEY = "${CONFIG_KEY_PREFIX}.start-server"

    private static final boolean START_SERVER_BY_DEFAULT = false

    private boolean startServerOnThisNode

    private HttpSoftwareRepository httpSoftwareRepository

    private HttpSoftwareRepositoryClient httpSoftwareRepositoryClient

    @Override
    void configure(Map<String, String> configuration) {
        startServerOnThisNode = Boolean.valueOf(configuration.get(START_SERVER_KEY, START_SERVER_BY_DEFAULT))
    }

    @Override
    void activate(IServiceRegistrator registry) {
        if (startServerOnThisNode) {
            httpSoftwareRepository = registry.registerService(HttpSoftwareRepository)
        }
        httpSoftwareRepositoryClient = registry.registerService(SoftwareRepositoryClient, HttpSoftwareRepositoryClient)
    }

    @Override
    void initialize() {
    }

    @Override
    void start() {
        if (startServerOnThisNode) {
            httpSoftwareRepository.start()
        }
    }

    @Override
    void notifyStarted() {

    }
}
