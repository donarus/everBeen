package cz.cuni.mff.d3s.been.detectors.sigar

import java.util.concurrent.TimeUnit

/**
 * Created by donarus on 21.4.15.
 *
 * For testing purposes only
 */
abstract class DetectorRunner {

    public static void main(String[] args) {
        def tmpDir = File.createTempDir()
        System.addShutdownHook { tmpDir.delete() }
        def detector = new SigarDetector(
                tmpDir: tmpDir
        )
        detector.initialize()

        def counter = 0

        // some cycles to ramp up JIT compiler
        for (i in 1..1000) {
            detector.generateSample()
        }

        def runSeconds = 10

        def startTime = System.nanoTime()
        while (System.nanoTime() - startTime < TimeUnit.SECONDS.toNanos(runSeconds)) {
            detector.generateSample()
            counter++;
        }


        System.out.println("Monitoring performs ${counter / runSeconds} samples/second");
    }
}