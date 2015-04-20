package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;

/**
 * Created by donarus on 18.4.15.
 */
public final class CpuUsage implements Serializable {

    private static final long serialVersionUID = -5141967143506586236L;

    private double percentage;

    public double getPercentage() {
        return percentage;
    }

    public CpuUsage setPercentage(double percentage) {
        this.percentage = percentage;
        return this;
    }

}
