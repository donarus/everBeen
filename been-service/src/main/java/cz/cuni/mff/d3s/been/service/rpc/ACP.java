package cz.cuni.mff.d3s.been.service.rpc;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ACP implements ApplicationContextAware {
    private static ApplicationContext context;

    private ACP() {}

    @Override
    public void setApplicationContext(ApplicationContext ac)
            throws BeansException {
        context = ac;
    }

    public static ApplicationContext getApplicationContext() {
        return new ACP().context;
    }
}
