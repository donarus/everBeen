package cz.cuni.mff.d3s.been.detectors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by donarus on 20.4.15.
 */
public class FileSystemsUsage implements Serializable {

    private static final long serialVersionUID = -2688395588523429904L;

    private List<FileSystemUsage> fileSystemsUsage = new ArrayList<>();

    public FileSystemsUsage addFileSystemsUsage(FileSystemUsage fileSystemUsage) {
        fileSystemsUsage.add(fileSystemUsage);
        return this;
    }

    public List<FileSystemUsage> getFileSystemsUsages() {
        return fileSystemsUsage;
    }

}
