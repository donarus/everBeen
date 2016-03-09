package cz.cuni.mff.d3s.been.node;

import com.hazelcast.core.DistributedObject;
import cz.cuni.mff.d3s.been.service.rpc.RemoteService;

public class SampleRemoteService extends RemoteService {
    @Override
    public String getServiceName() {
        return "SampleRemoteService";
    }

    @Override
    public DistributedObject createDistributedObject(String objectName) {
        return new SampleRemoteServiceProxy(objectName, nodeEngine, this);
    }

}
