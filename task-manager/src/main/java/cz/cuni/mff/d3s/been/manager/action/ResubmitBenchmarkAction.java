package cz.cuni.mff.d3s.been.manager.action;

import static cz.cuni.mff.d3s.been.manager.TaskManagerConfiguration.DEFAULT_MAXIMUM_ALLOWED_RESUBMITS;
import static cz.cuni.mff.d3s.been.manager.TaskManagerConfiguration.MAXIMUM_ALLOWED_RESUBMITS;

import java.util.List;
import java.util.Properties;

import com.hazelcast.core.IMap;

import cz.cuni.mff.d3s.been.cluster.context.Benchmarks;
import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.cluster.context.Tasks;
import cz.cuni.mff.d3s.been.core.benchmark.BenchmarkEntry;
import cz.cuni.mff.d3s.been.core.benchmark.ResubmitHistoryItem;
import cz.cuni.mff.d3s.been.core.task.TaskEntry;
import cz.cuni.mff.d3s.been.core.task.TaskState;
import cz.cuni.mff.d3s.been.util.PropertyReader;

/**
 * @author Kuba Brecka
 */
final class ResubmitBenchmarkAction implements TaskAction {
	private final TaskEntry entry;
	private final Benchmarks benchmarks;
	private final Tasks tasks;
	private Properties properties;

	/**
	 * Creates ResubmitBenchmarkAction.
	 *
	 * @param entry
	 *          targeted task entry
	 */
	public ResubmitBenchmarkAction(TaskEntry entry, Benchmarks benchmarks, Tasks tasks, Properties properties) {
		this.entry = entry;
		this.benchmarks = benchmarks;
		this.tasks = tasks;
		this.properties = properties;
	}

	@Override
	public void execute() throws TaskActionException {
		String benchmarkId = entry.getBenchmarkId();
		IMap<String, BenchmarkEntry> benchmarksMap = benchmarks.getBenchmarksMap();

		benchmarksMap.lock(benchmarkId);
		try {
			BenchmarkEntry benchmarkEntry = benchmarks.get(benchmarkId);
			String generatorId = benchmarkEntry.getGeneratorId();
			TaskEntry generatorTask = tasks.getTask(generatorId);

			if (!benchmarkEntry.isAllowResubmit()) {
				return;
			}

			List<ResubmitHistoryItem> resubmits = benchmarkEntry.getResubmitHistory().getResubmitHistoryItem();

			PropertyReader propertyReader = PropertyReader.on(properties);
			int maximumResubmits = propertyReader.getInteger(MAXIMUM_ALLOWED_RESUBMITS, DEFAULT_MAXIMUM_ALLOWED_RESUBMITS);
			if (resubmits.size() >= maximumResubmits) {
				return;
			}

			// fail-safe check for race conditions
			if (generatorTask.getState() != TaskState.ABORTED) {
				return;
			}

			benchmarks.resubmit(benchmarkEntry);
		} finally {
			benchmarksMap.unlock(benchmarkId);
		}

	}
}
