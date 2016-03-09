package cz.cuni.mff.d3s.been.api;

import cz.cuni.mff.d3s.been.service.rpc.RemoteServiceDefinition;

public class HostRuntimeAPIServiceDefinition extends RemoteServiceDefinition {
    public HostRuntimeAPIServiceDefinition() {
        super(HostRuntimeAPIService.class, HostRuntimeAPIService.SERVICE_NAME);
    }
}
