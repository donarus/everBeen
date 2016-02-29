package cz.cuni.mff.d3s.been.swrep.api;

import com.hazelcast.spi.NodeEngine;
import cz.cuni.mff.d3s.been.commons.bpk.Bpk;
import cz.cuni.mff.d3s.been.commons.bpk.BpkFile;
import cz.cuni.mff.d3s.been.commons.bpk.BpkId;
import cz.cuni.mff.d3s.been.service.rpc.RemoteService;
import cz.cuni.mff.d3s.been.service.rpc.RemoteServiceProxy;
import cz.cuni.mff.d3s.been.swr.SWRAPI;
import cz.cuni.mff.d3s.been.swr.SWRException;
import cz.cuni.mff.d3s.been.swrep.SoftwareRepository;

import java.util.List;

public class SWRAPIServiceProxy extends RemoteServiceProxy implements SWRAPI {

    private String softwareRepositoryId;

    private SoftwareRepository softwareRepository;

    public SWRAPIServiceProxy(String name, NodeEngine nodeEngine, RemoteService remoteService) {
        super(name, nodeEngine, remoteService);
        this.softwareRepositoryId = getBean("softwareRepositoryId");
        this.softwareRepository = getBean("softwareRepository");
    }

    @Override
    public String getId() {
        return this.softwareRepositoryId;
    }

    @Override
    public BpkFile receivePackage(BpkId pkgId, String method) throws SWRException {
        return softwareRepository.receivePackage(pkgId, method);
    }

    @Override
    public void storePackage(BpkFile bpkFile) throws SWRException {
        softwareRepository.storePackage(bpkFile);
    }

    @Override
    public List<BpkId> list() throws SWRException {
        return softwareRepository.list();
    }

}