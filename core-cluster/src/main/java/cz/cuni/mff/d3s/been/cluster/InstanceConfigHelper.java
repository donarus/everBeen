package cz.cuni.mff.d3s.been.cluster;

import com.hazelcast.config.*;
import cz.cuni.mff.d3s.been.util.PropertyReader;

import java.net.URL;
import java.util.Properties;

import static cz.cuni.mff.d3s.been.cluster.ClusterConfiguration.*;
import static cz.cuni.mff.d3s.been.cluster.ClusterConfiguration.LOGGING_TYPE.NONE;
import static cz.cuni.mff.d3s.been.cluster.ClusterConfiguration.LOGGING_TYPE.SLF4J;
import static cz.cuni.mff.d3s.been.mapstore.MapStoreConfiguration.*;

/**
 * Utility class for creating Hazelcast configurations.
 *
 * @author Martin Sixta
 * @author Radek Macha
 */
final class InstanceConfigHelper {

	/** Separator of list values */
	static final String VALUE_SEPARATOR = ";";

	/** Path to resource with default hazelcast configuration */
	static final String CONFIG_RESOURCE = "/hazelcast.xml";

	/** Name of hazelcast property for preferred TCP/IP stack */
	static final String PROPERTY_HAZELCAST_PREFER_IPV4_STACK = "hazelcast.prefer.ipv4.stack";

	/** Name of the hazelcast property to control binding of local interfaces */
	static final String PROPERTY_HAZELCAST_SOCKET_BIND_ANY = "hazelcast.socket.bind.any";

	/** How will Hazelcast log its messages */
	private static final String PROPERTY_HAZELCAST_LOGGING_TYPE = "hazelcast.logging.type";

	/**
	 * Properties which are used to create configs.
	 *
	 * The resulting properties are created by merging default and user
	 * properties.
	 */
	private final PropertyReader propReader;

	/**
	 * Creates the helper class.
	 *
	 * @param userProperties
	 *          user properties
	 *
	 */
	private InstanceConfigHelper(final Properties userProperties) {
		this.propReader = PropertyReader.on(userProperties);
	}

	/**
	 * Creates configuration for a Hazelcast cluster member
	 *
	 * @param userProperties
	 *          BEEN's user-defined properties
	 * @return Hazelcast member configuration
	 * @throws ServiceException
	 *           if configuration cannot be created
	 */
	static Config createMemberConfig(final Properties userProperties) throws ServiceException {
		return new InstanceConfigHelper(userProperties).createMemberConfig();
	}

	/**
	 *
	 * The actual function which creates Config.
	 *
	 * @return Hazelcast member configuration
	 * @throws ServiceException
	 *           if configuration cannot be created
	 */
	Config createMemberConfig() throws ServiceException {
		URL url = InstanceConfigHelper.class.getResource(CONFIG_RESOURCE);

		Config config;
		try {
			// set logging type early otherwise loading of config will produce stderr messages
			System.setProperty(PROPERTY_HAZELCAST_LOGGING_TYPE, getLoggingMode());

			// create default config
			config = new UrlXmlConfig(url);
		} catch (Exception e) {
			String msg = String.format("Cannot read Hazelcast's default configuration %s", CONFIG_RESOURCE);
			throw new ServiceException(msg, e);
		}

		// override with user-defined configuration
		overrideConfiguration(config);

		setMapConfig(config);

		return config;
	}

	/**
	 * Sets Maps properties
	 *
	 * @param config
	 *          Hazelcast config
	 */
	private void setMapConfig(final Config config) {
		MapConfig tasksMap = new MapConfig(Names.TASKS_MAP_NAME);
		MapConfig contextsMap = new MapConfig(Names.TASK_CONTEXTS_MAP_NAME);
		MapConfig benchmarksMap = new MapConfig(Names.BENCHMARKS_MAP_NAME);
		MapConfig namedTaskDescriptorsMap = new MapConfig(Names.NAMED_TASK_DESCRIPTORS_MAP_NAME);
		MapConfig namedTaskContextDescriptorsMap = new MapConfig(Names.NAMED_TASK_CONTEXT_DESCRIPTORS_MAP_NAME);

		final int backupCount = propReader.getInteger(BACKUP_COUNT, DEFAULT_BACKUP_COUNT);

		// do not locally cache to avoid synchronization hell
		tasksMap.setNearCacheConfig(new NearCacheConfig().setCacheLocalEntries(false)).setBackupCount(backupCount);

		contextsMap.setBackupCount(backupCount);

		benchmarksMap.setBackupCount(backupCount);

		if (propReader.getBoolean(USE_MAP_STORE, DEFAULT_USE_MAP_STORE)) {
			String factoryClassName = propReader.getString(MAP_STORE_FACTORY, DEFAULT_MAP_STORE_FACTORY);
			int writeDelay = propReader.getInteger(MAP_STORE_WRITE_DELAY, DEFAULT_MAP_STORE_WRITE_DELAY);

			MapStoreConfig mapStoreConfig = new MapStoreConfig();

			mapStoreConfig.setEnabled(true).setFactoryClassName(factoryClassName).setWriteDelaySeconds(writeDelay);

			String dbname = propReader.getString(MAP_STORE_DB_NAME, DEFAULT_MAP_STORE_DB_NAME);
			String username = propReader.getString(MAP_STORE_DB_USERNAME, DEFAULT_MAP_STORE_DB_USERNAME);
			String hostname = propReader.getString(MAP_STORE_DB_HOSTNAME, DEFAULT_MAP_STORE_DB_HOSTNAME);
			String password = propReader.getString(MAP_STORE_DB_PASSWORD, DEFAULT_MAP_STORE_DB_PASSWORD);

			if (username == null) {
				username = "";
			}

			if (password == null) {
				password = "";
			}

			mapStoreConfig.setProperty(MAP_STORE_DB_USERNAME, username);
			mapStoreConfig.setProperty(MAP_STORE_DB_PASSWORD, password);
			mapStoreConfig.setProperty(MAP_STORE_DB_HOSTNAME, hostname);
			mapStoreConfig.setProperty(MAP_STORE_DB_NAME, dbname);

			tasksMap.setMapStoreConfig(mapStoreConfig);
			contextsMap.setMapStoreConfig(mapStoreConfig);
			benchmarksMap.setMapStoreConfig(mapStoreConfig);
			namedTaskDescriptorsMap.setMapStoreConfig(mapStoreConfig);
			namedTaskContextDescriptorsMap.setMapStoreConfig(mapStoreConfig);

		}

		config.addMapConfig(tasksMap).addMapConfig(contextsMap).addMapConfig(benchmarksMap).addMapConfig(
				namedTaskDescriptorsMap).addMapConfig(namedTaskContextDescriptorsMap);

	}

	/**
	 * Overrides the default configuration with user-defined values.
	 *
	 * @param mainConfig
	 *          Config to override
	 * @throws ServiceException
	 */
	private void overrideConfiguration(final Config mainConfig) throws ServiceException {
		final int port = propReader.getInteger(PORT, DEFAULT_PORT);
		final InterfacesConfig interfaces = createInterfaces(propReader.getString(INTERFACES, DEFAULT_INTERFACES));
		final GroupConfig groupConfig = createGroupConfig();
		final JoinConfig joinConfig = createJoinConfig();

		final NetworkConfig networkConfig = new NetworkConfig();

		networkConfig.setPort(port).setInterfaces(interfaces).setJoin(joinConfig);
		mainConfig.setNetworkConfig(networkConfig).setGroupConfig(groupConfig);
		mainConfig.setProperty(
				PROPERTY_HAZELCAST_PREFER_IPV4_STACK,
				propReader.getBoolean(PREFER_IPV4, DEFAULT_PREFER_IPV4).toString());
		mainConfig.setProperty(
				PROPERTY_HAZELCAST_SOCKET_BIND_ANY,
				propReader.getBoolean(SOCKET_BIND_ANY, DEFAULT_SOCKET_BIND_ANY).toString());
	}

	/**
	 * Creates Hazelcast {@link JoinConfig} from configuration.
	 *
	 * The {@link JoinConfig} class determines how cluster members will get to know
	 * about each other.
	 *
	 * @return Hazelcast {@link JoinConfig} configuration.
	 *
	 * @throws ServiceException
	 *           when config cannot be created
	 */
	private JoinConfig createJoinConfig() throws ServiceException {
		final JoinConfig join = new JoinConfig();

		switch (getJoinType()) {
			case MULTICAST:
				join.setMulticastConfig(createMulticastConfig());
				join.getTcpIpConfig().setEnabled(false);
				break;
			case TCP:
				join.setTcpIpConfig(createTcpIpConfig());
				join.getMulticastConfig().setEnabled(false); // enabled by default
				break;
			default:
				throw new ServiceException("Unimplemented join type " + getJoinType());
		}

		return join;

	}

	/**
	 * Creates Hazelcast {@link GroupConfig} from configuration.
	 *
	 * This determines properties of the group of members.
	 *
	 * @return {@link GroupConfig} for the Hazelcast member
	 */
	private GroupConfig createGroupConfig() {
		String group = propReader.getString(GROUP, DEFAULT_GROUP);
		String password = propReader.getString(PASSWORD, DEFAULT_PASSWORD);
		return new GroupConfig().setName(group).setPassword(password);
	}

	/**
	 * Creates Hazelcast {@link MulticastConfig} from configuration.
	 *
	 * @return {@link MulticastConfig} for the Hazelcast member
	 *
	 * @throws ServiceException
	 *           when the config cannot be created due to config file errors
	 */
	private MulticastConfig createMulticastConfig() throws ServiceException {
		MulticastConfig multicastConfig = new MulticastConfig();
		multicastConfig.setEnabled(true);
		multicastConfig.setMulticastGroup(propReader.getString(MULTICAST_GROUP, DEFAULT_MULTICAST_GROUP));
		multicastConfig.setMulticastPort(propReader.getInteger(MULTICAST_PORT, DEFAULT_MULTICAST_PORT));

		return multicastConfig;
	}

	/**
	 * Creates Hazelcast {@link TcpIpConfig} from configuration.
	 *
	 * @return {@link TcpIpConfig} for the Hazelcast member
	 *
	 * @throws ServiceException
	 *           when the config cannot be created due to config file errors
	 */
	private TcpIpConfig createTcpIpConfig() throws ServiceException {
		TcpIpConfig tcpIpConfig = new TcpIpConfig();
		tcpIpConfig.setEnabled(true);

		for (String address : propReader.getString(TCP_MEMBERS, DEFAULT_TCP_MEMBERS).split(VALUE_SEPARATOR)) {
			tcpIpConfig.addMember(address);
		}

		return tcpIpConfig;
	}

	/**
	 * Creates configuration of Hazelcast's interfaces.
	 *
	 * @param interfaceList
	 *          list of interfaces separated by
	 *          {@link InstanceConfigHelper#VALUE_SEPARATOR}
	 * @return Interface configuration of Hazelcast
	 */
	private InterfacesConfig createInterfaces(final String interfaceList) {
		InterfacesConfig interfaces = new InterfacesConfig();

		if (interfaceList == null || interfaceList.isEmpty()) {
			interfaces.setEnabled(false);
			return interfaces;
		}

		for (String ip : interfaceList.split(VALUE_SEPARATOR)) {
			interfaces.addInterface(ip);
		}

		interfaces.setEnabled(true);
		return interfaces;
	}

	/**
	 * Figures out what is the logging type for the instance.
	 *
	 * @return String representing logging type usable in setProperty calls on a
	 *         Hazelcast instance
	 */
	private String getLoggingMode() {
		final boolean enableHazelcastLogging = propReader.getBoolean(LOGGING, DEFAULT_LOGGING);
		return enableHazelcastLogging ? SLF4J.name().toLowerCase() : NONE.name().toLowerCase();
	}

	/**
	 * Determines from the configuration how cluster members will join
	 *
	 * @return the join type
	 * @throws ServiceException
	 *           if unknown join type is specified
	 */
	private JOIN_TYPE getJoinType() throws ServiceException {
		String joinStringValue = propReader.getString(JOIN, DEFAULT_JOIN);
		try {
			return JOIN_TYPE.valueOf(joinStringValue.toUpperCase());
		} catch (IllegalArgumentException e) {
			String msg = String.format("Unknown join type %s", joinStringValue);
			throw new ServiceException(msg, e);
		}
	}
}
