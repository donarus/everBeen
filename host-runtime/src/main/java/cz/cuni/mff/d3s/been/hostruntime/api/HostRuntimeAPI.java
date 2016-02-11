package cz.cuni.mff.d3s.been.hostruntime.api;

import cz.cuni.mff.d3s.been.commons.TaskDefinition;
import cz.cuni.mff.d3s.been.core.ri.RuntimeInfo;

public interface HostRuntimeAPI {
    String getId();
    RuntimeInfo getRuntimeInfo() throws HostRuntimeAPIException;
    void runTask(TaskDefinition taskDefinition) throws HostRuntimeAPIException;
}
