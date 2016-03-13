package cz.cuni.mff.d3s.been.node

import com.hazelcast.core.HazelcastInstance
import cz.cuni.mff.d3s.been.commons.nodeinfo.NodeInfo
import cz.cuni.mff.d3s.been.commons.nodeinfo.NodeType
import cz.cuni.mff.d3s.been.service.BeenUUIDGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [DataNodeTestApplicationContext, NodeApplicationContext])
@DirtiesContext
class NodeApplicationContextDataNodeTest extends Specification {

    @Autowired
    String beenNodeId

    @Autowired
    File beenWorkingDirectory

    @Autowired
    HazelcastInstance hazelcastInstance

    @Autowired
    NodeInfo nodeInfo

    @Autowired
    BeenUUIDGenerator beenUUIDGenerator

    @Autowired
    Node node

    def 'beenNodeId is at 36 chars long'() {
        expect:
            beenNodeId.length() == 36
    }

    def 'global beenUUIDGenerator is instantiated'() {
        expect:
            beenUUIDGenerator.generate() instanceof String
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
