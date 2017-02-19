package be.ugent.vopro1.interactor.permission;

import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Default implementation of a {@link PermissionFactory}
 *
 * @param <T> Type of permission handler to create
 */
public class PermissionFactoryImpl<T> implements PermissionFactory<T> {

    private final String beanName;
    private final Class<T> beanClass;
    private T instance;

    /**
     * Creates a new PermissionFactoryImpl for given PermissionInteractor
     * @param beanName Name of the permission bean in the application context
     * @param beanClass Class of the permission bean in the application context
     */
    public PermissionFactoryImpl(Class<T> beanClass, String beanName) {
        this.beanClass = beanClass;
        this.beanName = beanName;
    }

    @Override
    public void setInstance(T instance) {
        this.instance = instance;
    }

    @Override
    public T getInstance() {
        if (instance == null) {
            this.instance = ApplicationContextProvider.getApplicationContext().getBean(beanName, beanClass);
        }

        return instance;
    }

}
