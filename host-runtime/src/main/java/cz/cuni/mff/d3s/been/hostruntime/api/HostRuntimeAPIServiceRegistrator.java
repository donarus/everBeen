package cz.cuni.mff.d3s.been.hostruntime.api;

import cz.cuni.mff.d3s.been.cluster.rpc.RemoteServiceRegistrator;
import org.springframework.stereotype.Component;

@Component
public class HostRuntimeAPIServiceRegistrator extends RemoteServiceRegistrator {
    public HostRuntimeAPIServiceRegistrator() {
        super(HostRuntimeAPIService.class, HostRuntimeAPIService.SERVICE_NAME);
    }
}
