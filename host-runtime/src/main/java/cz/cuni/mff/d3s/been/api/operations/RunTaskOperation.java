package cz.cuni.mff.d3s.been.api.operations;

import com.hazelcast.spi.AbstractOperation;
import cz.cuni.mff.d3s.been.commons.monitorsample.TaskDefinition;
import cz.cuni.mff.d3s.been.processmanager.ProcessManager;
import cz.cuni.mff.d3s.been.service.rpc.ACP;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;

public class RunTaskOperation extends AbstractOperation {
    private final TaskDefinition taskDefinition;

    public RunTaskOperation(TaskDefinition taskDefinition) {
        this.taskDefinition = taskDefinition;
    }

    @Override
    public void run() throws Exception {
        ApplicationContext applicationContext = ACP.getApplicationContext();
        TaskExecutor executor = applicationContext.getBean(TaskExecutor.class);
        ProcessManager processManager = applicationContext.getBean(ProcessManager.class);
        executor.execute(() -> processManager.runTask(taskDefinition));
    }

    @Override
    public boolean returnsResponse() {
        return false;
    }

}
