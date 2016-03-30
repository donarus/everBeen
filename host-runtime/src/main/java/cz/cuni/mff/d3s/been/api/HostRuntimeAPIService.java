package cz.cuni.mff.d3s.been.api;

import com.hazelcast.core.DistributedObject;
import cz.cuni.mff.d3s.been.service.rpc.RemoteService;

public class HostRuntimeAPIService extends RemoteService {

    public static final String SERVICE_NAME = "HostRuntimeAPI";

    @Override
    public DistributedObject createDistributedObject(String nodeName) {
        return new HostRuntimeAPIServiceProxy(nodeName, nodeEngine, this);
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }
}