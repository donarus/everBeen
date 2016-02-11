package cz.cuni.mff.d3s.been.cluster.rpc;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ReplicatedMap;
import com.hazelcast.nio.Address;
import com.hazelcast.spi.AbstractDistributedObject;
import com.hazelcast.spi.InvocationBuilder;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.spi.Operation;
import com.hazelcast.util.ExceptionUtil;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.Future;

public abstract class RemoteServiceProxy extends AbstractDistributedObject<RemoteService> {

    private final String name;
    private RemoteService remoteService;

    protected final ApplicationContext applicationContext;
    protected final HazelcastInstance hazelcastInstance;

    public RemoteServiceProxy(String name, NodeEngine nodeEngine, RemoteService remoteService) {
        super(nodeEngine, remoteService);
        this.name = name;
        this.remoteService = remoteService;
        this.applicationContext = ACP.getApplicationContext();
        this.hazelcastInstance = applicationContext.getBean(HazelcastInstance.class);
    }

    @Override
    public String getServiceName() {
        return remoteService.getServiceName();
    }

    @Override
    public String getName() {
        return name;
    }

    protected final <T> T invokeOperation(Operation operation) {
        ReplicatedMap<String, Address> addresses = hazelcastInstance.getReplicatedMap("HR_ADDRESSES");
        Address address = addresses.get(name);
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
        throw new RuntimeException("Unknown host '" + name + "'");
    }

    protected <T> T getBean(String name) {
        return (T) applicationContext.getBean("hostRuntimeId");
    }

    protected <T> T getBean(Class<T> clazz) {
        return (T) applicationContext.getBean(clazz);
    }
}
