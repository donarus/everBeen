package cz.cuni.mff.d3s.been.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Been {

    private static final Logger log = LoggerFactory.getLogger(Been.class);


    public void start() {
        log.info("Aloha!!! Been started.");
    }

    public void stop() {
        log.info("Been stopped. Good bye!!!");
    }

}
