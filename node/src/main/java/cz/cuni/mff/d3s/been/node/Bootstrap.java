package cz.cuni.mff.d3s.been.node;

import cz.cuni.mff.d3s.been.BeenServiceConfiguration;
import cz.cuni.mff.d3s.been.cluster.NodeType;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static cz.cuni.mff.d3s.been.core.StatusCode.EX_OK;
import static cz.cuni.mff.d3s.been.core.StatusCode.EX_USAGE;

/**
 * EverBEEN application entry point
 */
public class Bootstrap {

    // ------------------------------------------------------------------------
    // LOGGING
    // ------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(Runner.class);

    // ------------------------------------------------------------------------
    // COMMAND LINE ARGUMENTS
    // ------------------------------------------------------------------------

    @Option(name = "-t", aliases = { "--node-type" }, usage = "Type of the node. DEFAULT is DATA")
    private NodeType nodeType = NodeType.DATA;

    @Option(name = "-cf", aliases = { "--config-file" }, usage = "Path or URL to BEEN config file.")
    private String configFile;

    @Option(name = "-dc", aliases = { "--dump-config" }, usage = "Whether to print runtime configuration and exit")
    private boolean dumpConfig;

    @Option(name = "-r", aliases = { "--host-runtime" }, usage = "Whether to run Host runtime on this node")
    private boolean runHostRuntime = false;

    @Option(name = "-sw", aliases = { "--software-repository" }, usage = "Whether to run Software Repository on this node.")
    private boolean runSWRepository = false;

    @Option(name = "-rr", aliases = { "--repository" }, usage = "Whether to run Repository on this node. Requires a running matching persistence layer.")
    private boolean runRepository = false;

    @Option(name = "-h", aliases = { "--help" }, usage = "Prints help")
    private boolean printHelp = false;

    public static void main(String[] args) {
        String[] testArgs = new String[] {"--host-runtime"};
        new Bootstrap().bootstrap(testArgs);
    }

    public void bootstrap(final String[] args) {
        parseCmdLineArguments(args);

        Properties properties = loadProperties();

        if (dumpConfig) {
            printBeenConfiguration(properties);
            EX_OK.sysExit();
        }

        if (printHelp) {
            printUsage();
            EX_USAGE.sysExit();
        }

        List<String> profiles = new ArrayList<>();
        if(runHostRuntime) {
            profiles.add("host-runtime");
        }
        if(runSWRepository) {
            profiles.add("software-repository");
        }
        if(runRepository) {
            profiles.add("results-repository");
        }

        StartupConfigurationHolder.setProperties(properties);
        StartupConfigurationHolder.setNodeType(nodeType);

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setConfigLocation("*context.xml");
        context.getEnvironment().setActiveProfiles(profiles.toArray(new String[profiles.size()]));
        context.refresh();

        context.getBean(Been.class).start();

    }

    /**
     * Parses supplied command line arguments for this object.
     * <p/>
     * In case of error, an error message and usage is print to System.err, then
     * program quits.
     *
     * @param args
     *          Command line arguments
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

    /**
     * Prints runtime configuration of BEEN.
     *
     * @param properties
     *          user specified properties
     */
    private void printBeenConfiguration(final Properties properties) {

        final String DEFAULT_VALUE_PREFIX = "DEFAULT_"; // by convention

        final ServiceLoader<BeenServiceConfiguration> configs = ServiceLoader.load(BeenServiceConfiguration.class);

        for (BeenServiceConfiguration config : configs) {
            Class<?> klazz = config.getClass();

            System.out.printf("#%n# %s%n#%n%n", klazz.getSimpleName());

            Map<String, Object> defaultValues = new HashMap<>();
            Map<String, String> propertyNames = new HashMap<>();

            for (Field field : klazz.getDeclaredFields()) {
                final String name = field.getName();

                try {
                    if (name.startsWith(DEFAULT_VALUE_PREFIX)) {
                        String propertyName = name.substring(DEFAULT_VALUE_PREFIX.length());
                        defaultValues.put(propertyName, field.get(config));
                    } else {
                        propertyNames.put(name, field.get(config).toString());
                    }

                } catch (IllegalAccessException e) {
                    String msg = String.format("Cannot get value for '%s'", name);
                    log.error(msg, e);
                }
            }

            for (Map.Entry<String, String> entry : propertyNames.entrySet()) {
                String name = entry.getValue();
                Object value = defaultValues.get(entry.getKey());

                if (properties.containsKey(name)) {
                    System.out.printf("%s=%s%n", entry.getValue(), value);
                } else {
                    System.out.printf("# %s=%s%n", entry.getValue(), value);
                }

            }

            System.out.printf("%n%n%n");

        }
    }

}
