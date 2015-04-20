package cz.cuni.mff.d3s.been.detectors;

/**
 * Created by donarus on 20.4.15.
 */
public class DetectorUnavailableException extends Exception {

    public DetectorUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public DetectorUnavailableException(Throwable cause) {
        super(cause);
    }

    public DetectorUnavailableException(String message) {
        super(message);
    }

}
