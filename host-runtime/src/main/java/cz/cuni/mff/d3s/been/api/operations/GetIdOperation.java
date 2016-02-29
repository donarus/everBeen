package cz.cuni.mff.d3s.been.api.operations;

import com.hazelcast.spi.AbstractOperation;
import cz.cuni.mff.d3s.been.service.rpc.ACP;

public class GetIdOperation extends AbstractOperation {
    private String response = null;

    @Override
    public void run() throws Exception {
        response = (String) ACP.getApplicationContext().getBean("hostRuntimeId");
    }

    @Override
    public Object getResponse() {
        return response;
    }
}
