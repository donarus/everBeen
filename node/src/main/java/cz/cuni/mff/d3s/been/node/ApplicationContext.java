package cz.cuni.mff.d3s.been.node;

import com.hazelcast.core.HazelcastInstance;
import cz.cuni.mff.d3s.been.cluster.Instance;
import cz.cuni.mff.d3s.been.cluster.NodeType;
import cz.cuni.mff.d3s.been.cluster.ServiceException;
import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.UUID;

import static cz.cuni.mff.d3s.been.core.StatusCode.*;

@Configuration
public class ApplicationContext {

    private static final Logger log = LoggerFactory.getLogger(ApplicationContext.class);

    @Bean(name = "runtimeId")
    public UUID runtimeId() {
        return UUID.randomUUID();
    }

    @Bean(name = "beenId")
    public String beenId(@Qualifier("runtimeId") UUID runtimeId) {
        try {
            return InetAddress.getLocalHost().getHostName() + "--" + runtimeId.toString();
        } catch (UnknownHostException e) {
            log.error("Cannot determine local hostname, will terminate.", e);
            EX_NETWORK_ERROR.sysExit();
        }

        throw new AssertionError("Unreachable code reached: Exception has been thrown during" +
                " 'beenId' determination and EverBEEN should have been already killed.");
    }

    @Bean(name = "properties")
    public Properties properties() {
        return StartupConfigurationHolder.getProperties();
    }

    @Bean(name = "hazelcastInstance")
    public HazelcastInstance hazelcastInstance(@Qualifier("properties") Properties properties) {
        NodeType nodeType = StartupConfigurationHolder.getNodeType();
        try {
			// Join the cluster
			log.info("The node is connecting to the cluster");
			Instance.init(nodeType, properties);
            HazelcastInstance instance = Instance.getInstance();
			log.info("The node is now connected to the cluster");
            return instance;
		} catch (ServiceException e) {
			log.error("Failed to initialize cluster instance", e);
			EX_COMPONENT_FAILED.sysExit();
		}

        throw new AssertionError("Unreachable code reached: Exception has been thrown during" +
                " 'hazelcastInstance' initialization and EverBEEN should have been already killed.");
    }

    @Bean(name = "clusterContext")
    public ClusterContext clusterContext(@Qualifier("hazelcastInstance") HazelcastInstance properties) {
        // FIXME .. for now we have to pass HCInstance because initializing is done in static way ...
        // .. and that's stupid, stupid, stupid and STUPIIIIID and I don't know why the hell I agreed
        // with the solution without DI in time when we worked on EverBEEN :////
        return Instance.createContext();
    }


//    @Bean(name = "hazelcast")
//    public HazelcastInstance hazelcast() {
//        return Hazelcast.newHazelcastInstance();
//    }
//
//    @Bean(name = "metadataMap")
//    public IMap<String, String> nodes(HazelcastInstance hazelcast) {
//        return hazelcast.getMap("metadataMap");
//    }
//
//    @Bean(name = "nodesMap")
//    public IMap<String, NodeInfo> nodesMap(HazelcastInstance hazelcast) {
//        return hazelcast.getMap("nodesMap");
//    }

}
