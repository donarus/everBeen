package cz.cuni.mff.d3s.been.processmanager;

import cz.cuni.mff.d3s.been.commons.bpk.Bpk;
import cz.cuni.mff.d3s.been.commons.TaskDefinition;
import cz.cuni.mff.d3s.been.commons.utils.BpkUtil;
import cz.cuni.mff.d3s.been.service.BeenUUIDGenerator;
import org.apache.commons.exec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Manages all Host Runtime task processes.
 * <p/>
 * All good names taken, so 'Process' is used.
 *
 * @author Martin Sixta
 * @author donarus
 */
public class ProcessManager {
    private static final Logger log = LoggerFactory.getLogger(ProcessManager.class);

    private static final int CHECK_LOCK_TIMEOUT = 1;


    @Autowired
    private BeenUUIDGenerator uuidGenerator;

    @Autowired
    private CmdLineBuilder cmdLineBuilder;

    @Autowired
    @Qualifier("hostRuntimeTasksWorkingDirectory")
    private File tasksWrkDir;

    private final Lock checkResourcesLock = new ReentrantLock();

    private List<RunningTaskHandler> taskHandlers = Collections.synchronizedList(new ArrayList<>());

    public void runTask(TaskDefinition task) {
        RunningTaskHandler handler;

        try {
            checkResourcesLock.tryLock(CHECK_LOCK_TIMEOUT, TimeUnit.SECONDS);
            boolean canRun = checkResources(task);
            if (canRun) {
                handler = createHandler(task);
                taskHandlers.add(handler);
                System.out.println("run");
            } else {
                System.out.println("cannot run");
                return;
            }
        } catch (InterruptedException e) {
            // FIXME
            return;
        } finally {
            checkResourcesLock.unlock();
        }

        try {
            handler = prepareDirectoryStructure(handler);
            handler = loadBpk(handler);
            handler = constructCommandLine(handler);

            startTask(handler); // and wait for finish
        } catch (Exception e) {
            // FIXME
        }

        try {
            checkResourcesLock.lock();
            taskHandlers.remove(handler);
        } finally {
            checkResourcesLock.unlock();
        }
    }

    private RunningTaskHandler createHandler(TaskDefinition task) {
        return new RunningTaskHandler()
                .setTaskDefinition(task)
                .setCreationTime(now())
                .setUUID(newId());
    }

    private String newId() {
        return uuidGenerator.generate();
    }

    private Date now() {
        return new Date();
    }

    private boolean checkResources(TaskDefinition taskDefinition) {
        return (taskHandlers.isEmpty()); // FIXME this is fake right now
    }

    private RunningTaskHandler prepareDirectoryStructure(RunningTaskHandler handler) {
        System.out.println("Using " + tasksWrkDir.toString() + " as working directory");
        Path taskDirPath = tasksWrkDir.toPath().resolve("task_" + handler.getUUID() + "_" + new Date().getTime());

        File taskWorkDir = taskDirPath.resolve("work").toFile();
        File taskTempDir = taskDirPath.resolve("temp").toFile();
        File taskDataDir = taskDirPath.resolve("data").toFile();

        taskWorkDir.mkdirs();
        taskTempDir.mkdirs();
        taskDataDir.mkdirs();

        return handler
                .setTaskWorkDir(taskWorkDir)
                .setTaskTempDir(taskTempDir)
                .setTaskDataDir(taskDataDir);
    }

    private RunningTaskHandler loadBpk(RunningTaskHandler handler) {
        File bpkExtracted = handler.getTaskDataDir().toPath().resolve("bpk_unpacked").toFile();
        try {
            BpkUtil.extract(handler.getTaskDefinition().getBpkFile(), bpkExtracted);
            Bpk bpk = BpkUtil.loadUnpackedBpk(bpkExtracted);
            handler.setBpk(bpk);
        } catch (IOException e) {
            // FIXME
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return handler;
    }

    private RunningTaskHandler constructCommandLine(RunningTaskHandler handler) {
        return handler.setCommandLine(cmdLineBuilder.build(handler));
    }

    private RunningTaskHandler startTask(RunningTaskHandler handler) {
        Executor executor = new DefaultExecutor();
        executor.setExitValue(0);
        executor.setStreamHandler(new PumpStreamHandler());
        ExecuteWatchdog watchdog = new ExecuteWatchdog(1000); // fixme .. can be set from configuration ... kills process after 10 seconds
        executor.setWatchdog(watchdog);


        try {
            executor.execute(handler.getCommandLine(), new DefaultExecuteResultHandler());
        } catch (IOException e) {
            // fixme ooops, something bad happened
        }
        return handler;
    }

}
