package cz.cuni.mff.d3s.been.node;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Utility for loading properties from different resources.
 */
public final class PropertyLoader {

    /**
     * Loads properties from given url
     *
     * @param url
     * @throws IOException when the resource cannot be loaded
     */
    public static Properties load(URL url) throws IOException {
        return load(url.openStream());
    }

    /**
     * Loads properties from given path
     *
     * @param path
     * @throws IOException when the resource cannot be loaded
     */
    public static Properties load(Path path) throws IOException {
        return load(new FileInputStream(path.toFile()));
    }

    private static Properties load(InputStream stream) throws IOException {
        Properties properties = new Properties();

        try (InputStream in = stream) {
            properties.load(in);
        }

        return properties;
    }

}
