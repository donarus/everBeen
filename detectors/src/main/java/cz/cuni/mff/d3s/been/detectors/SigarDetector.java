package cz.cuni.mff.d3s.been.detectors;

import cz.cuni.mff.d3s.been.commons.nodeinfo.*;
import cz.cuni.mff.d3s.been.commons.nodeinfo.Cpu;
import org.apache.commons.io.IOUtils;
import org.hyperic.jni.ArchLoader;
import org.hyperic.jni.ArchNotSupportedException;
import org.hyperic.sigar.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


/**
 * Native detector.
 *
 * @author Kuba Brecka
 */
public class SigarDetector implements IDetector {

    private boolean sigarUnavailable = false;

    private Sigar sigar;

    private void loadSigar() throws SigarException {
        if (sigar != null)
            return;

        if (sigarUnavailable)
            return;

        try {

            ArchLoader archLoader = new ArchLoader();
            archLoader.setName("sigar");
            String libName = archLoader.getArchLibName();
            archLoader.setLibName(libName);
            libName = archLoader.getLibraryName();
            InputStream libStream = this.getClass().getResourceAsStream(libName);

            if (libStream == null) {
                sigarUnavailable = true;
                return;
            }

            final Path dirPath = Files.createTempDirectory("been_native_lib");
            final Path filePath = dirPath.resolve(libName);
            OutputStream outputStream = Files.newOutputStream(filePath);

            IOUtils.copy(libStream, outputStream);

            outputStream.close();
            libStream.close();

            // shutdown hook to delete the lib file and temporary directory
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        Files.delete(filePath);
                        Files.delete(dirPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            System.setProperty("org.hyperic.sigar.path", dirPath.toString());

            sigar = new Sigar();

        } catch (IOException | ArchNotSupportedException e) {
            // cannot load sigar native lib, continue
        }
    }

    /**
     * Whether the native detector is available for the current platform.
     *
     * @return Whether the native detector is available for the current platform.
     */
    public boolean isSigarAvailable() {
        try {
            loadSigar();
        } catch (SigarException e) {
            // do nothing
        }

        return !sigarUnavailable;
    }

    /**
     * Detects hardware info
     *
     * @return hardware info
     */
    public Hardware detectHardware() {
        try {
            loadSigar();

            if (sigar == null)
                return null;

            Hardware hw = new Hardware();

            List<cz.cuni.mff.d3s.been.commons.nodeinfo.Cpu> cpus = new ArrayList<>();
            for (CpuInfo i : sigar.getCpuInfoList()) {
                Cpu cpu = new Cpu();
                cpu.setVendor(i.getVendor());
                cpu.setModel(i.getModel());
                cpu.setMhz(i.getMhz());
                cpu.setCacheSize(i.getCacheSize());
                cpus.add(cpu);
            }
            hw.setCpu(cpus);

            Mem mem = sigar.getMem();
            Swap swap = sigar.getSwap();

            hw.setMemory(new Memory());
            hw.getMemory().setRam(mem.getTotal());
            hw.getMemory().setSwap(swap.getTotal());

            List<NetworkInterface> networkInterfaces = new ArrayList<>();
            for (String s : sigar.getNetInterfaceList()) {
                NetInterfaceConfig c = sigar.getNetInterfaceConfig(s);

                NetworkInterface networkInterface = new NetworkInterface();
                networkInterface.setName(c.getName());
                networkInterface.setHwaddr(c.getHwaddr());
                networkInterface.setType(c.getType());
                networkInterface.setMtu(c.getMtu());
                networkInterface.setAddress(c.getAddress());
                networkInterface.setNetmask(c.getNetmask());
                networkInterface.setBroadcast(c.getBroadcast());

                networkInterfaces.add(networkInterface);
            }
            hw.setNetworkInterface(networkInterfaces);

            return hw;
        } catch (SigarException e) {
            return null;
        }
    }

    /**
     * Detects operating system info.
     *
     * @return operating system info.
     */
    public cz.cuni.mff.d3s.been.commons.nodeinfo.OperatingSystem detectOperatingSystem() {
        cz.cuni.mff.d3s.been.commons.nodeinfo.OperatingSystem os = new cz.cuni.mff.d3s.been.commons.nodeinfo.OperatingSystem();

        try {
            loadSigar();

            if (sigar == null)
                return os;

            org.hyperic.sigar.OperatingSystem sys = org.hyperic.sigar.OperatingSystem.getInstance();
            os.setName(sys.getName());
            os.setVersion(sys.getVersion());
            os.setArch(sys.getArch());
            os.setVendor(sys.getVendor());
            os.setVendorVersion(sys.getVendorVersion());
            os.setDataModel(sys.getDataModel());
            os.setEndian(sys.getCpuEndian());
        } catch (SigarException e) {
            // do nothing
        }

        return os;
    }

    /**
     * Detects file system info
     *
     * @return file system info
     */
    public List<Filesystem> detectFilesystems() {
        ArrayList<Filesystem> fslist = new ArrayList<>();

        try {
            loadSigar();
            if (sigar == null)
                return fslist;

            for (FileSystem fs : sigar.getFileSystemList()) {
                FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());

                Filesystem f = new Filesystem();
                f.setDeviceName(fs.getDevName());
                f.setDirectory(fs.getDirName());
                f.setType(fs.getTypeName());
                f.setTotal(usage.getTotal() * 1024);
                f.setFree(usage.getFree() * 1024);
                fslist.add(f);
            }
        } catch (SigarException e) {
            // do nothing
        }

        return fslist;
    }

}
