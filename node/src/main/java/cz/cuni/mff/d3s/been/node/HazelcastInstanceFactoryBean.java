package cz.cuni.mff.d3s.been.node;

import com.hazelcast.config.Config;
import com.hazelcast.config.ServiceConfig;
import com.hazelcast.config.ServicesConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import cz.cuni.mff.d3s.been.commons.exceptions.NodeException;
import cz.cuni.mff.d3s.been.commons.nodeinfo.NodeType;
import cz.cuni.mff.d3s.been.service.rpc.RemoteServiceDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.List;

/**
 * This class is responsible for instantiation of correct type of {@link HazelcastInstance} based on
 * configuration of {@link NodeType}. During the instantiation it also registers all remote services
 * implementing interface {@link RemoteServiceDefinition}.
 */
final class HazelcastInstanceFactoryBean extends AbstractFactoryBean<HazelcastInstance> {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private NodeType nodeType;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired(required = false)
    private List<RemoteServiceDefinition> serviceDefinitions;

    @Override
    public Class<?> getObjectType() {
        return HazelcastInstance.class;
    }

    @Override
    protected HazelcastInstance createInstance() throws NodeException {
        return Hazelcast.newHazelcastInstance(
                new Config()
                        .setServicesConfig(createServiceConfig())
                        .setLiteMember(runAsLiteNode())
        );
    }

    @Override
    protected void destroyInstance(HazelcastInstance instance) throws Exception {
        instance.shutdown();
    }

    private ServicesConfig createServiceConfig() {
        ServicesConfig config = new ServicesConfig();
        if (serviceDefinitions != null) {
            for (RemoteServiceDefinition definition : serviceDefinitions) {
                config.addServiceConfig(new ServiceConfig()
                        .setEnabled(true)
                        .setName(definition.getServiceName())
                        .setClassName(definition.getServiceClass().getName()));
            }
        }
        return config;
    }

    private boolean runAsLiteNode() throws NodeException {
        if (nodeType == NodeType.DATA) {
            return false;
        } else if (nodeType == NodeType.LITE) {
            return true;
        }

        throw new NodeException("Unknown node type ['%s']", nodeType);
    }

}
