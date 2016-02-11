package cz.cuni.mff.d3s.been.manager.action;

import cz.cuni.mff.d3s.been.cluster.context.*;
import cz.cuni.mff.d3s.been.core.task.TaskEntry;

import java.util.Properties;

/**
 * 
 * Factory for {@link TaskAction}s.
 * 
 * @author Martin Sixta
 */
public class Actions {

	/**
	 * Creates action which does nothing.
	 * 
	 * @return action which does nothing
	 */
	public static TaskAction createNullAction() {
		return new NullAction();
	}

	/**
	 * Creates abort action.
	 * 
	 * @param tasks
	 *          connection to the cluster
	 * @param entry
	 *          {@link TaskEntry} of a task to abort
	 * @param msg
	 *          reason for the abortion of the task
	 * @return action which will abort the task
	 */
	public static TaskAction createAbortAction(final Tasks tasks, final TaskEntry entry, final String msg) {
		return new AbortTaskAction(tasks, entry, msg);
	}

	/**
	 * Creates actions which changes owner of the task.
	 *
	 * @param entry
	 *          {@link TaskEntry} of a task to change owner of
	 * @return action which will change owner of the task
	 */
	public static TaskAction createScheduleTaskAction(final TaskEntry entry, Tasks tasks, Runtimes runtimes, Topics topics) {
		return new ScheduleTaskAction(entry, tasks, runtimes, topics);
	}

	/**
	 * Creates actions which checks context of a task/ .
	 *
	 * @param entry
	 *          {@link TaskEntry} of a task to check context for
	 * @return action which will check context of the task
	 */
	public static TaskAction createTaskContextCheckerAction(final TaskEntry entry, TaskContexts taskContext, Tasks tasks) {
		return new TaskContextCheckerAction(entry, taskContext, tasks);
	}

	/**
	 * Creates actions which will resubmit a benchmark task.
	 * 
	 * @param entry
	 *          {@link TaskEntry} of a benchmark task to resubmit
	 * @return action which will resubmit the benchmark task
	 */
	public static TaskAction createResubmitBenchmarkAction(final TaskEntry entry, Benchmarks benchmarks, Tasks tasks, Properties properties) {
		return new ResubmitBenchmarkAction(entry, benchmarks, tasks, properties);
	}

	/**
	 * Creates actions which will run a task context.
	 * 
	 * @param taskContexts
	 * @param contextId
	 *          ID of the context to run
	 * @return action which will run the context
	 */
	public static TaskAction createRunContextAction(final TaskContexts taskContexts, final String contextId) {
		return new RunContextAction(taskContexts, contextId);

	}
}
