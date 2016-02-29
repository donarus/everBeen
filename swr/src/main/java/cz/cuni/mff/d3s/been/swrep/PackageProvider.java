package cz.cuni.mff.d3s.been.swrep;

import cz.cuni.mff.d3s.been.commons.bpk.BpkFile;
import cz.cuni.mff.d3s.been.commons.bpk.BpkId;

import java.io.IOException;
import java.io.InputStream;

public interface PackageProvider {
    String getMethod();

    BpkFile create(InputStream packageStream) throws IOException;
}
