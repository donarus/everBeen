package cz.cuni.mff.d3s.been.node;

import cz.cuni.mff.d3s.been.service.rpc.RemoteServiceDefinition;

public class SampleRemoteServiceDefinition extends RemoteServiceDefinition {
    public SampleRemoteServiceDefinition() {
        super(SampleRemoteService.class, "sampleRemoteService");
    }
}
