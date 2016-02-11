package cz.cuni.mff.d3s.been.hostruntime;

import cz.cuni.mff.d3s.been.hostruntime.processmanager.BpkLoader;
import cz.cuni.mff.d3s.been.hostruntime.processmanager.BpkReceiver;
import cz.cuni.mff.d3s.been.hostruntime.processmanager.CmdLineBuilder;
import cz.cuni.mff.d3s.been.hostruntime.processmanager.ProcessManager;
import cz.cuni.mff.d3s.been.service.BeenUUIDGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;

/**
 * Created by donarus on 2.2.16.
 */
@Configuration
@Profile("host-runtime")
public class HostRuntimeApplicationContext {

    @Bean(name = "hostRuntimeId")
    String hostRuntimeId(BeenUUIDGenerator beenUUIDGenerator) {
        return beenUUIDGenerator.generate();
    }

    @Bean(name = "hostRuntimeWorkingDirectory")
    File hostRuntimeWorkingDirectory(@Qualifier("beenWorkingDirectory") File beenWorkDir) {
        return mkdir(beenWorkDir, "host-runtime");
    }

    @Bean(name = "hostRuntimeTasksWorkingDirectory")
    File hostRuntimeTasksWorkingDirectory(@Qualifier("hostRuntimeWorkingDirectory") File hrWorkDir) {
        return mkdir(hrWorkDir, "tasks");
    }

    @Bean(name = "hostRuntimeProcessManager")
    ProcessManager processManager() {
        return new ProcessManager();
    }

    @Bean(name = "hostRuntimeBpkLoader")
    BpkLoader bpkLoader() {
        return new BpkLoader();
    }

    @Bean(name = "hostRuntimeBpkReceiver")
    BpkReceiver bpkReceiver() {
        return new BpkReceiver();
    }

    @Bean(name = "cmdLineBuilder")
    CmdLineBuilder cmdLineBuilder() {
        return new CmdLineBuilder();
    }

    private final File mkdir(File parent, String child) {
        File dir = parent.toPath().resolve(child).toFile();
        dir.mkdir();
        return dir;
    }

}
