package cz.cuni.mff.d3s.been.cluster.action;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.hazelcast.core.MapEvent;
import cz.cuni.mff.d3s.been.cluster.context.TaskContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.IMap;

import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.core.task.TaskContextEntry;
import cz.cuni.mff.d3s.been.core.task.TaskContextState;
import cz.cuni.mff.d3s.been.socketworks.twoway.Replies;
import cz.cuni.mff.d3s.been.socketworks.twoway.Reply;
import cz.cuni.mff.d3s.been.socketworks.twoway.Request;

/**
 * An {@link Action} that handles a request for waiting until a specified task
 * context is finished.
 * 
 * @author Kuba Brecka
 */
public class ContextWaitAction implements Action {

	/** slf4j logger */
	private static final Logger log = LoggerFactory.getLogger(ContextWaitAction.class);

	/** the request to handle */
	private final Request request;

	/** a blocking queue which is used for the waiting operation */
	BlockingQueue<TaskContextEntry> queue = new LinkedBlockingQueue<>();

	private final TaskContexts taskContexts;

	/**
	 * Default constructor, creates the action with the specified request and
	 * cluster context.
	 * 
	 * @param request
	 *          the request to handle
	 * @param taskContexts
	 *          the cluster context
	 */
	public ContextWaitAction(Request request, TaskContexts taskContexts) {
		this.request = request;
		this.taskContexts = taskContexts;
	}

	/**
	 * A helper class which implements a listener for a specified Hazelcast map
	 * entry and adds it into the blocking queue when the event occurs.
	 */
	class MapWaiter implements EntryListener<String, TaskContextEntry> {

		@Override
		public void entryAdded(EntryEvent<String, TaskContextEntry> event) {}

		@Override
		public void entryRemoved(EntryEvent<String, TaskContextEntry> event) {
			queue.add(event.getValue());
		}

		@Override
		public void entryUpdated(EntryEvent<String, TaskContextEntry> event) {
			if (isContextDone(event.getValue())) {

				queue.add(event.getValue());
			}
		}

		@Override
		public void entryEvicted(EntryEvent<String, TaskContextEntry> event) {
			queue.add(event.getValue());
		}

		@Override
		public void mapCleared(MapEvent event) {

		}

		@Override
		public void mapEvicted(MapEvent event) {

		}
	}

	@Override
	public Reply handle() {
		String key = request.getValue();


		final MapWaiter waiter = new MapWaiter();

		IMap<String, TaskContextEntry> iMap = taskContexts.getTaskContextsMap();

		String listenerId = iMap.addEntryListener(waiter, key, true);

		TaskContextEntry value = iMap.get(key);

		boolean timeout = false;

		// TODO states...
		if (value == null || !isContextDone(value)) {
			try {
				if (request.getTimeout() <= 0) {
					value = queue.take();
				} else {
					value = queue.poll(request.getTimeout(), TimeUnit.MILLISECONDS);
					if (value == null) {
						timeout = true;
					}
				}
			} catch (InterruptedException e) {
				log.warn("Poll interrupted", e);
			}
		}

		Reply reply;
		if (value == null) {
			if (timeout) {
				reply = Replies.createErrorReply("TIMEOUT");

			} else {
				reply = Replies.createErrorReply("Unknown error");
			}
		} else {
			reply = Replies.createOkReply(value.getContextState().toString());
		}

		iMap.removeEntryListener(listenerId);
		queue.clear();

		return reply;
	}

	/**
	 * Checks whether the task context is already finished (or failed) or not.
	 * 
	 * @param entry
	 *          the task context entry to check
	 * @return true if the task context is finished or failed, false otherwise
	 */
	// TODO move to an utility class
	private boolean isContextDone(TaskContextEntry entry) {
		return entry.getContextState() == TaskContextState.FINISHED || entry.getContextState() == TaskContextState.FAILED;
	}

}
