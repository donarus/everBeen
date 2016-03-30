package cz.cuni.mff.d3s.been.node;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MembershipEvent;
import cz.cuni.mff.d3s.been.commons.MapNames;
import cz.cuni.mff.d3s.been.commons.exceptions.NodeException;
import cz.cuni.mff.d3s.been.commons.nodeinfo.NodeInfo;
import cz.cuni.mff.d3s.been.commons.nodeinfo.NodeType;
import cz.cuni.mff.d3s.been.service.rpc.RemoteServiceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

final class NodeImpl implements Node {

    private static final Logger log = LoggerFactory.getLogger(NodeImpl.class);

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private NodeInfo nodeInfo;

    @Autowired
    private NodeType nodeType;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired(required = false)
    private List<RemoteServiceDefinition> serviceDefinitions;

    public void start() throws NodeException {
        if (nodeType == NodeType.LITE
                && hazelcastInstance.getCluster().getMembers().stream().noneMatch(m -> !m.isLiteMember())) {
            throw new NodeException("Cannot start LITE node as there is no no DATA node present in the cluster.");
        }

        hazelcastInstance.getCluster().addMembershipListener(new HCMemberRemovedListener() {
            @Override
            public void memberRemoved(MembershipEvent membershipEvent) {
                // node id is set as "hcInstance.cluster.localMember.uuid" / "membershipEvent.member.uuid"
                hazelcastInstance.getMap(MapNames.NODE_INFO).remove(membershipEvent.getMember().getUuid());
                hazelcastInstance.getMap(MapNames.NODE_ADDRESSES).remove(membershipEvent.getMember().getUuid());
                hazelcastInstance.getMultiMap(MapNames.SERVICES).remove(membershipEvent.getMember().getUuid());
            }
        });
        hazelcastInstance.getMap(MapNames.NODE_ADDRESSES).put(nodeInfo.getId(), hazelcastInstance.getCluster().getLocalMember().getAddress());
        hazelcastInstance.getMap(MapNames.NODE_INFO).put(nodeInfo.getId(), nodeInfo);
        thisNodeServiceNames().forEach(s -> hazelcastInstance.getMultiMap(MapNames.SERVICES).put(nodeInfo.getId(), s));
        log.info("Aloha!!! Been node started.");
    }

    public void stop() {
        hazelcastInstance.getMap(MapNames.NODE_INFO).remove(nodeInfo.getId());
        hazelcastInstance.getMap(MapNames.NODE_ADDRESSES).remove(nodeInfo.getId());
        hazelcastInstance.getMultiMap(MapNames.SERVICES).remove(nodeInfo.getId());
        log.info("Been node stopped. Good bye!!!");
    }

    private List<String> thisNodeServiceNames() {
        if(serviceDefinitions != null && !serviceDefinitions.isEmpty()) {
            return serviceDefinitions.stream().map(s -> s.getServiceName()).sorted().collect(toList());
        } else {
            return new ArrayList<>();
        }
    }

}
