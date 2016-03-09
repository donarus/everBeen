package cz.cuni.mff.d3s.been.swrep.api;

import cz.cuni.mff.d3s.been.service.rpc.RemoteServiceDefinition;

public class SoftwareRepositoryAPIServiceDefinition extends RemoteServiceDefinition {
    public SoftwareRepositoryAPIServiceDefinition() {
        super(SoftwareRepositoryAPIService.class, SoftwareRepositoryAPIService.SERVICE_NAME);
    }
}
