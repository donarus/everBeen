package cz.cuni.mff.d3s.been.webapi;

import cz.cuni.mff.d3s.been.webapi.nodes.Nodes;
import cz.cuni.mff.d3s.been.webapi.swr.SoftwareRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

public class WebUI {

    private static final Logger log = LoggerFactory.getLogger(WebUI.class);

    private static final int DEFAULT_PORT = 8080;

    public WebUI() {
        try (ServerSocket ss = new ServerSocket(DEFAULT_PORT)) {
            Spark.port(DEFAULT_PORT);
            Spark.staticFileLocation("public"); //index.html is served at localhost:4567 (default port)
            Spark.get("/nodes", new Nodes());
            Spark.get("/software-repository", new SoftwareRepository());
            Spark.init();
        } catch (BindException e) {
            // FIXME better exception handling
            log.error("WebUI cannot start on port " + DEFAULT_PORT + ". This port is already occupied by some other application. WebUI will not be started on this node");
        } catch (IOException e) {
            log.error("WebUI cannot be started on port " + DEFAULT_PORT + ". WebUI will not be started on this node", e);
        }
    }


}
