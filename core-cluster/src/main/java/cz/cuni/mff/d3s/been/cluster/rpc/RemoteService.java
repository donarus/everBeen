package cz.cuni.mff.d3s.been.cluster.rpc;

import com.hazelcast.spi.ManagedService;
import com.hazelcast.spi.NodeEngine;

import java.util.Properties;

public abstract class RemoteService implements ManagedService, com.hazelcast.spi.RemoteService {
    protected NodeEngine nodeEngine;

    @Override
    public void init(NodeEngine nodeEngine, Properties properties) {
        this.nodeEngine = nodeEngine;
    }

    @Override
    public void reset() {

    }

    @Override
    public void shutdown(boolean terminate) {

    }

    @Override
    public void destroyDistributedObject(String objectName) {

    }

    public abstract String getServiceName();
}
