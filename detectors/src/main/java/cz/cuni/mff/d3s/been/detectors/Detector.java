package cz.cuni.mff.d3s.been.detectors;

import cz.cuni.mff.d3s.been.commons.NodeInfo;

/**
 * Hw and OS detector.
 *
 * @author Kuba Brecka
 */
public final class Detector {

    private final IDetector detector;

    public Detector() {
        SigarDetector nativeDetector = new SigarDetector();
        if (nativeDetector.isSigarAvailable()) {
            detector = nativeDetector;
        } else {
            detector = new JavaDetector();
        }
    }

    /**
     * Detect all possible information available on the Host
     */
    public NodeInfo detect() {
        return new NodeInfo()
                .setJava(detector.detectJava())
                .setOperatingSystem(detector.detectOperatingSystem())
                .setHardware(detector.detectHardware())
                .setFilesystem(detector.detectFilesystems());
    }

}
