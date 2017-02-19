package be.ugent.vopro1.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Provides the correct {@link ApplicationContext}.
 *
 * @see ApplicationContextAware
 */
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * A getter for the correct ApplicationContext.
     *
     * @return the context of this provider.
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * {@inheritDoc}
     *
     * @param ac {@inheritDoc}
     * @throws BeansException if there's something wrong with a Bean.
     */
    @Override
    public void setApplicationContext(ApplicationContext ac)
            throws BeansException {
        context = ac;
    }
}
