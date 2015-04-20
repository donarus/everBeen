package cz.cuni.mff.d3s.been.detectors.sigar

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Requires
import spock.lang.Specification

/**
 * Created by donarus on 20.4.15.
 */
class SigarDetectorTest extends Specification {

    @Rule
    TemporaryFolder temporaryFolder

    //
    //
    //
    // FOLLOWING TESTS ARE OS DEPENDENT!!!!
    //
    //
    //

    @Requires({ os.linux })
    def 'test linux detectors detects at least something, no matter what'() {
        given:
            def detector = new SigarDetector(
                    tmpDir: temporaryFolder.newFolder()
            )
            detector.initialize()

        when:
            def machineInfo = detector.detectMachineInfo()

        then:
            // FS INFO
            machineInfo.fileSystemsInfo.fileSystemsInfo*.deviceName.each { it != null }
            machineInfo.fileSystemsInfo.fileSystemsInfo*.directory.each { it != null }
            machineInfo.fileSystemsInfo.fileSystemsInfo*.type.each { it != null }
            machineInfo.fileSystemsInfo.fileSystemsInfo*.free.each { it != null }
            machineInfo.fileSystemsInfo.fileSystemsInfo*.total.each { it != null }

            // HW INFO
            machineInfo.hardwareInfo.cpusInfo.cpusInfo.size() > 0
            machineInfo.hardwareInfo.cpusInfo.cpusInfo*.cacheSize.each { it != null }
            machineInfo.hardwareInfo.cpusInfo.cpusInfo*.mhz.each { it != null }
            machineInfo.hardwareInfo.cpusInfo.cpusInfo*.model.each { it != null }
            machineInfo.hardwareInfo.cpusInfo.cpusInfo*.vendor.each { it != null }

            machineInfo.hardwareInfo.memoryInfo.memorySize != null
            machineInfo.hardwareInfo.memoryInfo.swapSize != null

            machineInfo.hardwareInfo.networkInfo.networkInterfacesInfo*.hwaddr.each { it != null }
            machineInfo.hardwareInfo.networkInfo.networkInterfacesInfo*.name.each { it != null }
            // other fields on network interface can't be easily
            // tested because we don't kno if they are set.

            // OS INFO
            machineInfo.operatingSystemInfo.name == 'Linux'
            machineInfo.operatingSystemInfo.vendor != null
            machineInfo.operatingSystemInfo.version != null
            machineInfo.operatingSystemInfo.vendorVersion != null
            machineInfo.operatingSystemInfo.architecture != null
            machineInfo.operatingSystemInfo.dataModel == '32' || machineInfo.operatingSystemInfo.dataModel == '64' // maybe too strong condition
            machineInfo.operatingSystemInfo.endian == 'little' || machineInfo.operatingSystemInfo.endian == 'big' // linux on sparc = big, linux on x86 = small
    }

    @Requires({ os.linux })
    def 'test linux usage sample samples at least something, no matter what'() {
        given:
            def detector = new SigarDetector(
                    tmpDir: temporaryFolder.newFolder()
            )
            detector.initialize()

        when:
            def sample = detector.generateSample()

        then:
            // CPU SAMPLE
            sample.cpusUsage.totalPercentage >= 0
            sample.cpusUsage.totalPercentage <= 1
            sample.cpusUsage.cpusUsage*.percentage.each { it >= 0 }
            sample.cpusUsage.cpusUsage*.percentage.each { it <= 1 }

            // FS SAMPLE
            sample.fileSystemsUsage.fileSystemsUsages*.deviceName.each { it != null }
            sample.fileSystemsUsage.fileSystemsUsages*.directory.each { it != null }
            sample.fileSystemsUsage.fileSystemsUsages*.readBytes.each { it != null }
            sample.fileSystemsUsage.fileSystemsUsages*.reads.each { it != null }
            sample.fileSystemsUsage.fileSystemsUsages*.writeBytes.each { it != null }
            sample.fileSystemsUsage.fileSystemsUsages*.writes.each { it != null }

            // LOAD SAMPLE
            sample.loadAverage.load1 >= 0d
            sample.loadAverage.load5 >= 0d
            sample.loadAverage.load15 >= 0d

            // MEMORY SAMPLE
            sample.memoryUsage.memActualFree >= 0
            sample.memoryUsage.memActualUsed >= 0
            sample.memoryUsage.memTotal >= 0
            sample.memoryUsage.memTotalFree >= 0
            sample.memoryUsage.memTotalUsed >= 0
            sample.memoryUsage.memTotalFreePercentage >= 0
            sample.memoryUsage.memTotalUsedPercentage >= 0
            sample.memoryUsage.swapFree >= 0
            sample.memoryUsage.swapTotal >= 0
            sample.memoryUsage.swapUsed >= 0
            sample.memoryUsage.swapUsedPercentage >= 0

            // PROCESS SAMPLE
            sample.processes.count >= 0

            // NETWORK INTERFACES SAMPLE
            sample.networkUsage.networkInterfaceUsages*.bytesIn.each { it >= 0 }
            sample.networkUsage.networkInterfaceUsages*.bytesOut.each { it >= 0 }
            sample.networkUsage.networkInterfaceUsages*.name.each { it != null }
    }
}
