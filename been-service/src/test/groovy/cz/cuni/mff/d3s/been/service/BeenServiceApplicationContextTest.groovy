package cz.cuni.mff.d3s.been.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [BeenServiceApplicationContext])
class BeenServiceApplicationContextTest extends Specification {

    @Autowired
    private ApplicationContext applicationContext

    def 'instantiated ACP gives us the real ApplicationContext'() {
        expect:
            ACP.getApplicationContext() == applicationContext
    }

}
