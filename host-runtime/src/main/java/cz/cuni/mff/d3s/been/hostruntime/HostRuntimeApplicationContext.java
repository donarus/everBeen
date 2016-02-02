package cz.cuni.mff.d3s.been.hostruntime;

import cz.cuni.mff.d3s.been.cluster.ServiceException;
import cz.cuni.mff.d3s.been.cluster.context.ClusterContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Properties;

/**
 * Created by donarus on 2.2.16.
 */
@Configuration
@Profile("host-runtime")
public class HostRuntimeApplicationContext {

    @Bean(name = "hostRuntime")
    public HostRuntime hostRuntime(
            @SuppressWarnings("SpringJavaAutowiringInspection") @Qualifier("clusterContext") ClusterContext clusterContext,
            @SuppressWarnings("SpringJavaAutowiringInspection") @Qualifier("properties") Properties properties) {
        HostRuntime hostRuntime = HostRuntimes.createRuntime(clusterContext, properties);
        try {
            hostRuntime.start();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return hostRuntime;
    }

    @Bean(name = "hostRuntimeId")
    public String hostRuntimeId(@Qualifier("hostRuntime") HostRuntime hostRuntime) {
        return hostRuntime.getId();
    }

}
