package cz.cuni.mff.d3s.been.databus.hazelcast

import com.hazelcast.config.Config
import com.hazelcast.config.ListConfig
import com.hazelcast.config.MapConfig
import com.hazelcast.config.MultiMapConfig
import com.hazelcast.config.QueueConfig
import com.hazelcast.config.ReplicatedMapConfig
import com.hazelcast.config.SemaphoreConfig
import com.hazelcast.config.SetConfig
import com.hazelcast.config.TopicConfig
import com.hazelcast.core.*
import cz.cuni.mff.d3s.been.pluger.IPluginActivator
import cz.cuni.mff.d3s.been.pluger.IServiceRegistrator
import cz.cuni.mff.d3s.been.pluger.InjectService
import cz.cuni.mff.d3s.been.pluger.PlugerServiceConstants

/**
 * Created by donarus on 8.3.15.
 */
class DataBusHazelcast implements IDataBus, IPluginActivator {

    private static final String HC_LOGGING_TYPE_KEY = "hazelcast.logging.type"

    private static final String HC_LOGGING_TYPE = "slf4j"

    @InjectService(serviceName = PlugerServiceConstants.PLUGIN_CLASSLOADER)
    private ClassLoader pluginsClassLoader

    private HazelcastInstance hazelcastInstance

    void connect() {
        hazelcastInstance = Hazelcast.newHazelcastInstance(createHazelcastConfig())
    }

    private Config createHazelcastConfig() {
        def config = new Config()
        config.setClassLoader(pluginsClassLoader)
        config.setProperty(HC_LOGGING_TYPE_KEY, HC_LOGGING_TYPE)
        return config
    }

    @Override
    String generateClusterWideUUID(String name) {
        // we need to add some prefix because we don't want to collide with id generator
        long generated = hazelcastInstance.getIdGenerator("uuid_${name}").newId()
        generated.toString().padLeft(16, "0")
    }

    @Override
    def <E> IQueue<E> getQueue(String name) {
        hazelcastInstance.getQueue(name)
    }

    @Override
    def <E> ITopic<E> getTopic(String name) {
        hazelcastInstance.getTopic(name)
    }

    @Override
    def <E> ISet<E> getSet(String name) {
        hazelcastInstance.getSet(name)
    }

    @Override
    def <E> IList<E> getList(String name) {
        hazelcastInstance.getList(name)
    }

    @Override
    def <K, V> IMap<K, V> getMap(String name) {
        hazelcastInstance.getMap(name)
    }

    @Override
    def <K, V> ReplicatedMap<K, V> getReplicatedMap(String name) {
        hazelcastInstance.getReplicatedMap(name)
    }

    @Override
    def <K, V> MultiMap<K, V> getMultiMap(String name) {
        hazelcastInstance.getMultiMap(name)
    }

    @Override
    ILock getLock(String key) {
        hazelcastInstance.getLock(key)
    }

    @Override
    IdGenerator getIdGenerator(String name) {
        // we need to add some prefix because we don't want to collide with uuid generator
        hazelcastInstance.getIdGenerator("genid_${name}")
    }

    @Override
    IAtomicLong getAtomicLong(String name) {
        hazelcastInstance.getAtomicLong(name)
    }

    @Override
    def <E> IAtomicReference<E> getAtomicReference(String name) {
        hazelcastInstance.getAtomicReference(name)
    }

    @Override
    ICountDownLatch getCountDownLatch(String name) {
        hazelcastInstance.getCountDownLatch(name)
    }

    @Override
    ISemaphore getSemaphore(String name) {
        hazelcastInstance.getSemaphore(name)
    }

    @Override
    MapConfig getMapConfig(String name) {
        hazelcastInstance.getConfig().getMapConfig()
    }

    @Override
    IDataBus addMapConfig(MapConfig mapConfig) {
        hazelcastInstance.getConfig().addMapConfig(mapConfig)
        this
    }

    @Override
    QueueConfig getQueueConfig(String name) {
        hazelcastInstance.getConfig().getQueueConfig()
    }

    @Override
    IDataBus addQueueConfig(QueueConfig queueConfig) {
        hazelcastInstance.getConfig().addQueueConfig(queueConfig)
        this
    }

    @Override
    ListConfig getListConfig(String name) {
        hazelcastInstance.getConfig().getListConfig()
    }

    @Override
    IDataBus addListConfig(ListConfig listConfig) {
        hazelcastInstance.getConfig().addListConfig(listConfig)
        this
    }

    @Override
    SetConfig getSetConfig(String name) {
        hazelcastInstance.getConfig().getSetConfig()
    }

    @Override
    IDataBus addSetConfig(SetConfig setConfig) {
        hazelcastInstance.getConfig().addSetConfig(setConfig)
        this
    }

    @Override
    MultiMapConfig getMultiMapConfig(String name) {
        hazelcastInstance.getConfig().getMultiMapConfig()
    }

    @Override
    IDataBus addMultiMapConfig(MultiMapConfig multiMapConfig) {
        hazelcastInstance.getConfig().addMultiMapConfig(multiMapConfig)
        this
    }

    @Override
    ReplicatedMapConfig getReplicatedMapConfig(String name) {
        hazelcastInstance.getConfig().getReplicatedMapConfig()
    }

    @Override
    IDataBus addReplicatedMapConfig(ReplicatedMapConfig replicatedMapConfig) {
        hazelcastInstance.getConfig().addReplicatedMapConfig(replicatedMapConfig)
        this
    }

    @Override
    TopicConfig getTopicConfig(String name) {
        hazelcastInstance.getConfig().getTopicConfig()
    }

    @Override
    IDataBus addTopicConfig(TopicConfig topicConfig) {
        hazelcastInstance.getConfig().addTopicConfig(topicConfig)
        this
    }

    @Override
    SemaphoreConfig getSemaphoreConfig(String name) {
        hazelcastInstance.getConfig().getSemaphoreConfig()
    }

    @Override
    IDataBus addSemaphoreConfig(SemaphoreConfig semaphoreConfig) {
        hazelcastInstance.getConfig().addSemaphoreConfig(semaphoreConfig)
        this
    }

    //
    // PLUGIN ACTIVATOR METHODS
    //

    @Override
    void activate(IServiceRegistrator registry) {
        registry.registerService(IDataBus, this)
        // this is one of the core plugins of Been platform and from this reason this plugin is connected
        // as soon as possible
        connect()
    }

    @Override
    void initialize() {

    }

    @Override
    void start() {

    }

    @Override
    void notifyStarted() {

    }
}
