package cz.cuni.mff.d3s.been.node

import spock.lang.Specification

import java.security.Permission

import static cz.cuni.mff.d3s.been.node.StatusCode.*

class StatusCodeTest extends Specification {

    def setupSpec() {
        System.setSecurityManager(new NoExitSecurityManager());
    }

    def cleanupSpec() {
        System.setSecurityManager(null);
    }

    def 'exit is called'() {
        when:
            code.sysExit()

        then:
            def ex = thrown(ExitException)
            ex.status == code.getCode()

        where:
            code << [EX_OK, EX_COMPONENT_FAILED, EX_NETWORK_ERROR, EX_OTHER, EX_USAGE, EX_INVALID_BEEN_SERVICES ]
    }

    def 'all exit codes are unique'() {
        when:
            def codes = StatusCode.values()*.code

        then:
            codes == codes.unique()

    }

    protected static class ExitException extends SecurityException {
        public final int status

        public ExitException(int status) {
            super("There is no escape!")
            this.status = status
        }
    }

    private static class NoExitSecurityManager extends SecurityManager {
        @Override
        public void checkPermission(Permission perm) {
            // allow anything.
        }

        @Override
        public void checkPermission(Permission perm, Object context) {
            // allow anything.
        }

        @Override
        public void checkExit(int status) {
            super.checkExit(status)
            throw new ExitException(status)
        }
    }

}
