package cz.cuni.mff.d3s.been.node;

import com.hazelcast.core.HazelcastInstance;
import cz.cuni.mff.d3s.been.commons.nodeinfo.NodeInfo;
import cz.cuni.mff.d3s.been.commons.nodeinfo.NodeType;
import cz.cuni.mff.d3s.been.detectors.Detector;
import cz.cuni.mff.d3s.been.service.BeenUUIDGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

@Configuration
class NodeApplicationContext {

    @Bean(name = "beenNodeId")
    public String beenNodeId(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getCluster().getLocalMember().getUuid();
    }

    @Bean(name = "node")
    public Node node() {
        return new NodeImpl();
    }

    @Bean
    public NodeInfo nodeInfo(
            @Qualifier("beenNodeId") String beenNodeId,
            NodeType nodeType,
            @Qualifier("beenWorkingDirectory") File beenWorkingDirectory
    ) {
        return new Detector().detect()
                .setId(beenNodeId)
                .setBeenWorkingDirectory(beenWorkingDirectory.getAbsolutePath())
                .setStartUpTime(new Date())
                .setNodeType(nodeType);
    }

    @Bean(name = "beenUUIDGenerator")
    public BeenUUIDGenerator beenUUIDGenerator() {
        return new BeenUUIDGenerator();
    }

    @Bean(name = "hazelcastInstance")
    public HazelcastInstanceFactoryBean hazelcastInstance() {
        return new HazelcastInstanceFactoryBean();
    }

    @Bean(name = "beenWorkingDirectory")
    public File beenWorkingDirectory() {
        try {
            // FIXME for now returning some random tmp directory
            return Files.createTempDirectory(null).toFile();
        } catch (IOException e) {
            // FIXME logging
            throw new RuntimeException(e);
        }
    }

}
