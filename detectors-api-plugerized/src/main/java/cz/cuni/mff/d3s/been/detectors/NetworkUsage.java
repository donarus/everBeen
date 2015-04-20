package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by donarus on 20.4.15.
 */
public final class NetworkUsage implements Serializable {

    private static final long serialVersionUID = -103741121455787650L;

    private List<NetworkInterfaceUsage> networkInterfaceUsages = new ArrayList<>();

    public NetworkUsage addNetworkInterfaceUsage(NetworkInterfaceUsage networkInterfaceUsage) {
        networkInterfaceUsages.add(networkInterfaceUsage);
        return this;
    }

    public List<NetworkInterfaceUsage> getNetworkInterfaceUsages() {
        return networkInterfaceUsages;
    }

}
