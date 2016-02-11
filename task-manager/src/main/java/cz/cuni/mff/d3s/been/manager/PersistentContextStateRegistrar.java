package cz.cuni.mff.d3s.been.manager;

import cz.cuni.mff.d3s.been.cluster.context.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.core.persistence.Entities;
import cz.cuni.mff.d3s.been.core.task.TaskContextState;
import cz.cuni.mff.d3s.been.persistence.DAOException;
import cz.cuni.mff.d3s.been.persistence.task.PersistentContextState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Context state persistence helper
 * 
 * @author darklight
 */
@Component
class PersistentContextStateRegistrar {

	private static final Logger log = LoggerFactory.getLogger(PersistentContextStateRegistrar.class);

	@Autowired
	private Persistence persistence;

	/**
	 * Processes context state change.
	 * 
	 * @param contextId
	 *          contextId
	 * @param benchmarkId
	 *          benchmarkId
	 * @param state
	 *          current state of the context
	 */
	public void processContextStateChange(String contextId, String benchmarkId, TaskContextState state) {
		final PersistentContextState persistentState = new PersistentContextState();
		persistentState.setContextId(contextId);
		persistentState.setBenchmarkId(benchmarkId);
		persistentState.setContextState(state);

		try {
			persistence.asyncPersist(Entities.OUTCOME_CONTEXT.getId(), persistentState);
		} catch (DAOException e) {
			log.error("Failed to persist context state '{}' for context '{}'", state.name(), contextId, e);
		}
	}
}
