package cz.cuni.mff.d3s.been.commons.bpk;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BpkFileByteArray implements BpkFile {
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public BpkFileByteArray(byte[] data) {
        this.data = data;
    }

    @Override
    public InputStream getStream() throws IOException {
        return new ByteArrayInputStream(data);
    }

}
