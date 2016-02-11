package cz.cuni.mff.d3s.been.swrepoclient;

import cz.cuni.mff.d3s.been.datastore.SoftwareStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory for <em>Software Repository</em> clients
 */
@Component
public class SwRepoClientFactory {

	@Autowired
	private SoftwareStore softwareCache;

	/**
	 * Create a client factory
	 *
	 * @param softwareCache Software cache for the clients to use
	 */
	public SwRepoClientFactory(SoftwareStore softwareCache) {
		this.softwareCache = softwareCache;
	}

	/**
	 * Create a client
	 *
	 * @param hostname Host name for the client to connect to
	 * @param port Port for the client to connect to
	 *
	 * @return The client
	 */
	public SwRepoClient getClient(String hostname, int port) {
		return new HttpSwRepoClient(hostname, port, softwareCache);
	}
}
