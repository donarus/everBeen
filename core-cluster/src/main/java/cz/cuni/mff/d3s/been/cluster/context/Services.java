package cz.cuni.mff.d3s.been.cluster.context;

import com.hazelcast.core.IMap;

import cz.cuni.mff.d3s.been.cluster.Names;
import cz.cuni.mff.d3s.been.core.service.ServiceInfo;
import cz.cuni.mff.d3s.been.swrepository.SWRepositoryServiceInfoConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Purpose of this class is to associate methods for cluster-wide services like
 * Software Repository or Results Repository.
 * 
 * @author donarus
 */
@Component
public class Services {

	/** BEEN cluster connection */
	@Autowired
	private ClusterContext clusterCtx;

	/**
	 * @return {@link ServiceInfo} of registered Repository or null if Software
	 *         repository has not been registered yes.
	 */
	public ServiceInfo getSWRepositoryInfo() {
		Object swRepObject = getServicesMap().get(SWRepositoryServiceInfoConstants.SERVICE_NAME);
		try {
			return (ServiceInfo) swRepObject;
		} catch (ClassCastException e) {
			e.printStackTrace();
			throw new RuntimeException(String.format(
					"Object in servicesMap under the key '%s' is instance of type '%s' but it should be '%s'",
					SWRepositoryServiceInfoConstants.SERVICE_NAME,
					swRepObject.getClass().getName(),
					ServiceInfo.class.getName()), e);
		}
	}

	/*    public Map<String, String> getServicesInfo() {
	        HashMap<String, String> result = new HashMap<>();

	        ServiceInfo swRepositoryInfo = getSWRepositoryInfo();
	        if (swRepositoryInfo != null) {
	            String swRepoString = swRepositoryInfo.getHost() + ":" + swRepositoryInfo.getHttpServerPort();
	            result.put("SOFTWARE_REPOSITORY", swRepoString);
	        }

	        return result;
	    }*/

	/**
	 * @return modifiable map of all registered Services.
	 */
	public IMap<String, ServiceInfo> getServicesMap() {
		return clusterCtx.getMap(Names.SERVICES_MAP_NAME);
	}

	/**
	 * Removes the entry about software repository from the map that holds
	 * information about BEEN services.
	 */
	public void removeSoftwareRepositoryInfo() {
		getServicesMap().remove(SWRepositoryServiceInfoConstants.SERVICE_NAME);
	}


	/**
	 * Registers a service.
	 *
	 * @param serviceInfo
	 *          service info object to be registered
	 * @param ttlSeconds
	 *          the time-to-live in seconds after which the entry in the map
	 *          should be evicted
	 */
	// TODO: check for concurency issues
	public void storeServiceInfo(ServiceInfo serviceInfo, int ttlSeconds) {
		getServicesMap().put(serviceInfo.getServiceName(), serviceInfo, ttlSeconds, TimeUnit.SECONDS);
	}

	/**
	 * Un-registers a service.
	 *
	 * @param serviceInfo
	 *          service info object to be unregistered
	 */
	// TODO: check for concurency issues
	public void removeServiceInfo(ServiceInfo serviceInfo) {
		getServicesMap().remove(serviceInfo.getServiceName());
	}
}
