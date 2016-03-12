package cz.cuni.mff.d3s.been.node;

import cz.cuni.mff.d3s.been.commons.exceptions.NodeException;

interface Node {
    void start() throws NodeException;
    void stop() throws NodeException;
}
