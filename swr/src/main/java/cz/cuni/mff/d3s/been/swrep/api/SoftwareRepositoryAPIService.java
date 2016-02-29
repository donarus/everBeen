package cz.cuni.mff.d3s.been.swrep.api;

import com.hazelcast.core.DistributedObject;
import cz.cuni.mff.d3s.been.service.rpc.RemoteService;

public class SoftwareRepositoryAPIService extends RemoteService {
    public static final String SERVICE_NAME = "SoftwareRepositoryAPI";

    @Override
    public DistributedObject createDistributedObject(String objectName) {
        return new SWRAPIServiceProxy(objectName, nodeEngine, this);
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }
}
