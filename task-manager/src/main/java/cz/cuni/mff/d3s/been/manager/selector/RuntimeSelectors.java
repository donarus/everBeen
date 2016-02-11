package cz.cuni.mff.d3s.been.manager.selector;

import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.cluster.context.Runtimes;
import cz.cuni.mff.d3s.been.core.task.TaskDescriptor;
import cz.cuni.mff.d3s.been.core.task.TaskEntry;

/**
 * Factory for different Host Runtime selection methods.
 * 
 * @author Martin Sixta
 */
public final class RuntimeSelectors {
	/**
	 * Creates appropriate Host Runtime selection method for a task
	 * 
	 * @param entry
	 *          entry of the task to find Host Runtime for
	 * 
	 * @return appropriate implementation of Host Runtime selection
	 */
	public static IRuntimeSelection fromEntry(final TaskEntry entry, final Runtimes runtimes) {
		final TaskDescriptor td = entry.getTaskDescriptor();

		boolean useXPath = td.isSetHostRuntimes() && td.getHostRuntimes().isSetXpath();

		if (useXPath) {
			return new XPathRuntimeSelection(runtimes, entry);
		} else {
			return new RandomRuntimeSelection(runtimes, entry);
		}
	}

}
