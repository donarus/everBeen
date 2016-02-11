package cz.cuni.mff.d3s.been.hostruntime.processmanager

import com.fasterxml.jackson.databind.JsonMappingException
import cz.cuni.mff.d3s.been.commons.JavaBpk
import groovy.json.JsonOutput
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

public class BpkLoaderTest extends Specification {

    @Rule
    TemporaryFolder tmpDir

    def FULL_VALID_JAVA_BPK = [
            "type"   : "java",
            "groupId": "cz.cuni.mff.d3s.been",
            "bpkId"  : "task-example-helloworld",
            "version": "1.0.0-SNAPSHOT",
            "jarFile": "task-example-helloworld-1.0.0-SNAPSHOT.jar"
    ]

    def LIBRARIES = [
            "library1.jar",
            "library2.jar",
            "library3.noJar"
    ]

    File unpackedBpk
    File configJson
    File libsDir

    def setup() {
        unpackedBpk = tmpDir.newFolder()

        configJson = new File(unpackedBpk, "bpk.json")
        configJson << JsonOutput.toJson(FULL_VALID_JAVA_BPK) // default
    }

    def createSampleLibraries() {
        libsDir = new File(unpackedBpk, "lib")
        libsDir.mkdir()
        LIBRARIES.each {
            new File(libsDir, it).createNewFile()
        }
    }

    def 'loading valid Java BPK'() {
        given:
            configJson.text = JsonOutput.toJson(FULL_VALID_JAVA_BPK)
            def loader = new BpkLoader()
        when:
            JavaBpk bpk = loader.load(unpackedBpk)
        then:
            bpk.bpkId == "task-example-helloworld"
            bpk.groupId == "cz.cuni.mff.d3s.been"
            bpk.version == "1.0.0-SNAPSHOT"
            bpk.jarFile == "task-example-helloworld-1.0.0-SNAPSHOT.jar"
    }

    def 'loaded BPJ without type is of type Java by default'() {
        given:
            def input = new HashMap<>(FULL_VALID_JAVA_BPK)
            input.remove("type")

            configJson.text = JsonOutput.toJson(input)
            def loader = new BpkLoader()
        when:
            JavaBpk bpk = loader.load(unpackedBpk)
        then:
            bpk.bpkId == "task-example-helloworld"
            bpk.groupId == "cz.cuni.mff.d3s.been"
            bpk.version == "1.0.0-SNAPSHOT"
            bpk.jarFile == "task-example-helloworld-1.0.0-SNAPSHOT.jar"
    }

    @Unroll
    def 'Java BPK without #missingProperty cannot be loaded'() {
        given:
            def input = new HashMap<>(FULL_VALID_JAVA_BPK)
            input.remove(missingProperty)

            configJson.text = JsonOutput.toJson(input)
            def loader = new BpkLoader()
        when:
            loader.load(unpackedBpk)
        then:
            def ex = thrown(JsonMappingException)
            ex.message.contains("Missing required creator property '$missingProperty'")
        where:
            missingProperty << ["groupId", "bpkId", "version", "jarFile"]
    }

    def 'only jar libraries are loaded for Java BPK'() {
        given:
            createSampleLibraries()
            def loader = new BpkLoader()
        when:
            JavaBpk bpk = loader.load(unpackedBpk)
        then:
            bpk.libraries == [
                    "library1.jar" : new File(libsDir, "library1.jar"),
                    "library2.jar" : new File(libsDir, "library2.jar")
            ]
    }

    def 'libraries are not loaded when there is no lib dir in Java BPK'() {
        given:
            def loader = new BpkLoader()
        when:
            JavaBpk bpk = loader.load(unpackedBpk)
        then:
            bpk.libraries == [:]
    }

}
