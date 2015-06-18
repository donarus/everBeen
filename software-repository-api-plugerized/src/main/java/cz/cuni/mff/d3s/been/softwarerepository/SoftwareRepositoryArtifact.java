package cz.cuni.mff.d3s.been.softwarerepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by donarus on 22.4.15.
 */
public class SoftwareRepositoryArtifact  {

    private SoftwareRepositoryArtifactDescriptor descriptor;

    private URL url;

    public SoftwareRepositoryArtifactDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(SoftwareRepositoryArtifactDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public InputStream openStream() throws IOException {
        return url.openStream();
    }

}
