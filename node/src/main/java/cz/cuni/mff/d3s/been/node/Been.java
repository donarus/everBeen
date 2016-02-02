package cz.cuni.mff.d3s.been.node;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public final class Been {

    @Autowired
    @Qualifier("properties")
    private Properties properties;

    @Autowired
    @Qualifier("hazelcastInstance")
    private HazelcastInstance hazelcastInstance;

    public void start() {


////        initIds();
////
////		HazelcastInstance instance = null;
//
//		try {
//			// Join the cluster
//			log.info("The node is connecting to the cluster");
//			instance = getInstance(nodeType, properties);
//			log.info("The node is now connected to the cluster");
//		} catch (ServiceException e) {
//			log.error("Failed to initialize cluster instance", e);
//			EX_COMPONENT_FAILED.sysExit();
//		}
//
//		Reaper clusterReaper = new ClusterReaper(instance);
//		this.clusterContext = Instance.createContext();
//
//		if (nodeType == NodeType.DATA) {
//			// must happen as soon as possible, see documentation for BEEN MapStore implementation
//			initializeMaps(
//					TASKS_MAP_NAME,
//					TASK_CONTEXTS_MAP_NAME,
//					BENCHMARKS_MAP_NAME,
//					NAMED_TASK_CONTEXT_DESCRIPTORS_MAP_NAME,
//					NAMED_TASK_DESCRIPTORS_MAP_NAME);
//		}
//
//		registerServiceCleaner();
//		try {
//			// standalone services
//			if (runSWRepository) {
//				clusterReaper.pushTarget(startSWRepository());
//			}
//
//			// Run Task Manager on DATA nodes
//			if (nodeType == NodeType.DATA) {
//				clusterReaper.pushTarget(startTaskManager());
//			}
//
//			if (runHostRuntime) {
//				clusterReaper.pushTarget(startHostRuntime(clusterContext, properties));
//			}
//
//			clusterReaper.pushTarget(startLogPersister());
//
//			// Services that require a persistence layer
//			if (runRepository) {
//				final Storage storage = StorageBuilderFactory.createBuilder(properties).build();
//				clusterReaper.pushTarget(startRepository(storage));
//			}
//
//		} catch (ServiceException se) {
//			log.error("Service bootstrap failed.", se);
//			clusterReaper.start();
//			try {
//				clusterReaper.join();
//			} catch (InterruptedException e) {
//				log.error("Failed to perform cleanup due to user interruption. Exiting dirty.");
//			}
//			EX_COMPONENT_FAILED.sysExit();
//		}
//
//		clusterReaper.pushTarget(this);
//
//		Runtime.getRuntime().addShutdownHook(clusterReaper);

    }

    public void stop() {

    }
}
