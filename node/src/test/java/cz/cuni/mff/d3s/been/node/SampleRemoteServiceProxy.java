package cz.cuni.mff.d3s.been.node;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.spi.NodeEngine;
import cz.cuni.mff.d3s.been.service.rpc.RemoteService;
import cz.cuni.mff.d3s.been.service.rpc.RemoteServiceProxy;

public class SampleRemoteServiceProxy extends RemoteServiceProxy implements ISampleRemoteService {
    public SampleRemoteServiceProxy(String name, NodeEngine nodeEngine, RemoteService remoteService) {
        super(name, nodeEngine, remoteService);
    }

    @Override
    public String getSampleString() {
        return "sampleString";
    }
}
