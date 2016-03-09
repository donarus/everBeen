package cz.cuni.mff.d3s.been.node;

import com.hazelcast.core.InitialMembershipEvent;
import com.hazelcast.core.InitialMembershipListener;
import com.hazelcast.core.MemberAttributeEvent;
import com.hazelcast.core.MembershipEvent;

abstract class HCMemberRemovedListener implements InitialMembershipListener {

    @Override
    public abstract void memberRemoved(MembershipEvent membershipEvent);

    @Override
    public final void init(InitialMembershipEvent event) {
    }

    @Override
    public final void memberAdded(MembershipEvent membershipEvent) {
    }

    @Override
    public final void memberAttributeChanged(MemberAttributeEvent memberAttributeEvent) {
    }

}
