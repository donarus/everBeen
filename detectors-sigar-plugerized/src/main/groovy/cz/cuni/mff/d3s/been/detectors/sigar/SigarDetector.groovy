package cz.cuni.mff.d3s.been.detectors.sigar

import cz.cuni.mff.d3s.been.detectors.*
import cz.cuni.mff.d3s.been.pluger.IPluginActivator
import cz.cuni.mff.d3s.been.pluger.IServiceRegistrator
import cz.cuni.mff.d3s.been.pluger.InjectService
import cz.cuni.mff.d3s.been.pluger.PlugerServiceConstants
import org.hyperic.jni.ArchLoader
import org.hyperic.jni.ArchName
import org.hyperic.jni.ArchNotSupportedException
import org.hyperic.sigar.OperatingSystem
import org.hyperic.sigar.Sigar
import org.hyperic.sigar.SigarException

import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

/**
 * Sigar native detector.
 *
 * @author Kuba Brecka
 * @author Tadeas Palusga
 */
public class SigarDetector implements Detector, IPluginActivator {

    @InjectService(serviceName = PlugerServiceConstants.TMP_DIRECTORY)
    private File tmpDir

    private Sigar sigar

    private Path sigarTmpDir

    private Sigar loadSigar() throws SigarException {
        try {
            def (libraryName, libraryUrl) = selectArchitectureLibrary()
            unpack(libraryUrl, libraryName, sigarTmpDir)
            System.setProperty("org.hyperic.sigar.path", sigarTmpDir.toString())
            return new Sigar()
        } catch (ArchNotSupportedException e) {
            e.printStackTrace()
            // FIXME logging unable to load sigar
        } catch (IOException e) {
            // FIXME unable to copy sigar lib
            e.printStackTrace()
        } catch (Exception e) {
            // FIXME unknown exception while loading sigar
            e.printStackTrace()
        }
    }

    private def selectArchitectureLibrary() throws ArchNotSupportedException {
        def libraryName = new ArchLoader(libName: "sigar-${ArchName.getName()}").libraryName
        def url = getClass().getResource(libraryName)
        [libraryName, url]
    }

    private void unpack(URL what, String name, Path destination) {
        def unpackedLibrary = destination.resolve(name)
        unpackedLibrary << what.openStream()
    }

    private CpusInfo detectCpusInfo() {
        def info = new CpusInfo()
        sigar.getCpuInfoList().each { sigarCpuInfo ->
            info.addCpuInfo(new CpuInfo(
                    vendor: sigarCpuInfo.vendor,
                    model: sigarCpuInfo.model,
                    mhz: sigarCpuInfo.mhz,
                    cacheSize: sigarCpuInfo.cacheSize
            ))
        }
        return info
    }

    private MemoryInfo detectMemoryInfo() {
        new MemoryInfo(
                memorySize: sigar.getMem().total,
                swapSize: sigar.getSwap().total
        )
    }

    private NetworkInfo detectNetworkInfo() {
        def networkInfo = new NetworkInfo()
        sigar.getNetInterfaceList().each { interfaze ->
            def interfaceConfig = sigar.getNetInterfaceConfig(interfaze)
            networkInfo.addNetworkInterfaceInfo(new NetworkInterfaceInfo(
                    name: interfaceConfig.name,
                    hwaddr: interfaceConfig.hwaddr,
                    type: interfaceConfig.type,
                    mtu: interfaceConfig.mtu,
                    address: interfaceConfig.address,
                    netmask: interfaceConfig.netmask,
                    broadcast: interfaceConfig.broadcast
            ))
        }
        return networkInfo
    }

    private HardwareInfo detectHardwareInfo() {
        new HardwareInfo(
                cpusInfo: detectCpusInfo(),
                memoryInfo: detectMemoryInfo(),
                networkInfo: detectNetworkInfo()
        )
    }

    private OperatingSystemInfo detectOperatingSystemInfo() {
        def os = OperatingSystem.getInstance()
        new OperatingSystemInfo(
                name: os.name,
                version: os.version,
                architecture: os.arch,
                vendor: os.vendor,
                vendorVersion: os.vendorVersion,
                dataModel: os.dataModel,
                endian: os.cpuEndian
        )
    }

    private FileSystemsInfo detectFileSystemsInfo() {
        def fileSystemsInfo = new FileSystemsInfo()
        sigar.getFileSystemList().each { fileSystem ->
            def usage = sigar.getFileSystemUsage(fileSystem.dirName)
            fileSystemsInfo.addFileSystemInfo(new FileSystemInfo(
                    deviceName: fileSystem.devName,
                    directory: fileSystem.dirName,
                    type: fileSystem.typeName,
                    total: usage.total,
                    free: usage.free
            ))
        }
        return fileSystemsInfo
    }

    @Override
    MachineInfo detectMachineInfo() {
        new MachineInfo(
                hardwareInfo: detectHardwareInfo(),
                operatingSystemInfo: detectOperatingSystemInfo(),
                fileSystemsInfo: detectFileSystemsInfo()
        )
    }

    private LoadAverage detectLoadAverage() {
        def load = sigar.getLoadAverage()
        new LoadAverage(
                load1: load[0],
                load5: load[1],
                load15: load[2]
        )
    }

    private CpusUsage detectCpusUsage() {
        def cpusUsage = new CpusUsage(
                totalPercentage: sigar.cpuPerc.combined
        )
        sigar.cpuPercList.each { cpuPerc ->
            cpusUsage.addCpuUsage(new CpuUsage(
                    percentage: cpuPerc.combined
            ))
        }
        return cpusUsage
    }

    MemoryUsage detectMemoryUsage() {
        def mem = sigar.mem
        def swap = sigar.swap
        new MemoryUsage(
                memTotal: mem.total,
                memTotalUsed: mem.used,
                memTotalFree: mem.free,
                memActualUsed: mem.actualUsed,
                memActualFree: mem.actualFree,
                swapTotal: swap.total,
                swapUsed: swap.used,
                swapFree: swap.free,
                memTotalUsedPercentage: mem.usedPercent,
                memTotalFreePercentage: mem.freePercent,
                swapUsedPercentage: (swap.total > 0) ? swap.used / swap.total : 0
        )
    }

    private Processes detectProcesses() {
        new Processes(
                count: sigar.getProcList().length
                // FIXME .. maybe more detailed ??
        )
    }

    private NetworkUsage detectNetworkUsage() {
        def networkUsage = new NetworkUsage()
        sigar.netInterfaceList.each { ifName ->
            def stat = sigar.getNetInterfaceStat(ifName)
            networkUsage.addNetworkInterfaceUsage(new NetworkInterfaceUsage(
                    name: ifName,
                    bytesIn: stat.rxBytes,
                    bytesOut: stat.txBytes
            ))
        }
        return (networkUsage)
    }

    private detectFileSystemsUsage() {
        def fsUsage = new FileSystemsUsage()
        sigar.fileSystemList.each { fs ->
            def usage = sigar.getFileSystemUsage(fs.dirName)
            fsUsage.addFileSystemsUsage(new FileSystemUsage(
                    deviceName: fs.devName,
                    directory: fs.dirName,
                    readBytes: usage.diskReadBytes,
                    reads: usage.diskReads,
                    writeBytes: usage.diskWriteBytes,
                    writes: usage.diskWrites
            ))
        }
        return (fsUsage)
    }

    @Override
    MonitorSample generateSample() {
        if (sigar != null) {
            new MonitorSample(
                    loadAverage: detectLoadAverage(),
                    cpusUsage: detectCpusUsage(),
                    memoryUsage: detectMemoryUsage(),
                    processes: detectProcesses(),
                    networkUsage: detectNetworkUsage(),
                    fileSystemsUsage: detectFileSystemsUsage()
            )
        } else {
            throw new DetectorUnavailableException("Sigar library was not loaded and can't be used.")
        }
    }

    @Override
    void activate(IServiceRegistrator registry) {
        registry.registerService(Detector, this)
    }

    @Override
    void initialize() {
        this.sigarTmpDir = Files.createTempDirectory(this.tmpDir.toPath(), "sigar")
        this.sigar = loadSigar()
    }

    @Override
    void start() {

    }

    @Override
    void notifyStarted() {

    }

}
