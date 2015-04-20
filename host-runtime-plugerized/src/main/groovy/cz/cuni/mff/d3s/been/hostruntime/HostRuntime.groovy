package cz.cuni.mff.d3s.been.hostruntime

import com.hazelcast.config.MapConfig
import cz.cuni.mff.d3s.been.databus.hazelcast.IDataBus
import cz.cuni.mff.d3s.been.detectors.Detector
import cz.cuni.mff.d3s.been.pluger.IPluginActivator
import cz.cuni.mff.d3s.been.pluger.IServiceRegistrator
import cz.cuni.mff.d3s.been.pluger.InjectService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by donarus on 8.3.15.
 */
class HostRuntime implements IHostRuntime, IPluginActivator {

    private static final String HOST_RUNTIME_INFO_MAP_NAME = HostRuntimeInfo.getName()

    private static final String HOST_RUNTIME_UUID_NAME = HostRuntime.getName()

    private static final Logger LOG = LoggerFactory.getLogger(HostRuntime)

    @InjectService
    private IDataBus dataBus

    @InjectService
    private Detector detector;

    private String uuid

    private Map<String, HostRuntimeInfo> runtimeInfoMap

    private HostRuntimeInfo runtimeInfo

    //
    // PLUGIN ACTIVATOR METHODS
    //

    @Override
    void activate(IServiceRegistrator registry) {
        registry.registerService(IHostRuntime, this)
    }

    @Override
    void initialize() {
        uuid = dataBus.generateClusterWideUUID(HOST_RUNTIME_UUID_NAME)
        LOG.info("generated uuid: $uuid")
        def mapConfig = new MapConfig(
                name: HOST_RUNTIME_INFO_MAP_NAME,
                timeToLiveSeconds: 60,
                backupCount: 1,
                asyncBackupCount: 0
        )
        dataBus.addMapConfig(mapConfig)
        runtimeInfoMap = dataBus.getMap(HOST_RUNTIME_INFO_MAP_NAME)
    }

    @Override
    public void start() {
        runtimeInfo = new HostRuntimeInfo(
                uuid: uuid,
                machineInfo: detector.detectMachineInfo()
        )
        runtimeInfoMap.put(uuid, runtimeInfo)
    }

    @Override
    void notifyStarted() {

    }
}
