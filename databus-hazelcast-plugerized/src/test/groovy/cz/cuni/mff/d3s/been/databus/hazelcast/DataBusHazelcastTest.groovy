package cz.cuni.mff.d3s.been.databus.hazelcast

import com.hazelcast.client.HazelcastClient
import com.hazelcast.core.HazelcastInstance
import spock.lang.Specification

/**
 * Created by donarus on 14.4.15.
 */
class DataBusHazelcastTest extends Specification {

    def 'hazelcast config uses correct classloader'() {
        given:
            def cl = Mock(ClassLoader)
            def databus = new DataBusHazelcast(pluginsClassLoader: cl)

        when:
            def config = databus.createHazelcastConfig()

        then:
            config.getClassLoader() == cl
    }

    def 'hazelcast config uses correct logging framework'() {
        given:
            def databus = new DataBusHazelcast()

        when:
            def config = databus.createHazelcastConfig()

        then:
            config.getProperty('hazelcast.logging.type') == 'slf4j'
    }

    def 'hazelcast starts on connect'() {
        given:
            def databus = new DataBusHazelcast()
            databus.connect()
            HazelcastInstance hi = databus.hazelcastInstance
            HazelcastInstance hc1 = HazelcastClient.newHazelcastClient()

        when:
            hi.getMap("test").put("x", "y")

        then:
            hc1.getMap("test").getAt("x") == "y"
    }

    def 'unique id is generated correctly as padded hex string of long value'() {
        given:
            def databus1 = new DataBusHazelcast()
            databus1.connect()

            def databus2 = new DataBusHazelcast()
            databus2.connect()

        when:
            def uuid1_g1 = databus1.generateClusterWideUUID("group1")
            def uuid2_g1 = databus2.generateClusterWideUUID("group1")
            def uuid3_g1 = databus1.generateClusterWideUUID("group1")

            def uuid1_g2 = databus2.generateClusterWideUUID("group2")
            def uuid2_g2 = databus1.generateClusterWideUUID("group2")
            def uuid3_g2 = databus2.generateClusterWideUUID("group2")

        then:
            [uuid1_g1, uuid2_g1, uuid3_g1].unique().size() == 3
            [uuid1_g2, uuid2_g2, uuid3_g2].unique().size() == 3
            [uuid1_g1, uuid2_g1, uuid3_g1].each {it.length() == 16}
    }

}
