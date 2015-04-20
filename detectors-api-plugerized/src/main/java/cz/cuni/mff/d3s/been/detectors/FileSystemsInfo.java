package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by donarus on 18.4.15.
 */
public final class FileSystemsInfo implements Serializable {

    private static final long serialVersionUID = 5726767821344263004L;

    private List<FileSystemInfo> fileSystemsInfo = new ArrayList<>();

    public FileSystemsInfo addFileSystemInfo(FileSystemInfo fileSystemInfo) {
        fileSystemsInfo.add(fileSystemInfo);
        return this;
    }

    public List<FileSystemInfo> getFileSystemsInfo() {
        return fileSystemsInfo;
    }
}
