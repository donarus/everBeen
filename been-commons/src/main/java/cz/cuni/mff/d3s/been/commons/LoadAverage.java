package cz.cuni.mff.d3s.been.commons;

import java.io.Serializable;

public class LoadAverage implements Serializable {

    private double load1;
    private double load5;
    private double load15;

    public double getLoad1() {
        return load1;
    }

    public void setLoad1(double load1) {
        this.load1 = load1;
    }

    public double getLoad5() {
        return load5;
    }

    public void setLoad5(double load5) {
        this.load5 = load5;
    }

    public double getLoad15() {
        return load15;
    }

    public void setLoad15(double load15) {
        this.load15 = load15;
    }
}
