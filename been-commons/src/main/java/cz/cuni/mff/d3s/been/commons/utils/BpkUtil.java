package cz.cuni.mff.d3s.been.commons.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cuni.mff.d3s.been.commons.bpk.Bpk;
import cz.cuni.mff.d3s.been.commons.bpk.BpkFile;
import cz.cuni.mff.d3s.been.commons.bpk.JavaBpk;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Function.identity;

public class BpkUtil {

    /**
     * JsonFactory follows "thread-safe after configuration" =
     *
     * @see <a href ="http://wiki.fasterxml.com/JacksonFAQThreadSafety">http://wiki.fasterxml.com/JacksonFAQThreadSafety</a>
     */
    private final static JsonFactory factory = new JsonFactory();

    static {
        factory.enable(JsonParser.Feature.ALLOW_COMMENTS);
    }

    public static Bpk loadBpk(File f) throws Exception {
        ZipFile zf;
        try {
            zf = new ZipFile(f);
        } catch (ZipException e) {
            //FIXME better ex handl
            throw e;
        }

        FileHeader fh = new FileHeader();
        fh.setFileName("bpk.json");
        try (ZipInputStream is = zf.getInputStream(fh)) {
            return new ObjectMapper(factory).readValue(is, Bpk.class);
        } catch (JsonParseException e) {
            //FIXME better ex handl
            throw e;
        } catch (JsonMappingException e) {
            //FIXME better ex handl
            throw e;
        } catch (IOException e) {
            //FIXME better ex handl
            throw e;
        } catch (ZipException e) {
            //FIXME better ex handl
            throw e;
        }
    }

    public static Bpk loadUnpackedBpk(File unpackedBpk) throws IOException {
        File bpkJson = new File(unpackedBpk, "bpk.json");
        Bpk bpk;
        try {
            bpk = new ObjectMapper(factory).readValue(bpkJson, Bpk.class);
        } catch (IOException e) {
            //FIXME better ex handl
            throw e;
        }

        if (bpk instanceof JavaBpk) {
            bpk = loadJavaBpk((JavaBpk) bpk, unpackedBpk);
        } else {
            // FIXME
            // unsupported type
        }
        // FIXME some sort of validation
        return bpk;
    }

    public static void extract(BpkFile bpkFile, File dest) throws IOException, ZipException {
        File tmpBpk = File.createTempFile("tmp", "bpk");

        try (InputStream is = bpkFile.getStream()) {
            FileUtils.copyInputStreamToFile(is, tmpBpk);
            ZipFile zipFile = new ZipFile(tmpBpk);
            zipFile.extractAll(dest.getPath());
            zipFile.extractAll(dest.getPath());
        } finally {
            tmpBpk.delete();
        }
    }

    private static JavaBpk loadJavaBpk(JavaBpk bpk, File unpackedBpk) throws FileNotFoundException {
        File bpkJarBinary = unpackedBpk.toPath().resolve(bpk.getJarFileName()).toFile();
        if (!bpkJarBinary.exists()) {
            // FIXME exc. handling
            throw new FileNotFoundException(bpkJarBinary.getAbsolutePath() + " does not exist.");
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
