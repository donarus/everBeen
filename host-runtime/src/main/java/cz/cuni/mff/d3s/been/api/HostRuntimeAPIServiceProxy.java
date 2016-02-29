package cz.cuni.mff.d3s.been.api;

import com.hazelcast.spi.NodeEngine;
import com.hazelcast.spi.Operation;
import cz.cuni.mff.d3s.been.api.operations.GetIdOperation;
import cz.cuni.mff.d3s.been.api.operations.RunTaskOperation;
import cz.cuni.mff.d3s.been.hr.HostRuntimeAPI;
import cz.cuni.mff.d3s.been.hr.HostRuntimeException;
import cz.cuni.mff.d3s.been.processmanager.ProcessManager;
import cz.cuni.mff.d3s.been.service.rpc.RemoteService;
import cz.cuni.mff.d3s.been.service.rpc.RemoteServiceProxy;
import cz.cuni.mff.d3s.been.commons.TaskDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

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
        return invokeOperation(new GetIdOperation());
    }

    @Override
    public void runTask(TaskDefinition taskDefinition) throws HostRuntimeException {
        invokeOperation(new RunTaskOperation(taskDefinition));
    }

}
