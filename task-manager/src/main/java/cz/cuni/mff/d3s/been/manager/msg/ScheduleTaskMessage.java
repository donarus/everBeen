package cz.cuni.mff.d3s.been.manager.msg;

import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.cluster.context.Runtimes;
import cz.cuni.mff.d3s.been.cluster.context.Tasks;
import cz.cuni.mff.d3s.been.cluster.context.Topics;
import cz.cuni.mff.d3s.been.core.task.TaskEntry;
import cz.cuni.mff.d3s.been.core.task.TaskState;
import cz.cuni.mff.d3s.been.manager.action.Actions;
import cz.cuni.mff.d3s.been.manager.action.TaskAction;

/**
 * Message which handles task scheduling.
 * 
 * @author Martin Sixta
 */
final class ScheduleTaskMessage extends AbstractEntryTaskMessage {

	private final Tasks tasks;
	private Runtimes runtimes;
	private final Topics topics;

	/**
	 * Creates ScheduleTaskMessage
	 * 
	 * @param entry
	 *          targeted task entry
	 */
	public ScheduleTaskMessage(TaskEntry entry, Tasks tasks, Runtimes runtimes, Topics topics) {
		super(entry);
		this.tasks = tasks;
		this.runtimes = runtimes;
		this.topics = topics;
	}

	@Override
	public TaskAction createAction(ClusterContext ctx) {
		TaskState state = this.getEntry().getState();
		if (state == TaskState.SUBMITTED || state == TaskState.WAITING || state == TaskState.SCHEDULED) {
			return Actions.createScheduleTaskAction(getEntry(), tasks, runtimes, topics);
		} else {
			return Actions.createNullAction();
		}
	}
}
