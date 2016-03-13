package cz.cuni.mff.d3s.been.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BeenServiceApplicationContext {

    @Bean
    public ACP acp() {
        return new ACP();
    }

}
