package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by donarus on 18.4.15.
 */
public final class CpusInfo implements Serializable {

    private static final long serialVersionUID = 3282219975084664885L;

    private List<CpuInfo> cpusInfo = new ArrayList<>();

    public CpusInfo addCpuInfo(CpuInfo cpuInfo) {
        cpusInfo.add(cpuInfo);
        return this;
    }

    public List<CpuInfo> getCpusInfo() {
        return cpusInfo;
    }

}
