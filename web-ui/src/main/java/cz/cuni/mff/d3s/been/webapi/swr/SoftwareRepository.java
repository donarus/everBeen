package cz.cuni.mff.d3s.been.webapi.swr;

import com.google.gson.Gson;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.query.SqlPredicate;
import cz.cuni.mff.d3s.been.commons.MapNames;
import cz.cuni.mff.d3s.been.commons.bpk.BpkId;
import cz.cuni.mff.d3s.been.hr.HostRuntimeAPI;
import cz.cuni.mff.d3s.been.service.ACP;
import cz.cuni.mff.d3s.been.swr.SWRAPI;
import cz.cuni.mff.d3s.been.swr.SWRException;
import org.springframework.context.ApplicationContext;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;
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

    private List<String> list() {
        return hazelcastInstance
                .getMultiMap(MapNames.SERVICES)
                .entrySet()
                .stream()
                .filter(e -> e.getValue().equals("SoftwareRepositoryAPI"))
                .map(e -> (String)e.getKey())
                .collect(Collectors.toList());
    }

}
