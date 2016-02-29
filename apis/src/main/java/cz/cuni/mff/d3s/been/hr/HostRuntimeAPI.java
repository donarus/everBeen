package cz.cuni.mff.d3s.been.hr;

import cz.cuni.mff.d3s.been.commons.TaskDefinition;

public interface HostRuntimeAPI {
    String getId();
    void runTask(TaskDefinition taskDefinition) throws HostRuntimeException;
}
