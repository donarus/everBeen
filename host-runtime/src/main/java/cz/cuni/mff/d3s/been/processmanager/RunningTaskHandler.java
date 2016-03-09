package cz.cuni.mff.d3s.been.processmanager;

import cz.cuni.mff.d3s.been.commons.bpk.Bpk;
import cz.cuni.mff.d3s.been.commons.monitorsample.TaskDefinition;
import org.apache.commons.exec.CommandLine;

import java.io.File;
import java.util.Date;

public class RunningTaskHandler {

    private TaskDefinition taskDefinition;

    private Date creationTime;

    private String uuid;

    private File taskWorkDir;

    private File taskTempDir;

    private File taskDataDir;
    private Bpk bpk;
    private CommandLine commandLine;

    public RunningTaskHandler setTaskDefinition(TaskDefinition taskDefinition) {
        this.taskDefinition = taskDefinition;
        return this;
    }

    public TaskDefinition getTaskDefinition() {
        return taskDefinition;
    }

    public RunningTaskHandler setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public RunningTaskHandler setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getUUID() {
        return uuid;
    }

    public RunningTaskHandler setTaskWorkDir(File taskWorkDir) {
        this.taskWorkDir = taskWorkDir;
        return this;
    }

    public File getTaskWorkDir() {
        return taskWorkDir;
    }

    public RunningTaskHandler setTaskTempDir(File taskTempDir) {
        this.taskTempDir = taskTempDir;
        return this;
    }

    public File getTaskTempDir() {
        return taskTempDir;
    }

    public RunningTaskHandler setTaskDataDir(File taskDataDir) {
        this.taskDataDir = taskDataDir;
        return this;
    }

    public File getTaskDataDir() {
        return taskDataDir;
    }

    public RunningTaskHandler setBpk(Bpk bpk) {
        this.bpk = bpk;
        return this;
    }

    public Bpk getBpk() {
        return bpk;
    }

    public RunningTaskHandler setCommandLine(CommandLine commandLine) {
        this.commandLine = commandLine;
        return this;
    }

    public CommandLine getCommandLine() {
        return commandLine;
    }
}
