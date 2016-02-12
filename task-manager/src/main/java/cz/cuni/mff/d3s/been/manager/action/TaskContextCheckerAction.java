package cz.cuni.mff.d3s.been.manager.action;

import java.util.Collection;

import cz.cuni.mff.d3s.been.cluster.context.Tasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.IMap;
import com.hazelcast.query.SqlPredicate;

import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.cluster.context.TaskContexts;
import cz.cuni.mff.d3s.been.core.task.TaskContextEntry;
import cz.cuni.mff.d3s.been.core.task.TaskContextState;
import cz.cuni.mff.d3s.been.core.task.TaskEntry;
import cz.cuni.mff.d3s.been.core.task.TaskState;

/**
 * @author Kuba Brecka
 */
final class TaskContextCheckerAction implements TaskAction {


	/** task entry used for the context check */
	private final TaskEntry entry;
	private TaskContexts taskContexts;
	private Tasks tasks;

	/** format of sql query predicate for finding all unfinished tasks */
	private static final String QUERY_FORMAT = "taskContextId = '%s' AND ((state == %s) OR (state == %s))";

	/**
	 * Creates new context checker action.
	 *
	 * @param entry
	 *          targeted task entry
	 */
	public TaskContextCheckerAction(TaskEntry entry, TaskContexts taskContexts, Tasks tasks) {
		this.entry = entry;
		this.taskContexts = taskContexts;
		this.tasks = tasks;
	}

	/**
	 * Checks if the finished context of the task is completely finished (all
	 * tasks from it are finished or aborted). If yes, calls the context cleanup
	 * procedure.
	 * 
	 * Serialization for multiply cluster members is ensured by cluster locks.
	 * 
	 * @throws TaskActionException
	 */
	@Override
	public void execute() throws TaskActionException {
		final String taskContextId = entry.getTaskContextId();

		IMap<String, TaskContextEntry> contextsMap = taskContexts.getTaskContextsMap();
		IMap<String, TaskEntry> tasksMap = tasks.getTasksMap();

		// fetch the entry
		TaskContextEntry contextEntry = taskContexts.getTaskContext(taskContextId);

		// optimization: fist check, and if needed then lock and check again
		if (isContextDone(contextEntry)) {
			return;
		}

		// benchmarks context should not be destroyed at all
		if (contextEntry.isLingering()) {
			return;
		}

		try {
			contextsMap.lock(taskContextId); // LOCK BEGIN

			// must fetch again
			contextEntry = taskContexts.getTaskContext(taskContextId);

			if (isContextDone(contextEntry)) {
				return;
			}

			Collection<TaskEntry> values = tasksMap.values(getPredicate(taskContextId));

			boolean isFinished = (values.size() == contextEntry.getContainedTask().size());

			if (isFinished) {
				TaskContextState finalState = TaskContextState.FINISHED;

				if (isFailed(values)) {
					finalState = TaskContextState.FAILED;
				}
				contextEntry.setContextState(finalState);

				if (finalState == TaskContextState.FINISHED) {
					taskContexts.cleanupTaskContext(contextEntry);
				} else {
					taskContexts.putContextEntry(contextEntry);
				}
			}
		} finally {
			contextsMap.unlock(taskContextId); // LOCK END
		}

	}

	/**
	 * Creates SqlPredicate for finding all unfinished tasks.
	 * 
	 * @param contextId
	 *          ID of the contexts tasks belong to
	 * 
	 * @return SqlPredicate for finding all unfinished tasks from the specified
	 *         context
	 */
	private SqlPredicate getPredicate(String contextId) {
		String sql = String.format(QUERY_FORMAT, contextId, TaskState.FINISHED, TaskState.ABORTED);

		return new SqlPredicate(sql);
	}

	private boolean isFailed(Collection<TaskEntry> tasks) {
		for (TaskEntry taskEntry : tasks) {
			if (taskEntry.getState() == TaskState.ABORTED) {
				return true;
			}
		}

		return false;
	}

	private boolean isContextDone(TaskContextEntry entry) {
		return entry.getContextState() == TaskContextState.FINISHED || entry.getContextState() == TaskContextState.FAILED;
	}
}
