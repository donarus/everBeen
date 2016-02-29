package cz.cuni.mff.d3s.been.service;

import java.util.UUID;

public class BeenUUIDGenerator {

    public String generate() {
        return UUID.randomUUID().toString();
    }

}
