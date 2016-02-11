package cz.cuni.mff.d3s.been.manager;

import static cz.cuni.mff.d3s.been.core.task.TaskState.WAITING;

import java.util.Collection;
import java.util.Properties;

import com.hazelcast.core.MapEvent;
import cz.cuni.mff.d3s.been.cluster.context.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.config.MapConfig;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.IMap;
import com.hazelcast.query.SqlPredicate;

import cz.cuni.mff.d3s.been.cluster.ServiceException;
import cz.cuni.mff.d3s.been.core.task.TaskEntries;
import cz.cuni.mff.d3s.been.core.task.TaskEntry;
import cz.cuni.mff.d3s.been.core.task.TaskState;
import cz.cuni.mff.d3s.been.manager.msg.Messages;
import cz.cuni.mff.d3s.been.manager.msg.TaskMessage;
import cz.cuni.mff.d3s.been.mq.IMessageSender;
import cz.cuni.mff.d3s.been.mq.MessagingException;

/**
 * Listens for local key events of the Task Map.
 * 
 * 
 * @author Martin Sixta
 */
final class LocalTaskListener extends TaskManagerService implements EntryListener<String, TaskEntry> {

	/** logging */
	private static final Logger log = LoggerFactory.getLogger(LocalTaskListener.class);

	/** Format of "tasks waiting for a task to finish" query */
	private static final String WAITING_TASKS_FMT = "taskContextId = '%s' AND taskDependency = '%s'";

	/** task map */
	private IMap<String, TaskEntry> taskMap;

	private TaskContexts taskContexts;
	private final Benchmarks benchmarks;
	private final Tasks tasks;
	private Runtimes runtimes;
	private Topics topics;
	private Properties properties;

	/** sender of "in-task manager" messages */
	private IMessageSender<TaskMessage> sender;

	private String listenerId;

	public LocalTaskListener(TaskContexts taskContexts, Benchmarks benchmarks, Tasks tasks, Runtimes runtimes,
							 Topics topics, Properties properties) {
		this.taskContexts = taskContexts;
		this.benchmarks = benchmarks;
		this.tasks = tasks;
		this.runtimes = runtimes;
		this.topics = topics;
		this.properties = properties;
		taskMap = tasks.getTasksMap();
		MapConfig cfg = tasks.getTasksMapConfig();

		if (cfg == null) {
			throw new RuntimeException("BEEN_MAP_TASKS! does not have a config!");
		}
		if (cfg.isNearCacheEnabled()) {
			log.warn("FIXME: throw new RuntimeException(\"Near cache is enabled for BEEN_MAP_TASKS!\");");
		}

	}

	@Override
	public void start() throws ServiceException {
		sender = createSender();

		this.listenerId = taskMap.addLocalEntryListener(this);
	}

	@Override
	public void stop() {
		taskMap.removeEntryListener(this.listenerId);
		sender.close();
	}

	@Override
	public synchronized void entryAdded(EntryEvent<String, TaskEntry> event) {
		log.debug("TaskEntry {} added", event.getKey());

		TaskEntry entry = event.getValue();

		if (entry.isSetTaskDependency()) {
			String dep = entry.getTaskDependency();

			TaskEntries.setState(entry, WAITING, "Waiting for task %s to finish", dep);
			tasks.putTask(entry);
			return;
		}
		try {
			TaskMessage msg = Messages.createNewTaskMessage(entry, tasks, runtimes, topics);
			sender.send(msg);
		} catch (MessagingException e) {
			String msg = String.format("Cannot send message to '%s'", sender.getConnection());
			log.error(msg, e);
		}
	}

	@Override
	public synchronized void entryRemoved(EntryEvent<String, TaskEntry> event) {
		log.debug("TaskEntry {} removed ", event.getKey());
	}

	@Override
	public synchronized void entryUpdated(EntryEvent<String, TaskEntry> event) {
		log.debug("TaskEntry {} updated", event.getKey());

		TaskEntry entry = event.getValue();
		TaskState state = entry.getState();

		// skip waiting tasks
		if (state == TaskState.WAITING) {

			try {
				TaskMessage msg = Messages.createCheckSchedulabilityMessage(entry, tasks, runtimes, topics);

				sender.send(msg);
			} catch (MessagingException e) {
				String msg = String.format("Cannot send message to '%s'", sender.getConnection());
				log.error(msg, e);
			}

			return;
		}

		// schedule dependent tasks if any
		if (state == TaskState.FINISHED || state == TaskState.ABORTED) {
			scheduleWaitingTasks(entry);
		}

		try {
			TaskMessage msg = Messages.createTaskChangedMessage(entry, taskContexts, benchmarks, tasks, runtimes, topics, properties);
			sender.send(msg);
		} catch (MessagingException e) {
			String msg = String.format("Cannot send message to '%s'", sender.getConnection());
			log.error(msg, e);
		}
	}

	@Override
	public synchronized void entryEvicted(EntryEvent<String, TaskEntry> event) {
		log.debug("TaskEntry {} evicted", event.getKey());
	}

	/**
	 * 
	 * Schedules tasks dependent on the finished task
	 * 
	 * @param entry
	 *          the task that has finished
	 */
	private void scheduleWaitingTasks(TaskEntry entry) {

		String query = String.format(WAITING_TASKS_FMT, entry.getTaskContextId(), entry.getId());

		SqlPredicate predicate = new SqlPredicate(query);
		try {
			Collection<TaskEntry> entries = taskMap.values(predicate);
			for (TaskEntry e : entries) {
				sender.send(Messages.createScheduleTaskMessage(e, tasks, runtimes, topics));
			}
		} catch (Exception e) {
			log.error("Cannot schedule a waiting task", e);
		}

	}

	@Override
	public void mapCleared(MapEvent event) {

	}

	@Override
	public void mapEvicted(MapEvent event) {

	}
}
