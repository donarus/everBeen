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
 * Messages which handles new tasks.
 * 
 * @author Martin Sixta
 */
final class NewTaskMessage extends AbstractEntryTaskMessage {

	private Tasks tasks;
	private Runtimes runtimes;
	private Topics topics;

	/**
	 * Creates NewTaskMessage.
	 * 
	 * @param entry
	 *          targeted entry
	 */
	public NewTaskMessage(TaskEntry entry, Tasks tasks, Runtimes runtimes, Topics topics) {
		super(entry);
		this.tasks = tasks;
		this.runtimes = runtimes;
		this.topics = topics;
	}

	@Override
	public TaskAction createAction(ClusterContext ctx) {
		TaskState state = this.getEntry().getState();
		if (state == TaskState.SUBMITTED) {
			return Actions.createScheduleTaskAction(getEntry(), tasks, runtimes, topics);
		} else {
			return Actions.createNullAction();
		}
	}
}
