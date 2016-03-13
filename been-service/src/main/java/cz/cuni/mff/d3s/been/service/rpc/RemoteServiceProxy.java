package cz.cuni.mff.d3s.been.service.rpc;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.nio.Address;
import com.hazelcast.spi.AbstractDistributedObject;
import com.hazelcast.spi.InvocationBuilder;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.spi.Operation;
import com.hazelcast.util.ExceptionUtil;
import cz.cuni.mff.d3s.been.commons.MapNames;
import cz.cuni.mff.d3s.been.service.ACP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.concurrent.Future;

public abstract class RemoteServiceProxy extends AbstractDistributedObject<RemoteService> {

    @Autowired
    protected HazelcastInstance hazelcastInstance;

    private final String nodeName;

    private final RemoteService remoteService;

    public RemoteServiceProxy(String nodeName, NodeEngine nodeEngine, RemoteService remoteService) {
        super(nodeEngine, remoteService);
        this.nodeName = nodeName;
        this.remoteService = remoteService;
        ApplicationContext applicationContext = ACP.getApplicationContext();
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public String getServiceName() {
        return remoteService.getServiceName();
    }

    @Override
    public String getName() {
        return nodeName;
    }

    protected final <T> T invokeOperation(Operation operation) {
        Map<String, Address> addresses = hazelcastInstance.getMap(MapNames.NODE_ADDRESSES);
        Address address = addresses.get(nodeName);
        if (address != null) {
            NodeEngine nodeEngine = getNodeEngine();
            InvocationBuilder builder = nodeEngine.getOperationService()
                    .createInvocationBuilder(getServiceName(), operation, address);
            try {
                final Future<T> future = builder.invoke();
                return future.get();
            } catch (Exception e) {
                throw ExceptionUtil.rethrow(e);
            }
        }
        throw new RuntimeException("Unknown host '" + nodeName + "'");
    }
}
