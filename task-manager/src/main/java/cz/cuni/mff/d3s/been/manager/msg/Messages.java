package cz.cuni.mff.d3s.been.manager.msg;

import cz.cuni.mff.d3s.been.cluster.context.*;
import cz.cuni.mff.d3s.been.core.task.TaskEntry;

import java.util.Properties;

/**
 * Factory for {@link TaskMessage}s.
 * 
 * 
 * @author Martin Sixta
 */
public class Messages {
	/**
	 * Creates RunContextMessage implementation
	 * 
	 * @param contextId
	 *          targeted context id
	 * @return RunContextMessage implementation
	 */
	public static TaskMessage createRunContextMessage(String contextId, TaskContexts taskContexts) {
		return new RunContextMessage(contextId, taskContexts);
	}

	/**
	 * Creates TaskChangedMessage implementation
	 * 
	 * @param entry
	 *          targeted task entry
	 * @return TaskChangedMessage implementation
	 */
	public static TaskMessage createTaskChangedMessage(TaskEntry entry, TaskContexts taskContexts,
													   Benchmarks benchmarks, Tasks tasks, Runtimes runtimes,
													   Topics topics, Properties properties) {
		return new TaskChangedMessage(entry, taskContexts, benchmarks, tasks, runtimes, topics, properties);
	}

	/**
	 * Creates NewTaskMessage implementation.
	 * 
	 * @param entry
	 *          targeted task entry
	 * @return NewTaskMessage implementation
	 */
	public static TaskMessage createNewTaskMessage(TaskEntry entry, Tasks tasks, Runtimes runtimes, Topics topics) {
		return new NewTaskMessage(entry, tasks, runtimes, topics);
	}

	/**
	 * Creates ScheduleTaskMessage implementation.
	 * 
	 * @param entry
	 *          targeted task entry
	 * @return ScheduleTaskMessage implementation
	 */
	public static TaskMessage createScheduleTaskMessage(TaskEntry entry, Tasks tasks, Runtimes runtimes, Topics topics) {
		return new ScheduleTaskMessage(entry, tasks, runtimes, topics);
	}

	/**
	 * Creates RescheduleTaskMessage implementation.
	 * 
	 * @param entry
	 *          targeted task entry
	 * @return RescheduleTaskMessage implementation
	 */
	public static TaskMessage createRescheduleTaskMessage(TaskEntry entry, Tasks tasks, Runtimes runtimes, Topics topics) {
		return new RescheduleTaskMessage(entry, tasks, runtimes, topics);
	}

	/**
	 * Creates CheckSchedulabilityMessage implementation.
	 * 
	 * @param entry
	 *          targeted task entry
	 * @return CheckSchedulabilityMessage implementation
	 */
	public static TaskMessage createCheckSchedulabilityMessage(TaskEntry entry, Tasks tasks, Runtimes runtimes, Topics topics) {
		return new CheckSchedulabilityMessage(entry, tasks, runtimes, topics);
	}

	/**
	 * Creates AbortTaskMessage implementation.
	 * 
	 * @param entry
	 *          targeted task entry
	 * @param reasonFormat
	 *          reason message format
	 * @param args
	 *          arguments for the format
	 * @return AbortTaskMessage implementation
	 */
	public static TaskMessage createAbortTaskMessage(Tasks tasks, TaskEntry entry, String reasonFormat, Object... args) {
		return new AbortTaskMessage(tasks, entry, reasonFormat, args);
	}
}
