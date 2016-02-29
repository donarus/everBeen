package cz.cuni.mff.d3s.been.webapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("web-ui")
public class WebUIApplicationContext {

    @Bean
    public WebUI webUI() {
        return new WebUI();
    }
}
