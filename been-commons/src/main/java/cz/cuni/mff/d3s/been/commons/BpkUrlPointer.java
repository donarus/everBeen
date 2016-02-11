package cz.cuni.mff.d3s.been.commons;

import java.io.Serializable;
import java.net.URL;

public final class BpkUrlPointer implements BpkPointer, Serializable{
    public URL url;

    public BpkUrlPointer(URL url) {
        this.url = url;
    }
}
