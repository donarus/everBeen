package cz.cuni.mff.d3s.been.node

import com.hazelcast.core.HazelcastInstance
import cz.cuni.mff.d3s.been.commons.nodeinfo.NodeInfo
import cz.cuni.mff.d3s.been.commons.nodeinfo.NodeType
import cz.cuni.mff.d3s.been.service.BeenUUIDGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [DataNodeTestApplicationContext, ApplicationContext])
@DirtiesContext
class ApplicationContextDataNodeTest extends Specification {

    @Autowired
    String beenNodeId

    @Autowired
    BeenUUIDGenerator beenUUIDGenerator

    @Autowired
    File beenWorkingDirectory

    @Autowired
    HazelcastInstance hazelcastInstance

    @Autowired
    NodeInfo nodeInfo

    @Autowired
    Node node

    def 'beenNodeId is at 36 chars long'() {
        expect:
            beenNodeId.length() == 36
    }

    def 'global beenUUIDGenerator returns unique strings'() {
        when:
            def uuids = (1..1000).collect { beenUUIDGenerator.generate() } // I know, but at least some test :)

        then:
            uuids.unique() == uuids
    }

    def 'beenWorkingDiretory has been created'() {
        expect:
            beenWorkingDirectory.exists()
    }

    def 'hazelcastInstance is of type DATA'() {
        expect:
            hazelcastInstance.cluster.getLocalMember().isLiteMember() == false
    }

    def 'nodeInfo is filled by at least some values'() {
        expect:
            nodeInfo.nodeType == NodeType.DATA
            nodeInfo.id == beenNodeId
            nodeInfo.operatingSystem != null
            nodeInfo.java != null
    }

    @DirtiesContext
    def 'node can be started and stopped'() {
        when:
            node.start()
            node.stop()

        then:
            noExceptionThrown()
    }

}
