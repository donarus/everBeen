package cz.cuni.mff.d3s.been.swr;

public class SWRException extends Exception {

    public SWRException() {
        super();
    }

    public SWRException(String message) {
        super(message);
    }

    public SWRException(String message, Throwable cause) {
        super(message, cause);
    }

    public SWRException(Throwable cause) {
        super(cause);
    }
}
