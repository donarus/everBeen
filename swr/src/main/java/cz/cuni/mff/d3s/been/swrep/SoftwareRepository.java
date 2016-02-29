package cz.cuni.mff.d3s.been.swrep;

import cz.cuni.mff.d3s.been.commons.bpk.Bpk;
import cz.cuni.mff.d3s.been.commons.bpk.BpkFile;
import cz.cuni.mff.d3s.been.commons.bpk.BpkId;
import cz.cuni.mff.d3s.been.commons.utils.BpkUtil;
import cz.cuni.mff.d3s.been.swr.SWRException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SoftwareRepository {

    @Autowired
    @Qualifier("softwareRepositoryPackagesDirectory")
    private File packagesDirectory;

    @Autowired
    @Qualifier("softwareRepositoryTemporaryDirectory")
    private File tmpDirectory;

    private Map<String, PackageProvider> packageProviders = new HashMap<>();

    public SoftwareRepository(Collection<PackageProvider> repositories) {
        for (PackageProvider repository : repositories) {
            this.packageProviders.put(repository.getMethod(), repository);
        }
    }

    public BpkFile receivePackage(BpkId pkgId, String method) throws SWRException {
        // FIXME .. some validation of groupId, artifactId and versions
        Path pkgParentPath = constructPkgParentPath(pkgId, false);
        Path bpkPath = constructBpkPath(pkgParentPath, pkgId);
        File bpk = bpkPath.toFile();
        if (!bpk.exists()) {
            // FIXME some throw
            throw new SWRException("bpk does not exists");
        }

        PackageProvider provider = packageProviders.get(method);
        if (provider == null) {
            // FIXME better exception handling
            throw new SWRException("unknown software repository provider");
        }

        try (InputStream bpkStream = new FileInputStream(bpk)) {
            return provider.create(bpkStream);
        } catch (IOException e) {
            throw new SWRException("cannot read BPK", e);
        }
    }

    public void storePackage(BpkFile bpkFile) throws SWRException {

        // prepare temporary file where the pacakge will be teporarily materialized
        File pkgFile;
        try {
            pkgFile = File.createTempFile("pkg", ".bpk", tmpDirectory);
        } catch (IOException e) {
            // FIXME exception handling
            throw new SWRException(e);
        }

        // materialize the file locally
        try (InputStream is = bpkFile.getStream(); OutputStream os = new FileOutputStream(pkgFile)) {
            FileCopyUtils.copy(is, os);
        } catch (IOException e) {
            // FIXME exception handling
            throw new SWRException(e);
        }

        // read package id
        Bpk bpk;
        try {
            bpk = BpkUtil.loadBpk(pkgFile);
        } catch (Exception e) {
            // FIXME exception handling
            throw new SWRException(e);
        }

        BpkId bpkId = bpk.getBpkId();

        // FIXME .. some validation of groupId, artifactId and versions
        Path pkgParentPath = constructPkgParentPath(bpkId, true);
        Path bpkPath = constructBpkPath(pkgParentPath, bpkId);
        try {
            Files.move(pkgFile.toPath(), bpkPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // FIXME exception handling
            throw new SWRException(e);
        }

    }

    private Path constructBpkPath(Path pkgParentPath, BpkId pkgId) {
        return Paths.get(pkgParentPath.toString(), pkgId.getArtifactId() + "-" + pkgId.getVersion() + ".bpk");
    }

    private Path constructPkgParentPath(BpkId pkgId, boolean mkdirIfNotExists) {
        Path pkgParentPath = packagesDirectory.toPath();
        pkgParentPath = Paths.get(pkgParentPath.toString(), pkgId.getGroupId().split("\\."));
        pkgParentPath = Paths.get(pkgParentPath.toString(), pkgId.getArtifactId());
        if (mkdirIfNotExists) {
            pkgParentPath.toFile().mkdirs(); // FIXME possible errors
        }
        return pkgParentPath;
    }

    public List<BpkId> list() {
        Collection<File> bpks = FileUtils.listFiles(packagesDirectory, new String[]{"bpk"}, true);
        List<BpkId> ids = bpks
                .stream()
                .map(bpk -> {
            try { return BpkUtil.loadBpk(bpk).getBpkId(); } catch (Exception e) { e.printStackTrace(); /* FIXME exc. handling */ }
            return null; })
                .filter(p -> p != null)
                .collect(Collectors.toList());

        return ids;
    }
}
