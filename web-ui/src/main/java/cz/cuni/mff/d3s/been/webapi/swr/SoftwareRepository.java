package cz.cuni.mff.d3s.been.webapi.swr;

import com.google.gson.Gson;
import com.hazelcast.core.HazelcastInstance;
import cz.cuni.mff.d3s.been.commons.bpk.BpkId;
import cz.cuni.mff.d3s.been.hr.HostRuntimeAPI;
import cz.cuni.mff.d3s.been.service.rpc.ACP;
import cz.cuni.mff.d3s.been.swr.SWRAPI;
import cz.cuni.mff.d3s.been.swr.SWRException;
import org.springframework.context.ApplicationContext;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SoftwareRepository implements Route {

    private final Gson gson = new Gson();

    private final HazelcastInstance hazelcastInstance;

    private Request request;

    private Response response;

    public SoftwareRepository() {
        ApplicationContext applicationContext = ACP.getApplicationContext();
        this.hazelcastInstance = applicationContext.getBean(HazelcastInstance.class);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        this.request = request;
        this.response = response;
        return handle();
    }

    private Object handle() {
        String method = request.requestMethod();
        Object ret = null;
        switch (method) {
            case "GET":
                ret = handleGet();
                break;
            case "POST":
            case "PUT":
            case "DELETE":
            default:
                throw new RuntimeException("FIXME: Unsupported method ['" + method + "']");
        }
        return gson.toJson(ret);
    }

    private Object handleGet() {
//        if(request.queryParams("id") == null)
        return list();
//        else
//            return get(request.queryParams("id"));
    }

    private List<BpkId> list() {
        // FIXME maybe dont list all distributed objects but store somewhere some information about where SWRep runs
        ((HostRuntimeAPI)(hazelcastInstance.getDistributedObject("HostRuntimeAPI", "8ed0a00b-92af-4ac1-9f8b-0c416ce14e1b"))).getId();
        List<SWRAPI> repositories = hazelcastInstance
                .getDistributedObjects()
                .stream()
                .filter(d -> d.getServiceName().equals("SoftwareRepositoryAPI"))
                .map(d -> (SWRAPI) d)
                .collect(Collectors.toList());

         List<BpkId> bpkIds = repositories.stream().map(r -> {
             try {
                 return r.list();
             } catch (SWRException e) {
                 e.printStackTrace();
             }
             return null;
         }).filter(p -> p != null).flatMap(p -> p.stream()).collect(Collectors.toList());

        return bpkIds;
    }

}
