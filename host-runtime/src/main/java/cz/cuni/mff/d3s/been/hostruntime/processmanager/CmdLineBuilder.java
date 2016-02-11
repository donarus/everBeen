package cz.cuni.mff.d3s.been.hostruntime.processmanager;

import cz.cuni.mff.d3s.been.commons.Bpk;
import cz.cuni.mff.d3s.been.commons.JavaBpk;
import org.apache.commons.exec.CommandLine;

import java.io.File;

public class CmdLineBuilder {

    public CommandLine build(RunningTaskHandler runningTaskHandler) {
        Bpk bpk = runningTaskHandler.getBpk();
        if (bpk instanceof JavaBpk) {
            return buildJavaCommandLine((JavaBpk) bpk);
        } else {
            throw new RuntimeException("unsupported type of bpk");
            // FIXME unsupported type
        }
    }

    private CommandLine buildJavaCommandLine(JavaBpk bpk) {
        String[] libraries = bpk.getLibraries().values().stream()
                .map(File::getAbsolutePath).toArray(s -> new String[s]);

        return new CommandLine("java")
                .addArgument("-jar")
                .addArgument(bpk.getJarFile().getAbsolutePath())
                .addArguments("-cp")
                .addArguments(libraries);
    }

}
