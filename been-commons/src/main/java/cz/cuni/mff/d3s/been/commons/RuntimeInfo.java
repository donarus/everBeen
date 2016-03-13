package cz.cuni.mff.d3s.been.commons;

import java.io.Serializable;
import java.util.List;

public class RuntimeInfo implements Serializable {

    private String id;
    private String workingDirectory;
    private String tasksWorkingDirectory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public String getTasksWorkingDirectory() {
        return tasksWorkingDirectory;
    }

    public void setTasksWorkingDirectory(String tasksWorkingDirectory) {
        this.tasksWorkingDirectory = tasksWorkingDirectory;
    }

}
