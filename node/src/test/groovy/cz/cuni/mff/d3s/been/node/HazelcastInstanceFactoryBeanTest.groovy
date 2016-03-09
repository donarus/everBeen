package cz.cuni.mff.d3s.been.node

import cz.cuni.mff.d3s.been.commons.exceptions.NodeException
import cz.cuni.mff.d3s.been.commons.nodeinfo.NodeType
import cz.cuni.mff.d3s.been.service.rpc.ACP
import cz.cuni.mff.d3s.been.service.rpc.RemoteServiceDefinition
import spock.lang.Specification
import spock.lang.Unroll

class HazelcastInstanceFactoryBeanTest extends Specification {

    def 'creating HazelcastInstance DATA or LITE'() {
        given:
            def factory = new HazelcastInstanceFactoryBean(
                    nodeType: nodeType
            )

        when:
            def hazelcastInstance = factory.createInstance()

        then:
            hazelcastInstance.getCluster().getLocalMember().isLiteMember() == isLite

        cleanup:
            hazelcastInstance.shutdown()

        where:
            nodeType      | isLite
            NodeType.DATA | false
            NodeType.LITE | true
    }

    def 'creating HazelcastInstance with some services on DATA node'() {
        given:
            def factory = new HazelcastInstanceFactoryBean(
                    serviceDefinitions: [new SampleRemoteServiceDefinition()],
                    nodeType: NodeType.DATA
            )

            ACP.context = Mock(org.springframework.context.ApplicationContext)

        when:
            def hazelcastInstance = factory.createInstance()

        then:
            hazelcastInstance.getDistributedObject("sampleRemoteService", "sampleService").getClass() == SampleRemoteServiceProxy

        cleanup:
            hazelcastInstance.shutdown()
    }

    def 'creating HazelcastInstance with some services on LITE node'() {
        given:
            def factoryData = new HazelcastInstanceFactoryBean(
                    nodeType: NodeType.DATA
            )

            def factoryLite = new HazelcastInstanceFactoryBean(
                    serviceDefinitions: [new SampleRemoteServiceDefinition()],
                    nodeType: NodeType.LITE
            )

            ACP.context = Mock(org.springframework.context.ApplicationContext)

        when:
            def hazelcastInstanceData = factoryData.createInstance()
            def hazelcastInstanceLite = factoryLite.createInstance()

        then:
            hazelcastInstanceLite.getDistributedObject("sampleRemoteService", "sampleService").getClass() == SampleRemoteServiceProxy

        cleanup:
            hazelcastInstanceData.shutdown()
            hazelcastInstanceLite.shutdown()
    }

    @Unroll
    def 'detect correctly if should run as #nodeType node'() {
        given:
            def factory = new HazelcastInstanceFactoryBean(
                    nodeType: nodeType
            )

        when:
            def runAsLiteNode = factory.runAsLiteNode()

        then:
            runAsLiteNode == isLite

        where:
            nodeType      | isLite
            NodeType.DATA | false
            NodeType.LITE | true
    }

    def 'detection of lite regime fails when NodeType is not provided'() {
        given:
            def factory = new HazelcastInstanceFactoryBean(
                    nodeType: null
            )

        when:
            factory.runAsLiteNode()

        then:
            def ex = thrown(NodeException)
            ex.message == "Unknown node type ['null']"
    }

    @Unroll
    def 'constructing service config when no service definition is defined (service definitions: #serviceDefinitions)'() {
        given:
            def factory = new HazelcastInstanceFactoryBean(
                    serviceDefinitions: serviceDefinitions
            )

        when:
            def config = factory.createServiceConfig()

        then:
            config.getServiceConfigs().empty

        where:
            serviceDefinitions << [null, []]
    }

    def 'constructing service config for multiple services'() {
        given:
            def service1 = new RemoteServiceDefinition(String, "service1")
            def service2 = new RemoteServiceDefinition(Long, "service2")
            def factory = new HazelcastInstanceFactoryBean(
                    serviceDefinitions: [service1, service2]
            )

        when:
            def config = factory.createServiceConfig()

        then:
            config.getServiceConfigs().size() == 2
            config.getServiceConfig("service1") != null
            config.getServiceConfig("service2") != null
    }

}
