package cz.cuni.mff.d3s.been.api;

import cz.cuni.mff.d3s.been.service.rpc.RemoteServiceRegistrator;
import org.springframework.stereotype.Component;

public class HostRuntimeAPIServiceRegistrator extends RemoteServiceRegistrator {
    public HostRuntimeAPIServiceRegistrator() {
        super(HostRuntimeAPIService.class, HostRuntimeAPIService.SERVICE_NAME);
    }
}
