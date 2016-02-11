package cz.cuni.mff.d3s.been.node;

import com.hazelcast.config.Config;
import com.hazelcast.config.ServiceConfig;
import com.hazelcast.config.ServicesConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import cz.cuni.mff.d3s.been.cluster.rpc.RemoteServiceRegistrator;
import cz.cuni.mff.d3s.been.commons.NodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.List;

public class HazelcastInstanceFactoryBean extends AbstractFactoryBean<HazelcastInstance> {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private NodeType nodeType;

    @Autowired
    private List<RemoteServiceRegistrator> remoteServiceRegistrators;

    @Override
    public Class<?> getObjectType() {
        return HazelcastInstance.class;
    }

    @Override
    protected HazelcastInstance createInstance() throws Exception {
        ServicesConfig ssc = new ServicesConfig();
        for (RemoteServiceRegistrator registrator : remoteServiceRegistrators) {
            ssc.addServiceConfig(new ServiceConfig()
                    .setEnabled(true)
                    .setName(registrator.getServiceName())
                    .setClassName(registrator.getServiceClass().getName()));
        }

        boolean lite;
        if (nodeType == NodeType.DATA) {
            lite = false;
        } else if (nodeType == NodeType.LITE) {
            lite = true;
        } else {
            // FIXME throw some reasonable exception
            throw new Exception("unknown node type " + nodeType);
        }

        return Hazelcast.newHazelcastInstance(new Config().setServicesConfig(ssc).setLiteMember(lite));
    }

}
