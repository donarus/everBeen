package cz.cuni.mff.d3s.been.swrep.api;

import cz.cuni.mff.d3s.been.service.rpc.RemoteServiceRegistrator;

public class SoftwareRepositoryAPIServiceRegistrator extends RemoteServiceRegistrator {
    public SoftwareRepositoryAPIServiceRegistrator() {
        super(SoftwareRepositoryAPIService.class, SoftwareRepositoryAPIService.SERVICE_NAME);
    }
}
