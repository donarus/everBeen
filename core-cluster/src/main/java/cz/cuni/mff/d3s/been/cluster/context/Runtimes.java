package cz.cuni.mff.d3s.been.cluster.context;

import java.util.Collection;

import com.hazelcast.core.IMap;

import cz.cuni.mff.d3s.been.cluster.Names;
import cz.cuni.mff.d3s.been.core.ri.RuntimeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Utility class for operations related to host runtimes.
 * 
 * @author Martin Sixta
 */
@Component
public class Runtimes {

	/** BEEN cluster connection */
	@Autowired
	private ClusterContext clusterCtx;

	/**
	 * @return collection clone (changes not reflected) of all registered host
	 *         runtimes. </br>
	 * 
	 *         <b>Warning!</b> modifying the returned list does not affect the
	 *         original list.
	 */
	public Collection<RuntimeInfo> getRuntimes() {
		return getRuntimeMap().values();
	}

	/**
	 * Stores given {@link RuntimeInfo} in cluster.
	 * 
	 * @param runtimeInfo
	 *          the host runtime information to store
	 */
	public void storeRuntimeInfo(RuntimeInfo runtimeInfo) {
		getRuntimeMap().put(runtimeInfo.getId(), runtimeInfo);
	}

	/**
	 * Removes stored {@link RuntimeInfo} identified by given id from cluster.
	 * 
	 * @param runtimeInfo
	 *          the runtimeInfo to be removed
	 */
	public void removeRuntimeInfo(RuntimeInfo runtimeInfo) {
		getRuntimeMap().remove(runtimeInfo.getId());
	}

	/**
	 * @return modifiable map of all registered Host Runtimes.
	 */
	public IMap<String, RuntimeInfo> getRuntimeMap() {
		return clusterCtx.getMap(Names.HOSTRUNTIMES_MAP_NAME);
	}

}
