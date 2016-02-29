package cz.cuni.mff.d3s.been.webapi.nodes;

import com.google.gson.Gson;
import com.hazelcast.core.HazelcastInstance;
import cz.cuni.mff.d3s.been.commons.NodeInfo;
import cz.cuni.mff.d3s.been.service.rpc.ACP;
import org.springframework.context.ApplicationContext;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Nodes implements Route {

    private final Gson gson = new Gson();

    private final HazelcastInstance hazelcastInstance;

    private Request request;

    private Response response;

    public Nodes() {
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
        if(request.queryParams("id") == null)
            return list();
        else
            return get(request.queryParams("id"));
    }

    private List<NodeInfo> list() {
        return new ArrayList<>(hazelcastInstance.<String, NodeInfo>getMap("BEEN_NODE_INFO_MAP").values());
    }

    private NodeInfo get(String id) {
        return hazelcastInstance.<String, NodeInfo>getMap("BEEN_NODE_INFO_MAP").get(id);
    }
}
