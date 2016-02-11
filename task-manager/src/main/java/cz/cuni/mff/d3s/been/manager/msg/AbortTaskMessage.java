package cz.cuni.mff.d3s.been.manager.msg;

import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.cluster.context.Tasks;
import cz.cuni.mff.d3s.been.core.task.TaskEntry;
import cz.cuni.mff.d3s.been.manager.action.Actions;
import cz.cuni.mff.d3s.been.manager.action.TaskAction;

/**
 * Message which drives abortion of a task.
 * 
 * @author Martin Sixta
 */
final class AbortTaskMessage extends AbstractEntryTaskMessage {
	private final String msg;
	private Tasks tasks;

	/**
	 * Creates new AbortTaskMessage
	 * 
	 * @param entry
	 *          the target entry
	 * @param reasonFormat
	 *          format of the reason message
	 * @param args
	 *          format's arguments
	 */
	public AbortTaskMessage(Tasks tasks, TaskEntry entry, String reasonFormat, Object... args) {
		super(entry);
		this.tasks = tasks;
		msg = String.format(reasonFormat, args);
	}

	@Override
	public TaskAction createAction(ClusterContext ctx) {
		return Actions.createAbortAction(tasks, getEntry(), msg);
	}
}
