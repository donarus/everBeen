package cz.cuni.mff.d3s.been.plugerized

import cz.cuni.mff.d3s.been.pluger.PlugerException
import cz.cuni.mff.d3s.been.pluger.impl.Pluger
import org.apache.commons.cli.Option
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class Bootstrap {

    private static final Logger LOG = LoggerFactory.getLogger(Bootstrap)

    private String[] beenOpts
    private File workingDirectory
    private boolean clearLibDir

    public static void main(String[] args) {
        def cli = new CliBuilder(usage: " ")

        cli.with {
            h longOpt: 'help',
                    optionalArg: true,
                    'Print this help.'

            w longOpt: 'workdir',
                    args: 1,
                    argName: 'path',
                    optionalArg: false,
                    'Set working directory - current WD by default.'

            o longOpt: 'opts',
                    args: Option.UNLIMITED_VALUES,
                    argName: 'option',
                    optionalArg: true,
                    valueSeparator: ',' as char,
                    'Comma separated options passed to plugins.'

            c longOpt: 'clean',
                    optionalArg: true,
                    'Clean start.'
        }

        def options = cli.parse(args)

        if (options.'help') {
            cli.usage()
            System.exit(0)
        }

        def workingDirectory = (options.'workdir') ? new File(options.'workdir') : new File(".")
        def beenOpts = (options.'optss') ? options.'optss' as String[] : [] as String[]
        def cleanLibDir = (options.'clean') ? true : false

        def bootstrap = new Bootstrap(beenOpts, workingDirectory, cleanLibDir)
        bootstrap.bootstrap()
    }

    public Bootstrap(String[] beenOpts, File workingDirectory, boolean clearLibDir) {
        this.clearLibDir = clearLibDir
        this.workingDirectory = workingDirectory
        this.beenOpts = beenOpts
    }

    public void bootstrap() {
        def config = [
                (Pluger.PLUGER_STARTUP_ARGS)  : beenOpts,
                (Pluger.WORKING_DIRECTORY_KEY): workingDirectory.toPath(),
                (Pluger.CLEAR_LIB_DIR_KEY)    : clearLibDir
        ]
        try {
            def pluger = Pluger.create(config)
            pluger.start()
        } catch (PlugerException e) {
            LOG.error("Pluger framework can't be initialized", e)
            System.exit(100)
        } catch (Exception e) {
            LOG.error("Pluger framework was unable to start", e)
            System.exit(101)
        }
    }

}
