package cz.cuni.mff.d3s.been.api;

import cz.cuni.mff.d3s.been.core.LogMessage;
import cz.cuni.mff.d3s.been.core.ri.RuntimeInfo;
import cz.cuni.mff.d3s.been.core.task.TaskContextEntry;
import cz.cuni.mff.d3s.been.core.task.TaskEntry;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * User: donarus
 * Date: 4/27/13
 * Time: 11:40 AM
 */
public interface BeenApi {

	public Collection<TaskEntry> getTasks();

	public TaskEntry getTask(String id);

	public Collection<TaskContextEntry> getTaskContexts();

	public TaskContextEntry getTaskContext(String id);

	public Collection<RuntimeInfo> getRuntimes();

	public RuntimeInfo getRuntime(String id);

	public Collection<String> getLogSets();

	public Collection<LogMessage> getLogs(String setId);

}
