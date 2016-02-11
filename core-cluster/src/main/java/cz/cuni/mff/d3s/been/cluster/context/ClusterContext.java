package cz.cuni.mff.d3s.been.cluster.context;

import com.hazelcast.config.Config;
import com.hazelcast.core.*;
import cz.cuni.mff.d3s.been.cluster.NodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for often used Hazelcast functions.
 *
 * @author Martin Sixta
 */
@Component
public class ClusterContext {

    /**
     * the current Hazelcast node instance
     */
    @Autowired
    private HazelcastInstance hazelcastInstance;

    /**
     * scheduler for running periodic jobs
     */
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    private NodeType nodeType;

    /**
     * Returns a Hazelcast latch with the specified name. If such a latch does not
     * exist, it will be created.
     *
     * @param name name of the latch
     * @return the latch with the specified name
     */
    public ICountDownLatch getCountDownLatch(String name) {
        return hazelcastInstance.getCountDownLatch(name);
    }


    /**
     * Returns the InetSocketAddress of this node.
     * <p/>
     * In case of DATA/LITE member it's the address the node is connected to the
     * cluster.
     * <p/>
     * In case of a NATIVE node hostname with 0 port is returned.
     *
     * @return InetSocketAddress Of the cluster member or hostname of a native
     * client
     */
    public InetSocketAddress getInetSocketAddress() {
        return getCluster().getLocalMember().getInetSocketAddress();
    }

    /**
     * Returns the Hazelcast {@link Cluster} object that represents the currently
     * connected cluster.
     *
     * @return the cluster object
     */
    public Cluster getCluster() {
        return hazelcastInstance.getCluster();
    }

    /**
     * Returns the Hazelcast {@link ClientService} object that offers services for
     * handling client connections and disconnections.
     *
     * @return the client service object
     */
    public ClientService getClientService() {
        return hazelcastInstance.getClientService();
    }

    /**
     * Returns a Hazelcast queue with the specified name. If such a queue does not
     * exist, it will be created.
     *
     * @param name name of the queue
     * @param <E>  type of the queue items
     * @return the queue with the specified name
     */
    public <E> IQueue<E> getQueue(String name) {
        return hazelcastInstance.getQueue(name);
    }

    /**
     * Returns a Hazelcast map with the specified name. If such a map does not
     * exist, it will be created.
     *
     * @param name name of the map
     * @param <K>  type of the map keys
     * @param <V>  type of the map values
     * @return the map with the specified name
     */
    public <K, V> IMap<K, V> getMap(String name) {
        return hazelcastInstance.getMap(name);
    }

    /**
     * Returns all queue, map, set, list, topic, lock, multimap instances created
     * by Hazelcast.
     *
     * @return the collection of instances created by Hazelcast.
     */
    public Collection<DistributedObject> getInstances() {
        return hazelcastInstance.getDistributedObjects();
    }

    public Collection<ICountDownLatch> getCountDownLatchInstances() {
        Collection<ICountDownLatch> instances = new ArrayList<>();
        for (DistributedObject instance : getInstances()) {
            if (instance instanceof ICountDownLatch) {
                instances.add((ICountDownLatch) instance);
            }
        }

        return instances;
    }

    public boolean containsMap(String name) {
        for (DistributedObject instance : getInstances()) {
            if (instance.getName() == name && instance instanceof IMap) {
                return true;
            }
        }

        return false;
    }

    public boolean containsCountDownLatch(String name) {
        for (DistributedObject instance : getInstances()) {
            if (instance.getName() == name && instance instanceof ICountDownLatch) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the configuration of this Hazelcast instance.
     *
     * @return configuration of this Hazelcast instance
     */
    public Config getConfig() {
        return hazelcastInstance.getConfig();
    }

    /**
     * Schedules a runnable job to be run at a periodic interval.
     *
     * @param runnable     the runnable job to be scheduled
     * @param initialDelay initial delay before the job will be run for the first time
     * @param period       the period after which the job will be run again
     * @param timeUnit     the time unit in which the period and delay are represented
     */
    public void schedule(Runnable runnable, int initialDelay, int period, TimeUnit timeUnit) {
        scheduler.scheduleAtFixedRate(runnable, initialDelay, period, timeUnit);
    }

    /**
     * Stops the scheduler and all scheduled tasks.
     */
    public void stop() {
        if (!scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
