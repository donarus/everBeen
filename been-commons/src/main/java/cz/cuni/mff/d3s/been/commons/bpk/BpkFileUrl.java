package cz.cuni.mff.d3s.been.commons.bpk;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class BpkFileUrl implements BpkFile {

    private final URL url;

    public BpkFileUrl(URL url) {
        this.url = url;
    }

    @Override
    public InputStream getStream() throws IOException {
        return url.openStream();
    }
}
