package cz.cuni.mff.d3s.been.objectrepository;

import static cz.cuni.mff.d3s.been.objectrepository.ObjectRepositoryServiceInfoConstants.SERVICE_NAME;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.cuni.mff.d3s.been.cluster.*;
import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.util.PropertyReader;
import cz.cuni.mff.d3s.been.core.service.ServiceInfo;
import cz.cuni.mff.d3s.been.core.service.ServiceState;
import cz.cuni.mff.d3s.been.objectrepository.janitor.Janitor;
import cz.cuni.mff.d3s.been.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Generic persistence layer for BEEN entities
 */
public final class ObjectRepository implements IClusterService {

    private static final Logger log = LoggerFactory.getLogger(ObjectRepository.class);

    private final ClusterContext ctx;
    private final Storage storage;
    private final String beenId;
    private PersistentQueueDrain entityDrain;
    private QueryQueueDrain queryDrain;
    private Janitor janitor;

    @Autowired
    private NodeType nodeType;
    private ServiceInfo info;

    private final PropertyReader propertyReader;

    private ObjectRepository(ClusterContext ctx, Storage storage, String beenId) {
        this.ctx = ctx;
        this.storage = storage;

        this.beenId = beenId;

        this.propertyReader = PropertyReader.on(ctx.getProperties());
    }

    /**
     * Build an object repository over a ready (but not running) persistence layer
     *
     * @param ctx     Cluster context to work in (provides shared queues to work with)
     * @param storage Persistence layer to use
     * @param beenId  ID unique to this JVM
     * @return The object repository
     */
    public static ObjectRepository create(ClusterContext ctx, Storage storage, String beenId) {
        return new ObjectRepository(ctx, storage, beenId);
    }

    @Override
    public void start() throws ServiceException {
        log.info("Starting ObjectRepository...");
        if (storage == null) {
            throw new ServiceException("Cannot start an object repository over a null Storage");
        }

        // create sub-services
        entityDrain = PersistentQueueDrain.create(ctx, Names.PERSISTENCE_QUEUE_NAME, storage);
        queryDrain = QueryQueueDrain.create(ctx, storage);
        janitor = Janitor.create(ctx, storage);

        // start sub-services
        storage.start();
        entityDrain.start();
        queryDrain.start();
        janitor.start();

        info = new ServiceInfo(SERVICE_NAME, beenId);
        info.setServiceInfo("ObjectRepository running");
        info.setServiceState(ServiceState.OK);
        info.setHazelcastUuid(nodeType != NodeType.NATIVE ? ctx.getCluster().getLocalMember().getUuid() : null);

        int period = 30;
        int timeout = 45;

        Runnable serviceInfoUpdater = new ServiceInfoUpdater(ctx, info, timeout) {
            @Override
            public void run() {
                if (storage.isConnected()) {
                    info.setServiceInfo("Storage is connected");
                    info.setServiceState(ServiceState.OK);
                } else {
                    info.setServiceInfo("Storage is disconnected");
                    info.setServiceState(ServiceState.WARN);
                }
                super.run();
            }
        };

        ctx.schedule(serviceInfoUpdater, 0, period, TimeUnit.SECONDS);

        log.info("ObjectRepository started.");
    }

    @Override
    public void stop() {
        log.info("Stopping ObjectRepository...");
        janitor.interrupt();
        try {
            janitor.join();
        } catch (InterruptedException e) {
            log.warn("Interrupted when waiting for janitor termination. Exiting dirty");
        }
        entityDrain.stop();
        queryDrain.stop();
        storage.stop();

        try {
            ctx.removeServiceInfo(info);
        } catch (IllegalStateException e) {
            // unregistering over a Hazelcast instance that is no longer active
            log.warn("Failed to unhook ObjectRepository from the cluster. ObjectRepository info is likely to linger.", e);
        }

        log.info("ObjectRepository stopped.");
    }
}
