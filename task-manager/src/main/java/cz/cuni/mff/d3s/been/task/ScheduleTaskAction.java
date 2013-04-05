package cz.cuni.mff.d3s.been.task;

import static cz.cuni.mff.d3s.been.core.task.TaskState.SCHEDULED;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ConcurrentModificationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.IMap;
import com.hazelcast.core.Transaction;

import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.cluster.context.Tasks;
import cz.cuni.mff.d3s.been.core.protocol.Context;
import cz.cuni.mff.d3s.been.core.protocol.messages.RunTaskMessage;
import cz.cuni.mff.d3s.been.core.task.TaskDescriptor;
import cz.cuni.mff.d3s.been.core.task.TaskEntries;
import cz.cuni.mff.d3s.been.core.task.TaskEntry;
import cz.cuni.mff.d3s.been.core.task.TaskState;

/**
 * @author Martin Sixta
 */
final class ScheduleTaskAction implements TaskAction {

	private static Logger log = LoggerFactory.getLogger(ScheduleTaskAction.class);

	private final ClusterContext ctx;
	private TaskEntry entry;
	private static final String RUNTIME_TOPIC = Context.GLOBAL_TOPIC.getName();

	public ScheduleTaskAction(ClusterContext ctx, TaskEntry entry) {
		this.ctx = ctx;
		this.entry = entry;
	}

	@Override
	public void execute() {

		String taskId = entry.getId();
		final Tasks tasks = ctx.getTasksUtils();
		final IMap<String, TaskEntry> map = tasks.getTasksMap();

		final String nodeId = ctx.getId();

		log.info("Received new task " + taskId);

		// TODO explain how/why concurrent modification detection works
		// TODO write down that assertClusterEqual does not work with ttl ...

		Transaction txn = null;

		try {

			// 1) Find suitable Host Runtime
			String receiverId = findHostRuntime(entry);

			String id = entry.getId();

			TaskEntry entryCopy = map.get(id);

			assertEqual(entry, entryCopy);

			// 2) Change task state to SCHEDULED and send message to the Host Runtime
			txn = ctx.getTransaction();

			{
				txn.begin(); // BEGIN TRANSACTION -----------------------------

				// Claim ownership of the node
				entry.setOwnerId(nodeId);

				// Update content of the entry
				TaskEntries.setState(entry, SCHEDULED, "Task scheduled on %s", receiverId);

				entry.setRuntimeId(receiverId);

				// Update entry
				TaskEntry oldValue = tasks.putTask(entry, 30, SECONDS);

				// Make sure there was no concurrent modification
				assertEqual(entryCopy, oldValue);

				txn.commit(); // END TRANSACTION ------------------------------
			}

			// Send a message to the runtime
			ctx.getTopicUtils().publish(RUNTIME_TOPIC, newRunTaskMessage(entry));

			log.info("Task " + taskId + " scheduled on " + receiverId);

		} catch (NoRuntimeFoundException e) {
			// TODO: Abort the task ...
			String msg = String.format("No runtime found for task %s", entry.getId());
			log.warn(msg, e);

			abortTask(msg);

		} catch (Throwable e) {
			String msg = String.format("Rollback while scheduling task %s", taskId);
			log.error(msg, e);

			if (txn != null) {
				try {
					txn.rollback();
				} catch (Throwable t) {
					//quell
				};
			}

			abortTask(msg);
		}

	}

	private void abortTask(String msg) {
		log.warn("Aborting task {}", entry.getId());
		ctx.getTasksUtils().updateTaskState(entry, TaskState.ABORTED, msg);
	}

	private String findHostRuntime(final TaskEntry entry) throws NoRuntimeFoundException {

		IRuntimeSelection selection = createSelection(entry.getTaskDescriptor());

		return selection.select(entry);

	}

	private IRuntimeSelection createSelection(final TaskDescriptor td) {
		if (td.isSetHostRuntimes() && td.getHostRuntimes().isSetXpath()) {
			return new XPathRuntimeSelection(ctx);
		} else {
			return new RandomRuntimeSelection(ctx);
		}
	}

	private RunTaskMessage newRunTaskMessage(TaskEntry taskEntry) {
		String senderId = taskEntry.getOwnerId();
		String receiverId = taskEntry.getRuntimeId();
		String taskId = taskEntry.getId();
		return new RunTaskMessage(senderId, receiverId, taskId);
	}

	private void assertEqual(TaskEntry entry1, TaskEntry entry2) {
		if (!entry1.equals(entry2)) {
			// TODO msg
			throw new ConcurrentModificationException("Task modified");
		}
	}
}
