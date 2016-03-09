package cz.cuni.mff.d3s.been.detectors;

import cz.cuni.mff.d3s.been.commons.nodeinfo.*;
import org.apache.commons.collections.EnumerationUtils;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Java based fallback detector.
 *
 * @author Kuba Brecka
 */
public class JavaDetector implements IDetector {

    /**
     * Operation system info
     */
    public OperatingSystem detectOperatingSystem() {
        OperatingSystem os = new OperatingSystem();
        os.setName(System.getProperty("os.name"));
        os.setArch(System.getProperty("os.arch"));
        os.setVersion(System.getProperty("os.version"));
        return os;
    }

    /**
     * Hardware info
     */
    public Hardware detectHardware() {
        Hardware hw = new Hardware();

        Memory mem = new Memory();
        mem.setRam(getTotalMemoryFromReflection());
        hw.setMemory(mem);

        List<Cpu> cpus = new ArrayList<>();
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            Cpu cpu = new Cpu();
            cpus.add(cpu);
        }
        hw.setCpu(cpus);

        try {
            Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();

            List<cz.cuni.mff.d3s.been.commons.nodeinfo.NetworkInterface> networkInterfaces = new ArrayList<>();
            for (Object i : EnumerationUtils.toList(ifs)) {
                NetworkInterface iface = (NetworkInterface) i;
                cz.cuni.mff.d3s.been.commons.nodeinfo.NetworkInterface networkInterface = new cz.cuni.mff.d3s.been.commons.nodeinfo.NetworkInterface();
                networkInterface.setName(iface.getName());
                networkInterface.setMtu(iface.getMTU());

                for (Object o : EnumerationUtils.toList(iface.getInetAddresses())) {
                    InetAddress a = (InetAddress) o;
                    networkInterface.setAddress(a.getHostAddress());
                }

                networkInterfaces.add(networkInterface);
            }
            hw.setNetworkInterface(networkInterfaces);
        } catch (SocketException e) {
            // do nothing
        }

        return hw;
    }

    /**
     * File system info
     */
    public List<Filesystem> detectFilesystems() {
        List<Filesystem> filesystems = new ArrayList<>();
        for (File root : File.listRoots()) {
            Filesystem f = new Filesystem();
            f.setDirectory(root.getAbsolutePath());
            f.setFree(root.getFreeSpace());
            f.setTotal(root.getTotalSpace());
            filesystems.add(f);
        }
        return filesystems;
    }

    private long getTotalMemoryFromReflection() {
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();

        try {
            Method a = os.getClass().getMethod("getTotalPhysicalMemorySize");
            a.setAccessible(true);
            Object o = a.invoke(os);
            return (Long) o;
        } catch (Throwable e) {
            // do nothing
        }

        return 0;
    }

}
