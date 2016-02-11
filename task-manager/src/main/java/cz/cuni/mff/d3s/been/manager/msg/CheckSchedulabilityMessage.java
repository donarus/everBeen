package cz.cuni.mff.d3s.been.manager.msg;

import static cz.cuni.mff.d3s.been.core.task.TaskState.ABORTED;
import static cz.cuni.mff.d3s.been.core.task.TaskState.FINISHED;

import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.cluster.context.Runtimes;
import cz.cuni.mff.d3s.been.cluster.context.Tasks;
import cz.cuni.mff.d3s.been.cluster.context.Topics;
import cz.cuni.mff.d3s.been.core.task.TaskEntry;
import cz.cuni.mff.d3s.been.core.task.TaskState;
import cz.cuni.mff.d3s.been.manager.action.Actions;
import cz.cuni.mff.d3s.been.manager.action.TaskAction;
import cz.cuni.mff.d3s.been.manager.selector.NoRuntimeFoundException;
import cz.cuni.mff.d3s.been.manager.selector.RuntimeSelectors;

/**
 * Message which checks scheduability of a task.
 * 
 * If a task can be scheduled an appropriate action should take place.
 * 
 * @author Martin Sixta
 */
public class CheckSchedulabilityMessage implements TaskMessage {
	private final TaskEntry entry;
	private Tasks tasks;
	private Runtimes runtimes;
	private Topics topics;

	/**
	 * Creates new CheckSchedulabilityMessage
	 * 
	 * @param entry
	 *          targeted entry
	 */
	public CheckSchedulabilityMessage(TaskEntry entry, Tasks tasks, Runtimes runtimes, Topics topics) {
		this.entry = entry;
		this.tasks = tasks;
		this.runtimes = runtimes;
		this.topics = topics;
	}

	@Override
	public TaskAction createAction(ClusterContext ctx) {

		if (isWaitingOnTask()) {
			return Actions.createNullAction();
		}

		try {
			RuntimeSelectors.fromEntry(entry, runtimes).select();
			return Actions.createScheduleTaskAction(entry, tasks, runtimes, topics);
		} catch (NoRuntimeFoundException e) {
			// do nothing, will have to wait
		}

		return Actions.createNullAction();
	}

	/**
	 * Checks whether the task is waiting on another task.
	 *
	 * @return true if the task is waiting on another task, false otherwise
	 */
	private boolean isWaitingOnTask() {
		final String taskDependency = entry.getTaskDependency();

		if (taskDependency == null || taskDependency.isEmpty()) {
			return false;
		}

		final TaskEntry task = tasks.getTask(taskDependency);

		if (task == null) {
			return false;
		} else {
			return !isTaskDone();
		}

	}

	/**
	 * Checks whether the task is done executing
	 * 
	 * @return whether the task is in ABORTED or FINISHED state
	 */
	private boolean isTaskDone() {
		final TaskState state = entry.getState();
		return (state == ABORTED) || (state == FINISHED);
	}
}
