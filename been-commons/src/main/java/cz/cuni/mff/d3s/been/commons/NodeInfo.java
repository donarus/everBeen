package cz.cuni.mff.d3s.been.commons;

import java.util.Date;
import java.util.List;

public class NodeInfo {

    private String id;
    private String beenWorkingDirectory;
    private NodeType nodeType;
    private Hardware hardware;
    private Date startUpTime;
    private Java java;
    private OperatingSystem operatingSystem;
    private List<Filesystem> filesystem;

}
