package cz.cuni.mff.d3s.been.service.rpc

import cz.cuni.mff.d3s.been.service.ACP
import org.springframework.context.ApplicationContext
import spock.lang.Specification

class ACPTest extends Specification {

    def 'ACP server application context in static way 2'() {
        setup:
            def oldAc = ACP.getApplicationContext()
            ACP.context = null
            def ac = Mock(ApplicationContext)

        expect:
            new ACP().setApplicationContext(ac)
            ACP.getApplicationContext() == ac

        cleanup:
            ACP.context = oldAc
    }

}
