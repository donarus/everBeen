package cz.cuni.mff.d3s.been.node

import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import cz.cuni.mff.d3s.been.commons.exceptions.NodeException
import cz.cuni.mff.d3s.been.commons.nodeinfo.NodeInfo
import cz.cuni.mff.d3s.been.commons.nodeinfo.NodeType
import cz.cuni.mff.d3s.been.service.BeenUUIDGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [LiteNodeTestApplicationContext, NodeApplicationContext])
@DirtiesContext
class NodeApplicationContextLiteNodeTest extends Specification {

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

    def 'beenNodeId is 36 chars long'() {
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
            hazelcastInstance.cluster.getLocalMember().isLiteMember() == true
    }

    def 'nodeInfo is filled by at least some values'() {
        expect:
            nodeInfo.nodeType == NodeType.LITE
            nodeInfo.id == beenNodeId
            nodeInfo.operatingSystem != null
            nodeInfo.java != null
    }

    @DirtiesContext
    def 'lite node cannot be started without any already running data node in the cluster'() {
        when:
            node.start()

        then:
            def ex = thrown(NodeException)
            ex.message == "Cannot start LITE node as there is no no DATA node present in the cluster."
    }

    @DirtiesContext
    def 'lite node can be started when there is at least single data node in the cluster'() {
        given:
            def dataNode = Hazelcast.newHazelcastInstance()

        when:
            node.start()
            node.stop()

        then:
            noExceptionThrown()

        cleanup:
            dataNode.shutdown()
    }

}
