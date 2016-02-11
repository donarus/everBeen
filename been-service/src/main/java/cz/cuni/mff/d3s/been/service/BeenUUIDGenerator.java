package cz.cuni.mff.d3s.been.service;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class BeenUUIDGenerator {

    private static final long PRIME_1 = 32416190071l;
    private static final long PRIME_2 = 179425579l;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    public String generate() {
        return String.format("%016X", PRIME_1 * (PRIME_2 + hazelcastInstance.getIdGenerator("cz.cuni.mff.d3s.been.node.UUID").newId()));
    }

}
