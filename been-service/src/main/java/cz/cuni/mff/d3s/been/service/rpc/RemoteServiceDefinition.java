package cz.cuni.mff.d3s.been.service.rpc;

public class RemoteServiceDefinition {
    private final Class<? extends RemoteService> serviceClass;
    private final String serviceName;

    public RemoteServiceDefinition(Class<? extends RemoteService> serviceClass, String serviceName) {
        this.serviceClass = serviceClass;
        this.serviceName = serviceName;
    }

    public final Class<? extends RemoteService> getServiceClass() {
        return serviceClass;
    }

    public final String getServiceName() {
        return serviceName;
    }

}
