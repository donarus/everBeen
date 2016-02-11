package cz.cuni.mff.d3s.been.node;

import cz.cuni.mff.d3s.been.service.BeenUUIDGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Configuration
public class ApplicationContext {

    @Bean(name = "beenNodeId")
    public String beenNodeId(BeenUUIDGenerator beenUUIDGenerator) {
        return beenUUIDGenerator.generate();
    }

    @Bean(name = "been")
    public Been been() {
        return new Been();
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
