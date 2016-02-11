package cz.cuni.mff.d3s.been.hostruntime.api;

import com.hazelcast.spi.NodeEngine;
import cz.cuni.mff.d3s.been.cluster.rpc.RemoteService;
import cz.cuni.mff.d3s.been.cluster.rpc.RemoteServiceProxy;
import cz.cuni.mff.d3s.been.commons.TaskDefinition;
import cz.cuni.mff.d3s.been.core.ri.RuntimeInfo;
import cz.cuni.mff.d3s.been.hostruntime.processmanager.ProcessManager;
import org.apache.commons.lang3.concurrent.ConcurrentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static java.util.concurrent.Executors.newCachedThreadPool;

public class HostRuntimeAPIServiceProxy extends RemoteServiceProxy implements HostRuntimeAPI {

    private static final Logger log = LoggerFactory.getLogger(HostRuntimeAPIServiceProxy.class);

    private Executor threadPool = newCachedThreadPool();

    public HostRuntimeAPIServiceProxy(String name, NodeEngine nodeEngine, RemoteService remoteService) {
        super(name, nodeEngine, remoteService);
    }

    @Override
    public String getId() {
        return getBean("hostRuntimeId");
    }

    @Override
    public RuntimeInfo getRuntimeInfo() throws HostRuntimeAPIException {
        try {
            return ConcurrentUtils.constantFuture(getBean(RuntimeInfo.class)).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Service was unable to retrieve runtime info.", e);
            throw new HostRuntimeAPIException(e);
        }
    }

    @Override
    public void runTask(TaskDefinition taskDefinition) throws HostRuntimeAPIException {
        threadPool.execute(() -> getBean(ProcessManager.class).runTask(taskDefinition));
    }

}
