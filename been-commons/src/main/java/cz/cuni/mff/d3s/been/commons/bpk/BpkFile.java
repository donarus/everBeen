package cz.cuni.mff.d3s.been.commons.bpk;

import java.io.IOException;
import java.io.InputStream;

public interface BpkFile {
    InputStream getStream() throws IOException;
}
