package cz.cuni.mff.d3s.been.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

public final class ACP implements ApplicationContextAware {
    private static ApplicationContext context;

    ACP() {}

    @Override
    public void setApplicationContext(ApplicationContext ac)
            throws BeansException {
        context = ac;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }
}
