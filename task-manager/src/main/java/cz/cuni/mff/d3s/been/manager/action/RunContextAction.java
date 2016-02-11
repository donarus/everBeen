package cz.cuni.mff.d3s.been.manager.action;

import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.cluster.context.TaskContexts;

/**
 * @author Martin Sixta
 */
public class RunContextAction implements TaskAction {
	private final TaskContexts taskContexts;
	private final String contextId;

	/**
	 * Creates RunContextAction.
	 * 
	 * @param ctx
	 *          connection to the cluster
	 * @param contextId
	 *          targeted context id
	 */
	public RunContextAction(final TaskContexts taskContexts, final String contextId) {
		this.taskContexts = taskContexts;
		this.contextId = contextId;
	}

	@Override
	public void execute() throws TaskActionException {
		taskContexts.runContext(contextId);
	}
}
