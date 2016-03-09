package cz.cuni.mff.d3s.been.commons.exceptions;

public class NodeException extends Exception {

    public NodeException(String messageFormat, Object... args) {
        super(String.format(messageFormat, args));
    }

    public NodeException(Throwable cause, String messageFormat, Object... args) {
        super(String.format(messageFormat, args), cause);
    }

    public NodeException(Throwable cause) {
        super(cause);
    }

}
