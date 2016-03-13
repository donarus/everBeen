package cz.cuni.mff.d3s.been.node

import spock.lang.Specification

class PropertiesLoaderTest extends Specification {

    def 'loading properties from URL'() {
        given:
            def url = getClass().getResource("test-properties.properties")

        when:
            def properties = PropertiesLoader.load(url)

        then:
            properties.getProperty("test.property.key") == "value"
    }

    def 'loading properties from file'() {
        given:
            def path = new File(getClass().getResource("test-properties.properties").toURI()).toPath()

        when:
            def properties = PropertiesLoader.load(path)

        then:
            properties.getProperty("test.property.key") == "value"
    }

    def 'internally closes input stream when load is successful'() {
        given:
            def inputStream = Mock(InputStream)

        when:
            PropertiesLoader.load(inputStream)

        then:
            1 * inputStream.close()
    }

    def 'internally closes input stream when invalid property used'() {
        given:
            def inputStream = Mock(InputStream)

        when:
            PropertiesLoader.load(inputStream)

        then:
            def ex = thrown(IOException)
            ex.message == "mock exception"
            1 * inputStream.read(_) >> { throw new IOException("mock exception") }
            1 * inputStream.close()
    }

}
