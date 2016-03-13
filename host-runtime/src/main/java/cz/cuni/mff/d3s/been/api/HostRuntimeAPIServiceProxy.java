package cz.cuni.mff.d3s.been.api;

import com.hazelcast.spi.NodeEngine;
import cz.cuni.mff.d3s.been.api.operations.GetIdOperation;
import cz.cuni.mff.d3s.been.api.operations.RunTaskOperation;
import cz.cuni.mff.d3s.been.hr.HostRuntimeAPI;
import cz.cuni.mff.d3s.been.hr.HostRuntimeException;
import cz.cuni.mff.d3s.been.service.rpc.RemoteService;
import cz.cuni.mff.d3s.been.service.rpc.RemoteServiceProxy;
import cz.cuni.mff.d3s.been.commons.monitorsample.TaskDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

import static java.util.concurrent.Executors.newCachedThreadPool;

public class HostRuntimeAPIServiceProxy extends RemoteServiceProxy implements HostRuntimeAPI {

    public HostRuntimeAPIServiceProxy(String nodeName, NodeEngine nodeEngine, RemoteService remoteService) {
        super(nodeName, nodeEngine, remoteService);
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
