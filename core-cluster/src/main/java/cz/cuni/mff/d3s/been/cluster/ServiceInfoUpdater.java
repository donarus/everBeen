package cz.cuni.mff.d3s.been.cluster;

import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import cz.cuni.mff.d3s.been.cluster.context.Services;
import cz.cuni.mff.d3s.been.core.service.ServiceInfo;

/**
 * 
 * Periodically updates information about a service.
 * 
 * @author donarus
 */
public class ServiceInfoUpdater implements Runnable {

	/** connection to the cluster */
	private final Services services;

	/** the service info */
	private final ServiceInfo info;

	/** timeout for the updater */
	private final int timeout;

	/**
	 * Creates ServiceInfoUpdater
	 * 
	 * @param services
	 *          connection to the cluster
	 * @param info
	 *          service info to update
	 * @param timeout
	 *          timeout validity of the service info
	 */
	public ServiceInfoUpdater(Services services, ServiceInfo info, int timeout) {
		this.services = services;
		this.info = info;
		this.timeout = timeout;
	}

	@Override
	public void run() {
		services.storeServiceInfo(info, timeout);
	}

}
