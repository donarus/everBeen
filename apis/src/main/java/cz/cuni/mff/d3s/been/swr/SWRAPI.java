package cz.cuni.mff.d3s.been.swr;

import cz.cuni.mff.d3s.been.commons.bpk.BpkFile;
import cz.cuni.mff.d3s.been.commons.bpk.BpkId;

import java.util.List;

public interface SWRAPI {
    String getId();

    BpkFile receivePackage(BpkId pkgId, String method) throws SWRException;

    void storePackage(BpkFile bpkFile) throws SWRException;

    List<BpkId> list() throws SWRException;
}
