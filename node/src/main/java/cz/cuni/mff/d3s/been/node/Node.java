package cz.cuni.mff.d3s.been.node;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MembershipEvent;
import cz.cuni.mff.d3s.been.commons.NodeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public final class Node {

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private NodeInfo nodeInfo;

    private static final Logger log = LoggerFactory.getLogger(Node.class);

    public void start() {
        hazelcastInstance.getCluster().addMembershipListener(new HCMemberRemovedListener() {
            @Override
            public void memberRemoved(MembershipEvent membershipEvent) {
                // node id is set as "hcINstance.cluster.localMember.uuid" / "membershipEvent.member.uuid"
                hazelcastInstance.getMap("BEEN_NODE_INFO_MAP").remove(membershipEvent.getMember().getUuid());
                hazelcastInstance.getReplicatedMap("NODE_ADDRESSES").remove(membershipEvent.getMember().getUuid());
            }
        });
        hazelcastInstance.getReplicatedMap("NODE_ADDRESSES").put(nodeInfo.getId(), hazelcastInstance.getCluster().getLocalMember().getAddress());
        hazelcastInstance.getMap("BEEN_NODE_INFO_MAP").put(nodeInfo.getId(), nodeInfo);
        log.info("Aloha!!! Been node started.");
    }

    public void stop() {
        log.info("Been node stopped. Good bye!!!");
    }

}
