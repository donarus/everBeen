package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;

/**
 * Created by donarus on 18.4.15.
 */
public final class LoadAverage implements Serializable {

    private static final long serialVersionUID = -8888187624391680971L;

    private double load1;

    private double load5;

    private double load15;

    public double getLoad1() {
        return load1;
    }

    public LoadAverage setLoad1(double load1) {
        this.load1 = load1;
        return this;
    }

    public double getLoad5() {
        return load5;
    }

    public LoadAverage setLoad5(double load5) {
        this.load5 = load5;
        return this;
    }

    public double getLoad15() {
        return load15;
    }

    public LoadAverage setLoad15(double load15) {
        this.load15 = load15;
        return this;
    }
}
