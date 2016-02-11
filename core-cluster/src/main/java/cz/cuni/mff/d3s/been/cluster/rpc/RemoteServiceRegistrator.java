package cz.cuni.mff.d3s.been.cluster.rpc;

public class RemoteServiceRegistrator {
    private final Class<? extends RemoteService> serviceClass;
    private final String serviceName;

    public RemoteServiceRegistrator(Class<? extends RemoteService> serviceClass, String serviceName) {
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
