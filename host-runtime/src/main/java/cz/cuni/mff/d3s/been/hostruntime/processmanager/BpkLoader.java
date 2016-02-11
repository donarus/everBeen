package cz.cuni.mff.d3s.been.hostruntime.processmanager;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cuni.mff.d3s.been.commons.Bpk;
import cz.cuni.mff.d3s.been.commons.JavaBpk;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Function.identity;

public class BpkLoader {

    /**
     * JsonFactory follows "thread-safe after configuration" =
     *
     * @see <a href ="http://wiki.fasterxml.com/JacksonFAQThreadSafety">http://wiki.fasterxml.com/JacksonFAQThreadSafety</a>
     */
    private final JsonFactory factory;

    public BpkLoader() {
        factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_COMMENTS);
    }

    public Bpk load(File unpackedBpk) throws IOException {
        File configJson = unpackedBpk.toPath().resolve("bpk.json").toFile();
        Bpk bpk = new ObjectMapper(factory).readValue(configJson, Bpk.class);
        if (bpk instanceof JavaBpk) {
            bpk = loadJavaBpk((JavaBpk) bpk, unpackedBpk);
        } else {
            // FIXME
            // unsupported type
        }
        // FIXME some sort of validation
        return bpk;
    }

    private JavaBpk loadJavaBpk(JavaBpk bpk, File unpackedBpk) {
        File bpkJarBinary = unpackedBpk.toPath().resolve(bpk.getJarFileName()).toFile();
        if (!bpkJarBinary.exists()) {
            // fixme throw
        }
        bpk.setJarFile(bpkJarBinary);

        File libsDir = unpackedBpk.toPath().resolve("lib").toFile();
        if (libsDir.exists()) {
            Map<String, File> libraries = Stream.of(libsDir.listFiles((dir, name) -> name.endsWith(".jar")))
                    .collect(Collectors.toMap(File::getName, identity()));
            bpk.addLibraries(libraries);
        } else {
            // FIXME some logging that no libs has been detected
        }
        return bpk;
    }

}
