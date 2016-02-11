package cz.cuni.mff.d3s.been.manager.msg;

import cz.cuni.mff.d3s.been.cluster.context.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.cuni.mff.d3s.been.core.task.TaskEntry;
import cz.cuni.mff.d3s.been.core.task.TaskState;
import cz.cuni.mff.d3s.been.core.task.TaskType;
import cz.cuni.mff.d3s.been.manager.action.Actions;
import cz.cuni.mff.d3s.been.manager.action.TaskAction;

import java.util.Properties;

/**
 * Messages which handles changes of task states.
 * 
 * @author Martin Sixta
 */
final class TaskChangedMessage extends AbstractEntryTaskMessage {

	private static final Logger log = LoggerFactory.getLogger(TaskChangedMessage.class);
	private final TaskEntry entry;
	private final TaskContexts taskContexts;
	private final Benchmarks benchmarks;
	private final Tasks tasks;
	private Runtimes runtimes;
	private Topics topics;
	private Properties properties;

	/**
	 * Creates TaskChangedMessage
	 * 
	 * @param entry
	 *          targeted task entry
	 */
	public TaskChangedMessage(TaskEntry entry, TaskContexts taskContexts, Benchmarks benchmarks,
							  Tasks tasks, Runtimes runtimes, Topics topics, Properties properties) {
		super(entry);
		this.entry = entry;
		this.taskContexts = taskContexts;
		this.benchmarks = benchmarks;
		this.tasks = tasks;
		this.runtimes = runtimes;
		this.topics = topics;
		this.properties = properties;
	}

	@Override
	public TaskAction createAction(ClusterContext ctx) {
		TaskState state = this.getEntry().getState();

		if (state == TaskState.SUBMITTED || state == TaskState.WAITING)
			return Actions.createScheduleTaskAction(getEntry(), tasks, runtimes, topics);

		if (this.getEntry().getTaskDescriptor().getType() == TaskType.BENCHMARK) {
			if (state == TaskState.ABORTED) {
				// a benchmark generator task has failed
				log.info("BENCHMARK GENERATOR TASK ID {} FAILED", this.getEntry().getId());
				return Actions.createResubmitBenchmarkAction(getEntry(), benchmarks, tasks, properties);
			}
		}

		if (state == TaskState.FINISHED || state == TaskState.ABORTED) {
			return Actions.createTaskContextCheckerAction(getEntry(), taskContexts, tasks);
		}

		return null;
	}
}
