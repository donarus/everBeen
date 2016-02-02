package cz.cuni.mff.d3s.been.node;

import cz.cuni.mff.d3s.been.cluster.NodeType;

import java.util.Properties;

/**
 * Created by donarus on 2.2.16.
 */
final class StartupConfigurationHolder {

    private static Properties properties;

    private static NodeType nodeType;

    private StartupConfigurationHolder() {
        // prevents instantiation
    }

    static void setProperties(Properties props) {
        if (props == null) {
            throw new RuntimeException("You have to set existing properties object");
        } else if (properties != null) {
            throw new RuntimeException("Properties can be set only once");
        }
        properties = props;
    }

    static Properties getProperties() {
        if (properties == null) {
            throw new RuntimeException("Properties has not been initialized");
        }
        return properties;
    }

    public static void setNodeType(NodeType type) {
        if (type == null) {
            throw new RuntimeException("You have to set correct NodeType - null setted");
        } else if (nodeType != null) {
            throw new RuntimeException("Node type can be set only once");
        }
        nodeType = type;
    }

    public static NodeType getNodeType() {
        if (nodeType == null) {
            throw new RuntimeException("NodeType has not been initialized");
        }
        return nodeType;
    }

    static void validate() {
        getProperties();
        getNodeType();
    }

}
