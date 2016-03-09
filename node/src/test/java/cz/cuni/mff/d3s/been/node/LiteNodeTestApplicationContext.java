package cz.cuni.mff.d3s.been.node;

import cz.cuni.mff.d3s.been.commons.nodeinfo.NodeType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class LiteNodeTestApplicationContext {

    @Bean(name = "nodeType")
    NodeType nodeType() {
        return NodeType.LITE;
    }

    @Bean(name = "properties")
    Properties properties() {
        return new Properties();
    }

}
