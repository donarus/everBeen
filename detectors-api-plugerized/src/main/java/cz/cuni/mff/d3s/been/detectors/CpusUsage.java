package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by donarus on 18.4.15.
 */
public final class CpusUsage implements Serializable {

    private static final long serialVersionUID = -627158923853223717L;

    private List<CpuUsage> cpusUsage = new ArrayList<>();

    private double totalPercentage;

    public CpusUsage addCpuUsage(CpuUsage cpuUsage) {
        cpusUsage.add(cpuUsage);
        return this;
    }

    public List<CpuUsage> getCpusUsage() {
        return cpusUsage;
    }

    public double getTotalPercentage() {
        return totalPercentage;
    }

    public CpusUsage setTotalPercentage(double totalPercentage) {
        this.totalPercentage = totalPercentage;
        return this;
    }
}
