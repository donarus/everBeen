package cz.cuni.mff.d3s.been.swrep;

import cz.cuni.mff.d3s.been.service.BeenUUIDGenerator;
import cz.cuni.mff.d3s.been.swrep.api.SoftwareRepositoryAPIServiceRegistrator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.util.List;

@Configuration
@Profile("software-repository")
public class SoftwareRepositoryApplicationContext {

    @Bean(name = "softwareRepositoryId")
    String softwareRepositoryId(BeenUUIDGenerator beenUUIDGenerator) {
        return beenUUIDGenerator.generate();
    }

    @Bean(name = "softwareRepositoryAPIServiceRegistrator")
    SoftwareRepositoryAPIServiceRegistrator softwareRepositoryAPIServiceRegistrator() {
        return new SoftwareRepositoryAPIServiceRegistrator();
    }

    @Bean(name = "softwareRepositoryWorkingDirectory")
    File softwareRepositoryWorkingDirectory(@Qualifier("beenWorkingDirectory") File beenWorkDir) {
        return mkdir(beenWorkDir, "software-repository");
    }

    @Bean(name = "softwareRepositoryPackagesDirectory")
    File softwareRepositoryPackagesDirectory(@Qualifier("softwareRepositoryWorkingDirectory") File swrWorkDir) {
        return mkdir(swrWorkDir, "packages");
    }

    @Bean(name = "softwareRepositoryTemporaryDirectory")
    File softwareRepositoryTemporaryDirectory(@Qualifier("softwareRepositoryWorkingDirectory") File swrWorkDir) {
        return mkdir(swrWorkDir, "temp");
    }

    @Bean
    ByteArrayPackageProvider byteArrayPackageProvider() {
        return  new ByteArrayPackageProvider();
    }

    @Bean
    SoftwareRepository softwareRepository(List<PackageProvider> packageProviders) {
        return new SoftwareRepository(packageProviders);
    }

    private final File mkdir(File parent, String child) {
        File dir = parent.toPath().resolve(child).toFile();
        dir.mkdir();
        return dir;
    }

}
