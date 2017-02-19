package be.ugent.vopro1.interactor.permission;

import be.ugent.vopro1.interactor.entity.EntityPermission;
import be.ugent.vopro1.interactor.entity.EntityPermissionImpl;
import be.ugent.vopro1.interactor.project.ProjectPermission;
import be.ugent.vopro1.interactor.project.ProjectPermissionImpl;
import be.ugent.vopro1.interactor.team.TeamPermission;
import be.ugent.vopro1.interactor.team.TeamPermissionImpl;
import be.ugent.vopro1.interactor.user.UserPermission;
import be.ugent.vopro1.interactor.user.UserPermissionImpl;
import be.ugent.vopro1.persistence.EntityDAO;
import be.ugent.vopro1.persistence.ProjectDAO;
import be.ugent.vopro1.persistence.UserDAO;
import be.ugent.vopro1.persistence.factory.*;

import java.util.HashMap;

/**
 * Provides the correct {@link PermissionFactory} based on a String parameter
 *
 * @see PermissionFactory
 * @see PermissionFactoryImpl
 */
public class PermissionProvider {

    private static HashMap<String, PermissionFactory> factories;
    static {
        setDefault();
    }

    /**
     * Sets a custom {@link PermissionFactory} for a certain name parameter, for dependency
     * injection and tests
     *
     * @param name Name parameter of this factory. For example "entity"
     * or "project".
     * @param factory Factory to set and return in future
     * {@link #getInstance(String name)} calls
     * @see PermissionFactory
     */
    public static void setInstance(String name, PermissionFactory factory) {
        factories.put(name, factory);
    }

    /**
     * Retrieves a {@link PermissionFactory} instance based on the name parameter
     *
     * @param name Name of the factory to retrieve
     * @return the requested PermissionFactory instance
     * @see PermissionFactory
     */
    public static PermissionFactory getInstance(String name) {
        return factories.get(name);
    }

    /**
     * Convenience method that retrieves the instance provided by the {@link PermissionFactory}
     * immediately.
     *
     * @param name Name of the factory to retrieve
     * @param <T> Type of the PermissionInteractor that should be provided
     * @return the requested PermissionInteractor
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String name) {
        return (T) factories.get(name).getInstance();
    }

    /**
     * Convenience method that sets the instance provided by the {@link PermissionFactory}
     * immediately.
     *
     * @param name Name of the factory to retrieve
     * @param permission PermissionInteractor should be provided in the future
     * @param <T> Type of the PermissionInteractor is provided
     */
    @SuppressWarnings("unchecked")
    public static <T> void set(String name, T permission) {
        factories.get(name).setInstance(permission);
    }

    /**
     * Fills up the factories HashMap with the default factory name for each
     * PermissionInteractor.
     *
     * @see EntityPermissionImpl
     * @see ProjectPermissionImpl
     * @see UserPermissionImpl
     * @see TeamPermissionImpl
     */
    public static void setDefault() {
        factories = new HashMap<>();
        factories.put("entity", new PermissionFactoryImpl<>(EntityPermission.class, "entityPermissionHandler"));
        factories.put("project", new PermissionFactoryImpl<>(ProjectPermission.class, "projectPermissionHandler"));
        factories.put("user", new PermissionFactoryImpl<>(UserPermission.class, "userPermissionHandler"));
        factories.put("team", new PermissionFactoryImpl<>(TeamPermission.class, "teamPermissionHandler"));
    }
}
