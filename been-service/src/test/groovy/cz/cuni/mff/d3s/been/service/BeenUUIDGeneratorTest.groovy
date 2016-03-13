package cz.cuni.mff.d3s.been.service

import spock.lang.Specification

class BeenUUIDGeneratorTest extends Specification {

    def 'test generated UUID are unique'() {
        given:
            def generator = new BeenUUIDGenerator()

        when:
            def uuids = (1..1000).collect { generator.generate() } // I know, but at least some test :)

        then:
            uuids.unique() == uuids
    }

}
