package cz.cuni.mff.d3s.been.cluster.action;

import cz.cuni.mff.d3s.been.cluster.context.Benchmarks;
import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.core.task.TaskContextEntry;
import cz.cuni.mff.d3s.been.core.task.TaskContextStateInfo;
import cz.cuni.mff.d3s.been.socketworks.twoway.Replies;
import cz.cuni.mff.d3s.been.socketworks.twoway.Reply;
import cz.cuni.mff.d3s.been.socketworks.twoway.Request;
import cz.cuni.mff.d3s.been.task.checkpoints.CheckpointRequest;
import cz.cuni.mff.d3s.been.util.JSONUtils;
import cz.cuni.mff.d3s.been.util.JsonException;

/**
 * An {@link Action} that handles a request for retrieving a list of contexts
 * contained within a benchmark.
 * 
 * @author Kuba Brecka
 */
public class ContainedContextsRetrieve implements Action {

	/** the request to handle */
	private final Request request;
	private Benchmarks benchmarks;

	/**
	 * Default constructor, creates the action with the specified request and
	 * cluster context.
	 * 
	 * @param request
	 *          the request to handle
	 */
	public ContainedContextsRetrieve(CheckpointRequest request, Benchmarks benchmarks) {
		this.request = request;
		this.benchmarks = benchmarks;
	}

	@Override
	public Reply handle() {
		String benchmarkId = this.request.getSelector();
		TaskContextStateInfo info = new TaskContextStateInfo();
		for (TaskContextEntry taskContextEntry : benchmarks.getTaskContextsInBenchmark(benchmarkId)) {
			TaskContextStateInfo.Item i = new TaskContextStateInfo.Item();
			i.state = taskContextEntry.getContextState();
			i.taskContextId = taskContextEntry.getId();
			info.items.add(i);
		}

		String s;
		try {
			s = JSONUtils.newInstance().serialize(info);
		} catch (JsonException e) {
			throw new IllegalArgumentException("Cannot serialize TaskContextStateInfo to JSON.", e);
		}
		return Replies.createOkReply(s);
	}

}
