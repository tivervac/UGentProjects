package be.ugent.vopro1.interactor.authentication;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds instances of AuthenticationHandlers.
 *
 * @see AuthenticationHandler
 * @see HeaderAuthenticationHandlerImpl
 */
public class AuthenticationFactory {

    private static Map<String, AuthenticationHandler> INSTANCES;

    /**
     * Sets custom AuthenticationHandlers, for dependency injection and tests
     *
     * @param authenticationHandlers AuthenticationHandlers to set and return in future {@link #getInstance(String)} calls
     */
    public static void setInstances(Map<String, AuthenticationHandler> authenticationHandlers) {
        INSTANCES = authenticationHandlers;
    }

    /**
     * Retrieves an AuthenticationHandler, for dependency injection and tests
     *
     * @param handler Handler to retrieve
     * @param <T> Type of the handler to return
     * @return Requested AuthenticationHandler
     */
    @SuppressWarnings("unchecked")
    public static <T extends AuthenticationHandler> T getInstance(String handler) {
        if (INSTANCES == null) {
            INSTANCES = new HashMap<>();
            INSTANCES.put("header", new HeaderAuthenticationHandlerImpl());
        }

        return (T) INSTANCES.get(handler);
    }

}