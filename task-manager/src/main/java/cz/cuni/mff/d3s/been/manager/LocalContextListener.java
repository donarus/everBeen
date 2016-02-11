package cz.cuni.mff.d3s.been.manager;

import com.hazelcast.core.MapEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.IMap;

import cz.cuni.mff.d3s.been.cluster.ServiceException;
import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.cluster.context.TaskContexts;
import cz.cuni.mff.d3s.been.core.task.TaskContextEntry;
import cz.cuni.mff.d3s.been.manager.msg.Messages;
import cz.cuni.mff.d3s.been.manager.msg.TaskMessage;
import cz.cuni.mff.d3s.been.mq.IMessageSender;
import cz.cuni.mff.d3s.been.mq.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 
 * Listener for local context events.
 * 
 * @author Martin Sixta
 */
@Component
final class LocalContextListener extends TaskManagerService implements EntryListener<String, TaskContextEntry> {

	/** logging */
	private static final Logger log = LoggerFactory.getLogger(LocalContextListener.class);

	@Autowired
	private TaskContexts taskContexts;
	private IMap<String, TaskContextEntry> contextsMap;
	@Autowired
	private PersistentContextStateRegistrar persistentStateRegistrar;

	private IMessageSender<TaskMessage> sender;
	private String listenerId;

	@PostConstruct
	private void initialize() {
		this.contextsMap = taskContexts.getTaskContextsMap();
	}

	@Override
	public void start() throws ServiceException {
		sender = createSender();
		this.listenerId = contextsMap.addLocalEntryListener(this);
	}

	@Override
	public void stop() {
		contextsMap.removeEntryListener(this.listenerId);
		sender.close();
	}

	@Override
	public synchronized void entryAdded(EntryEvent<String, TaskContextEntry> event) {
		final TaskContextEntry entry = event.getValue();

		try {
			TaskMessage msg = Messages.createRunContextMessage(entry.getId(), taskContexts);
			sender.send(msg);
		} catch (MessagingException e) {
			String msg = String.format("Cannot send message to '%s'", sender.getConnection());
			log.error(msg, e);
		}
	}

	@Override
	public void entryRemoved(EntryEvent<String, TaskContextEntry> event) {}

	@Override
	public synchronized void entryUpdated(EntryEvent<String, TaskContextEntry> event) {
		final TaskContextEntry entry = event.getValue();
		persistentStateRegistrar.processContextStateChange(entry.getId(), entry.getBenchmarkId(), entry.getContextState());
	}

	@Override
	public void entryEvicted(EntryEvent<String, TaskContextEntry> event) {}

	@Override
	public void mapCleared(MapEvent event) {

	}

	@Override
	public void mapEvicted(MapEvent event) {

	}
}
