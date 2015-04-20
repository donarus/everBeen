package cz.cuni.mff.d3s.been.databus.hazelcast;

import com.hazelcast.config.*;
import com.hazelcast.core.*;

/**
 * Created by donarus on 8.3.15.
 */
public interface IDataBus {

    String generateClusterWideUUID(String name);

    <E> IQueue<E> getQueue(String name);

    <E> ITopic<E> getTopic(String name);

    <E> ISet<E> getSet(String name);

    <E> IList<E> getList(String name);

    <K, V> IMap<K, V> getMap(String name);

    <K, V> ReplicatedMap<K, V> getReplicatedMap(String name);

    <K, V> MultiMap<K, V> getMultiMap(String name);

    ILock getLock(String key);

    IdGenerator getIdGenerator(String name);

    IAtomicLong getAtomicLong(String name);

    <E> IAtomicReference<E> getAtomicReference(String name);

    ICountDownLatch getCountDownLatch(String name);

    ISemaphore getSemaphore(String name);


    MapConfig getMapConfig(String name);

    IDataBus addMapConfig(MapConfig mapConfig);

    QueueConfig getQueueConfig(String name);

    IDataBus addQueueConfig(QueueConfig queueConfig);

    ListConfig getListConfig(String name);

    IDataBus addListConfig(ListConfig listConfig);

    SetConfig getSetConfig(String name);

    IDataBus addSetConfig(SetConfig setConfig);

    MultiMapConfig getMultiMapConfig(String name);

    IDataBus addMultiMapConfig(MultiMapConfig multiMapConfig);

    ReplicatedMapConfig getReplicatedMapConfig(String name);

    IDataBus addReplicatedMapConfig(ReplicatedMapConfig replicatedMapConfig);

    TopicConfig getTopicConfig(String name);

    IDataBus addTopicConfig(TopicConfig topicConfig);

    SemaphoreConfig getSemaphoreConfig(String name);

    IDataBus addSemaphoreConfig(SemaphoreConfig semaphoreConfig);

}
