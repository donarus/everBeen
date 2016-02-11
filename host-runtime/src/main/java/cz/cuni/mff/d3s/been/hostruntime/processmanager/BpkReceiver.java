package cz.cuni.mff.d3s.been.hostruntime.processmanager;

import cz.cuni.mff.d3s.been.commons.BpkPointer;
import cz.cuni.mff.d3s.been.commons.BpkUrlPointer;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class BpkReceiver {

    public void receive(BpkPointer bpkPointer, File dest) throws IOException, ZipException {
        if (bpkPointer instanceof BpkUrlPointer) {
            receive(((BpkUrlPointer) bpkPointer), dest);
        } else {
            throw new IllegalArgumentException("Unsupported type of bpk pointer: " + bpkPointer.getClass());
        }
    }

    private void receive(BpkUrlPointer pointer, File dest) throws IOException, ZipException {
        File tmpBpk = File.createTempFile(pointer.url.getFile(), "temp_bpk");
        try {
            FileUtils.copyURLToFile(pointer.url, tmpBpk);
            ZipFile zipFile = new ZipFile(tmpBpk);
            zipFile.extractAll(dest.getPath());
        } finally {
            if (tmpBpk.exists()) {
                tmpBpk.delete();
            }
        }
    }

}
