package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;

/**
 * Created by donarus on 20.4.15.
 */
public final class Processes implements Serializable {

    private static final long serialVersionUID = -2083557489995818144L;

    private int count;

    public int getCount() {
        return count;
    }

    public Processes setCount(int count) {
        this.count = count;
        return this;
    }

}
