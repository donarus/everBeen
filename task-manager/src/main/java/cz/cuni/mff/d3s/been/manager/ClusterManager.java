package cz.cuni.mff.d3s.been.manager;

import cz.cuni.mff.d3s.been.cluster.context.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.cuni.mff.d3s.been.cluster.IClusterService;
import cz.cuni.mff.d3s.been.cluster.ServiceException;
import cz.cuni.mff.d3s.been.mq.MessageQueues;
import cz.cuni.mff.d3s.been.mq.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * Manages local cluster resources.
 * 
 * @author Martin Sixta
 */
@Component
final class ClusterManager implements IClusterService {
	private static final Logger log = LoggerFactory.getLogger(ClusterManager.class);

	@Autowired
	private  ClusterContext clusterCtx;
	@Autowired
	private TaskContexts taskContexts;
	@Autowired
	private Runtimes runtimes;
	@Autowired
	private Tasks tasks;
	@Autowired
	private Benchmarks benchmarks;
	@Autowired
	private Topics topics;

	@Autowired
	private Properties properties;

	private  LocalRuntimeListener localRuntimeListener;
	private  LocalTaskListener localTaskListener;

	@Autowired
	private  LocalContextListener localContextListener;
	private  MembershipListener membershipListener;
	private  ClientListener clientListener;
	private LocalKeyScanner keyScanner;

	private final MessageQueues messageQueues = MessageQueues.getInstance();


	@PostConstruct
	private void initialize() {
		localRuntimeListener = new LocalRuntimeListener(taskContexts, runtimes, tasks, benchmarks, topics, properties);
		localTaskListener = new LocalTaskListener( taskContexts, benchmarks, tasks, runtimes, topics, properties);
		membershipListener = new MembershipListener(clusterCtx);
		clientListener = new ClientListener(clusterCtx);

	}

	private TaskMessageProcessor taskMessageProcessor;

	@Override
	public void start() throws ServiceException {
		log.info("Starting Task Manager...");
		try {
			messageQueues.createInprocQueue(TaskManagerNames.ACTION_QUEUE_NAME);
		} catch (MessagingException e) {
			throw new ServiceException("Cannot start clustered Task Manager", e);
		}

		taskMessageProcessor = new TaskMessageProcessor(clusterCtx);
		keyScanner = new LocalKeyScanner(clusterCtx, tasks, runtimes, topics, properties);

		taskMessageProcessor.start();
		localRuntimeListener.start();
		localTaskListener.start();
		localContextListener.start();
		membershipListener.start();
		clientListener.start();
		keyScanner.start();

		log.info("Task Manager started.");
	}

	@Override
	public void stop() {
		log.info("Stopping Task Manager...");
		keyScanner.stop();

		clientListener.stop();
		localRuntimeListener.stop();
		localContextListener.stop();
		localTaskListener.stop();
		membershipListener.stop();
		taskMessageProcessor.poison();

		log.info("Task Manager stopped.");

	}
}
