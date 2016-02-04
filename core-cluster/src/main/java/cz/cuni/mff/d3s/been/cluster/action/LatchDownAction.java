package cz.cuni.mff.d3s.been.cluster.action;

import com.hazelcast.core.ICountDownLatch;
import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.socketworks.twoway.Replies;
import cz.cuni.mff.d3s.been.socketworks.twoway.Reply;
import cz.cuni.mff.d3s.been.task.checkpoints.CheckpointRequest;

/**
 * An {@link Action} that handles a request for a latch-down operation.
 * 
 * @author Martin Sixta
 */
final class LatchDownAction implements Action {

	/** the request to handle */
	private final CheckpointRequest request;

	/** BEEN cluster instance */
	private final ClusterContext ctx;

	/**
	 * Default constructor, creates the action with the specified request and
	 * cluster context.
	 * 
	 * @param request
	 *          the request to handle
	 * @param ctx
	 *          the cluster context
	 */
	public LatchDownAction(CheckpointRequest request, ClusterContext ctx) {
		this.request = request;
		this.ctx = ctx;
	}

	@Override
	public Reply handle() {
		String latchName = Actions.latchNameForRequest(request);
		if (!ctx.containsCountDownLatch(latchName)) {
			return Replies.createErrorReply("No such Count Down Latch '%s'", latchName);
		}

		final ICountDownLatch countDownLatch = ctx.getCountDownLatch(latchName);
		countDownLatch.countDown();

		return Replies.createOkReply(Boolean.toString(countDownLatch.getCount() > 0));
	}

}
