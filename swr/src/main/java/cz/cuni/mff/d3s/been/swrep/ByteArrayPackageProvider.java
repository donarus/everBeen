package cz.cuni.mff.d3s.been.swrep;

import cz.cuni.mff.d3s.been.commons.bpk.Bpk;
import cz.cuni.mff.d3s.been.commons.bpk.BpkFile;
import cz.cuni.mff.d3s.been.commons.bpk.BpkFileByteArray;
import cz.cuni.mff.d3s.been.commons.bpk.BpkId;
import cz.cuni.mff.d3s.been.commons.utils.BpkUtil;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

public class ByteArrayPackageProvider implements PackageProvider {

    private static final String METHOD = "BYTE_ARRAY";

    @Override
    public String getMethod() {
        return METHOD;
    }

    @Override
    public BpkFile create(InputStream packageStream) throws IOException {
        return new BpkFileByteArray(StreamUtils.copyToByteArray(packageStream));
    }
}
