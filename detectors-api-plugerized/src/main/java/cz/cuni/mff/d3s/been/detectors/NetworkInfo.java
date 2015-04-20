package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by donarus on 18.4.15.
 */
public final class NetworkInfo implements Serializable {

    private static final long serialVersionUID = -6763169768295037878L;

    private List<NetworkInterfaceInfo> networkInterfacesInfo = new ArrayList<>();

    public NetworkInfo addNetworkInterfaceInfo(NetworkInterfaceInfo networkInterfaceInfo) {
        networkInterfacesInfo.add(networkInterfaceInfo);
        return this;
    }

    public List<NetworkInterfaceInfo> getNetworkInterfacesInfo() {
        return networkInterfacesInfo;
    }

}
