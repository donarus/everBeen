package cz.cuni.mff.d3s.been.commons;

import cz.cuni.mff.d3s.been.commons.nodeinfo.NodeInfo;

/**
 * Names of widely used maps in Been Hazelcast cluster.
 */
public final class MapNames {

    /**
     * Map with this name stores addresses of running Hazelcast instances.
     *
     * key ({@link java.lang.String}) is UUID of Hazelcast node
     * value ({@link com.hazelcast.nio.Address}) is address of particular Hazelcast instance
     */
    public static final String NODE_ADDRESSES = "BEEN:NODE_ADDRESSES";

    /**
     * Map with this name stores information about nodes.
     *
     * key ({@link java.lang.String}) is UUID of Hazelcast node
     * value ({@link NodeInfo}) is node info of particular node
     */
    public static final String NODE_INFO = "BEEN:NODE_INFO";

}
