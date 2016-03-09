package cz.cuni.mff.d3s.been.node;

import cz.cuni.mff.d3s.been.commons.exceptions.NodeException;
import cz.cuni.mff.d3s.been.commons.nodeinfo.NodeType;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static cz.cuni.mff.d3s.been.node.StatusCode.EX_OTHER;
import static cz.cuni.mff.d3s.been.node.StatusCode.EX_USAGE;

public class Bootstrap {

    private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);

    @Option(name = "-t", aliases = {"--node-type"}, usage = "Type of the node. DEFAULT is DATA")
    private NodeType nodeType = NodeType.DATA;

    @Option(name = "-s", aliases = {"--services"}, usage = "Services to be started. Empty by default.", handler = StringArrayOptionHandler.class)
    private String[] requiredServices = new String[0];

    @Option(name = "-cf", aliases = {"--config-file"}, usage = "Path or URL to BEEN config file.")
    private String configFile;

    @Option(name = "-h", aliases = {"--help"}, usage = "Prints help")
    private boolean printHelp = false;

    public static void main(String[] args) {
        new Bootstrap().bootstrap(args);
    }

    public void bootstrap(final String[] args) {
        parseCmdLineArguments(args);

        Properties properties = loadProperties();

        if (printHelp) {
            printUsage();
            EX_USAGE.sysExit();
        }

        ClassPathXmlApplicationContext context = startApplicationContext(properties);

        Node been = context.getBean(Node.class);
        try {
            been.start();
        } catch (NodeException e) {
            log.error("Node cannot be started.", e);
            EX_OTHER.sysExit();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> been.stop()));
    }

    private ClassPathXmlApplicationContext startApplicationContext(Properties properties) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("nodeType", nodeType);
        beanFactory.registerSingleton("properties", properties);
        GenericApplicationContext existingBeansContext = new GenericApplicationContext(beanFactory);
        existingBeansContext.refresh();

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setConfigLocation("base-been-context.xml");
        context.setParent(existingBeansContext);
        context.getEnvironment().setActiveProfiles(requiredServices);
        context.refresh();
        return context;
    }

    /**
     * Parses supplied command line arguments for this object.
     * <p/>
     * In case of error, an error message and usage is print to System.err, then
     * program quits.
     *
     * @param args Command line arguments
     */
    private void parseCmdLineArguments(final String[] args) {
        // Handle command-line arguments
        CmdLineParser parser = new CmdLineParser(this);

        try {
            // parse the arguments.
            parser.parseArgument(args);

        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("\nUsage:");
            parser.printUsage(System.err);

            System.exit(EX_USAGE.getCode());
        }
    }

    private void printUsage() {
        CmdLineParser parser = new CmdLineParser(this);
        parser.printUsage(System.out);
    }

    private Properties loadProperties() {

        if (configFile == null || configFile.isEmpty()) {
            log.info("No config file or url specified. Will start with default configuration.");
            return new Properties();
        }

        PropertyLoader loader = null;

        // try as a file
        try {
            Path path = Paths.get(configFile);
            if (Files.exists(path)) {
                loader = PropertyLoader.fromPath(path);
            }
        } catch (InvalidPathException e) {
            // quell
        }

        // try as an URL
        if (loader == null) {
            try {
                URL url = new URL(configFile);
                loader = PropertyLoader.fromUrl(url);
            } catch (MalformedURLException e) {
                // quell
            }
        }

        if (loader == null) {
            log.error("{} is not a file nor an URL. Aborting.", configFile);
            EX_USAGE.sysExit();
            throw new AssertionError(); // make the compiler happy
        }

        try {
            Properties properties = loader.load();
            log.info("Configuration loaded from {}", configFile);
            return properties;
        } catch (IOException e) {
            String msg = String.format("Cannot load properties from %s. Aborting.", configFile);
            log.error(msg, e);
            EX_USAGE.sysExit();
        }

        throw new AssertionError(); // will not get here, make the compiler happy
    }

}
